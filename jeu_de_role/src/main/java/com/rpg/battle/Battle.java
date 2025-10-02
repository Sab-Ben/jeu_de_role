package com.rpg.battle;

import com.rpg.core.Character;
import com.rpg.observer.GameEvent;
import com.rpg.observer.Subject;

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

    public Battle(Character a, Character b, DamageStrategy strategy) {
        this.unitA = new BattleUnit(a);
        this.unitB = new BattleUnit(b);
        this.damage = strategy;
    }

    public BattleUnit getUnitA() { return unitA; }
    public BattleUnit getUnitB() { return unitB; }

    /** Une attaque standard de 'attacker' vers 'defender'. */
    public void attack(BattleUnit attacker, BattleUnit defender) {
        int base = damage.computeDamage(attacker.getCharacter(), defender.getCharacter());
        int absorbed = defender.consumeDefenseBoost(); // bonus de défense s'il y a eu "defend" avant
        int finalDmg = Math.max(1, base - absorbed);

        defender.damage(finalDmg);

        notifyObservers(GameEvent.attack(
                attacker.getCharacter().getName(),
                defender.getCharacter().getName(),
                finalDmg,
                defender.getHp()
        ));
    }

    /** Posture défensive : prépare un boost qui sera consommé à la prochaine attaque subie. */
    public void defend(BattleUnit who) {
        int boost = Math.max(1, who.getCharacter().getDefense() / 3);
        who.addDefenseBoost(boost);
        notifyObservers(GameEvent.defend(who.getCharacter().getName(), boost));
    }

    /** Pouvoir générique : petit burst dépendant de l'INT. */
    public void usePower(BattleUnit caster, BattleUnit target) {
        int bonus = Math.max(2, caster.getCharacter().getIntelligence() / 3);
        target.damage(bonus);
        notifyObservers(GameEvent.power(
                caster.getCharacter().getName(), target.getCharacter().getName(), bonus, target.getHp()
        ));
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

