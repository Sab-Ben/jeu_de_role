// src/main/java/com/rpg/command/UsePowerCommand.java
package com.rpg.command;

import com.rpg.battle.Battle;
import com.rpg.battle.BattleUnit;
import com.rpg.replay.ActionRecord;
import com.rpg.settings.GameSettings;
import com.rpg.validation.*;

public class UsePowerCommand implements RecordableCommand {
    private final Battle battle;
    private final BattleUnit caster;
    private final BattleUnit target;
    private final Validator chain; // réutilisable

    public UsePowerCommand(Battle battle, BattleUnit caster, BattleUnit target, Validator chain) {
        this.battle = battle; this.caster = caster; this.target = target; this.chain = chain;
    }

    public UsePowerCommand(Battle battle, BattleUnit caster, BattleUnit target) {
        this(battle, caster, target, null); // fallback
    }

    @Override
    public void execute() {
        // (ex) revalider contraintes (slots, somme de stats après buff, etc.)
        ValidationResult res = new ValidationResult();
        chain.validate(caster.getCharacter(),
                new ValidationContext(GameSettings.getInstance(), /* DAO */ null), res);
        if (!res.isValid()) throw new ValidationException(res.getErrors());

        battle.usePower(caster, target);
    }

    @Override public void undo() { /* no-op ici */ }
    @Override public String label() { return caster.getCharacter().getName() + " utilise un pouvoir"; }

    @Override
    public ActionRecord toRecord() {
        return new ActionRecord(ActionRecord.Type.POWER,
                caster.getCharacter().getName(), target.getCharacter().getName());
    }
}
