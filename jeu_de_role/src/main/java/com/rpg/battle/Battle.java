package com.rpg.battle;

import com.rpg.core.Character;
import com.rpg.decorator.AbilityHook;
import com.rpg.decorator.CharacterComponent;
import com.rpg.decorator.CharacterDecorator;
import com.rpg.observer.GameEvent;
import com.rpg.observer.Subject;
import com.rpg.settings.GameSettings;

/**
 * Moteur de combat très simple :
 * - possède deux BattleUnit (A, B) et une DamageStrategy
 * - expose une Subject pour notifier les observateurs (journal)
 * - les commandes (Command) viendront appeler attack/defend/usePower(...)
 */
public class Battle extends Subject {
    private final BattleUnit unitA;
    private final BattleUnit unitB;
    private final DamageStrategy damage;
    private int round = 1;

    public Battle(Character a, Character b, DamageStrategy strategy) {
        this.unitA = new BattleUnit(a);
        this.unitB = new BattleUnit(b);
        this.damage = strategy;
    }

    public BattleUnit getUnitA() { return unitA; }
    public BattleUnit getUnitB() { return unitB; }

    /** Cap “stalemate” si on dépasse le nombre de tours autorisé. */
    private boolean checkStalemate() {
        int cap = GameSettings.getInstance().getMaxRounds();
        if (round > cap) {
            notifyObservers(GameEvent.stalemate(round));
            return true;
        }
        return false;
    }

    private void callHooks(Character c, java.util.function.Consumer<AbilityHook> call) {
        if (c instanceof CharacterComponent) {
            CharacterComponent cur = (CharacterComponent) c;
            while (cur instanceof CharacterDecorator) {
                CharacterDecorator dec = (CharacterDecorator) cur;
                call.accept(dec);
                cur = dec.getDecorated();
            }
        }
    }

    /** Une attaque standard de 'attacker' vers 'defender'. */
    public void attack(BattleUnit attacker, BattleUnit defender) {
        if (checkStalemate()) return;

        int base = damage.computeDamage(attacker.getCharacter(), defender.getCharacter());
        base -= defender.consumeDefenseBoost(); // garde réduit les dégâts une seule fois
        base = Math.max(1, base);

        // -- Appel des hooks décorateurs accumulés (avant application)
        int finalBase = base;
        AbilityHook.OnAttackContext ctx = new AbilityHook.OnAttackContext() {
            private int b = finalBase;
            public Character attacker(){ return attacker.getCharacter(); }
            public Character defender(){ return defender.getCharacter(); }
            public int baseDamage(){ return b; }
            public void setBaseDamage(int v){ b = Math.max(1, v); }
        };
        // Parcours de la pile de décorateurs côté ATT et DEF
        callHooks(attacker.getCharacter(), h -> h.onBeforeAttack(ctx));
        callHooks(defender.getCharacter(), h -> h.onBeforeAttack(ctx));
        int finalDmg = ctx.baseDamage();

        defender.damage(finalDmg);

        notifyObservers(GameEvent.attack(
                attacker.getCharacter().getName(),
                defender.getCharacter().getName(),
                finalDmg, defender.getHp(), round));

        round++;
    }

    /** Posture défensive : prépare un boost qui sera consommé à la prochaine attaque subie. */
    public void defend(BattleUnit unit) {
        unit.addDefenseBoost(GameSettings.getInstance().getDefendBoost());
        notifyObservers(GameEvent.defend(unit.getCharacter().getName(), round));
        round++;
    }

    /** Pouvoir générique : petit burst dépendant de l'INT. */
    public void usePower(BattleUnit caster, BattleUnit target) {
        // Exemple simple: “pouvoir” = mini-burst basé sur INT
        int dmg = Math.max(1, caster.getCharacter().getIntelligence()/2);
        target.damage(dmg);
        notifyObservers(GameEvent.power(
                caster.getCharacter().getName(), target.getCharacter().getName(), dmg, target.getHp(), round));
        round++;
    }

    public BattleUnit findByName(String name) {
        String n = name.trim();
        if (unitA.getCharacter().getName().equalsIgnoreCase(n)) return unitA;
        if (unitB.getCharacter().getName().equalsIgnoreCase(n)) return unitB;
        throw new IllegalArgumentException("Aucun combattant ne se nomme '"+name+"'");
    }

    public boolean isOver() { return !unitA.isAlive() || !unitB.isAlive(); }

    public String winnerNameOrNull() {
        if (!isOver()) return null;
        if (unitA.getHp() == unitB.getHp()) return null; // égalité
        return (unitA.getHp() > unitB.getHp()) ? unitA.getCharacter().getName()
                : unitB.getCharacter().getName();
    }

    /** Récupère l'unité par nom de personnage (insensible à la casse) — utile pour le replay fichier. */
    public BattleUnit getUnitByName(String name) {
        String n = name == null ? "" : name.trim();
        if (unitA.getCharacter().getName().equalsIgnoreCase(n)) return unitA;
        if (unitB.getCharacter().getName().equalsIgnoreCase(n)) return unitB;
        throw new IllegalArgumentException("Aucun combattant ne se nomme '" + name + "'");
    }

    /** Utilitaire : renvoie l'adversaire direct de 'unit' dans ce duel. */
    public BattleUnit opponentOf(BattleUnit unit) {
        if (unit == unitA) return unitB;
        if (unit == unitB) return unitA;
        throw new IllegalArgumentException("Unit inconnue dans ce Battle");
    }

}



