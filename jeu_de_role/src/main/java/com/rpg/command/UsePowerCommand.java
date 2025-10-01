package com.rpg.command;

import com.rpg.battle.Battle;
import com.rpg.battle.BattleUnit;

/** Commande "utiliser un pouvoir" (petit burst basé sur INT). */
public class UsePowerCommand implements GameCommand {
    private final Battle battle;
    private final BattleUnit caster;
    private final BattleUnit target;

    public UsePowerCommand(Battle battle, BattleUnit caster, BattleUnit target) {
        this.battle = battle; this.caster = caster; this.target = target;
    }

    @Override public void execute() { battle.usePower(caster, target); }
    @Override public void undo() { /* no-op dans cette version */ }
    @Override public String label() { return caster.getCharacter().getName() + " utilise un pouvoir"; }
}

