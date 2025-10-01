package com.rpg.command;

import com.rpg.battle.Battle;
import com.rpg.battle.BattleUnit;

/**
 * Commande "attaquer".
 * - garde un pointeur vers le moteur (receiver) et les 2 unités concernées
 * - undo() est ici naïf (on ne récupère pas le montant exact infligé sans journal fin),
 *   mais on montre l'interface ; pour un vrai undo, on journaliserait l'HP avant/après.
 */
public class AttackCommand implements GameCommand {
    private final Battle battle;
    private final BattleUnit attacker;
    private final BattleUnit defender;

    public AttackCommand(Battle battle, BattleUnit attacker, BattleUnit defender) {
        this.battle = battle; this.attacker = attacker; this.defender = defender;
    }

    @Override public void execute() { battle.attack(attacker, defender); }
    @Override public void undo() { /* simplifié : pas d'undo fidèle */ }
    @Override public String label() { return attacker.getCharacter().getName() + " attaque"; }
}

