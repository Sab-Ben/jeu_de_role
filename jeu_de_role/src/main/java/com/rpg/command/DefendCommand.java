package com.rpg.command;

import com.rpg.battle.Battle;
import com.rpg.battle.BattleUnit;
import com.rpg.replay.ActionRecord;

/** Commande "défendre". */
public class DefendCommand implements RecordableCommand {
    private final Battle battle;
    private final BattleUnit who;

    public DefendCommand(Battle battle, BattleUnit who) {
        this.battle = battle; this.who = who;
    }

    @Override public void execute() { battle.defend(who); }
    @Override public void undo() { /* no-op dans cette version */ }
    @Override public String label() { return who.getCharacter().getName() + " se défend"; }

    @Override
    public ActionRecord toRecord() {
        return new ActionRecord(ActionRecord.Type.DEFEND,
                who.getCharacter().getName(), "");
    }
}

