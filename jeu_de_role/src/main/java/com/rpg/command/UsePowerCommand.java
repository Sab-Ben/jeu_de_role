package com.rpg.command;

import com.rpg.battle.Battle;
import com.rpg.battle.BattleUnit;
import com.rpg.replay.ActionRecord;
import com.rpg.settings.GameSettings;
import com.rpg.validation.*;
import com.rpg.validation.chain.ValidationChainBuilder;

/**
 * Commande "utiliser un pouvoir".
 * - Compatible avec l'ancien appel (sans Validator) : on construit une chaîne par défaut si null.
 * - Si une chaîne explicite est fournie (ex: injectée depuis ailleurs), on l'utilise telle quelle.
 */
public class UsePowerCommand implements RecordableCommand {
    private final Battle battle;
    private final BattleUnit caster;
    private final BattleUnit target;
    /** Peut être null : on fabriquera une chaîne par défaut dans ce cas. */
    private final Validator chain;

    /** Constructeur compatible avec l'existant (main/replay) : pas de chaîne fournie. */
    public UsePowerCommand(Battle battle, BattleUnit caster, BattleUnit target) {
        this(battle, caster, target, null);
    }

    /** Nouveau constructeur permettant d'injecter une chaîne de validation. */
    public UsePowerCommand(Battle battle, BattleUnit caster, BattleUnit target, Validator chain) {
        this.battle = battle;
        this.caster = caster;
        this.target = target;
        this.chain = chain; // peut être null
    }

    /** Construit une chaîne minimale si aucune n'a été fournie. */
    private Validator getOrBuildChain() {
        if (chain != null) return chain;

        // Chaîne "combat" par défaut :
        // - Format du nom (regex des settings)
        // - Somme des stats (plafond des settings)
        // (Pas d'unicité ici : en combat, on n'insère pas dans le DAO)
        return ValidationChainBuilder.start()
                .add(new NameFormatValidator())
                .add(new StatTotalValidator())
                .build();
    }

    @Override
    public void execute() {
        // Revalidation (sécurité) avant application du pouvoir
        ValidationResult res = new ValidationResult();
        Validator v = getOrBuildChain();
        v.validate(
                caster.getCharacter(),
                new ValidationContext(GameSettings.getInstance(), /* dao */ null),
                res
        );
        if (!res.isValid()) {
            throw new ValidationException(res.getErrors());
        }

        battle.usePower(caster, target);
    }

    @Override public void undo() { /* no-op dans cette version */ }

    @Override
    public String label() {
        return caster.getCharacter().getName() + " utilise un pouvoir";
    }

    @Override
    public ActionRecord toRecord() {
        return new ActionRecord(
                ActionRecord.Type.POWER,
                caster.getCharacter().getName(),
                target.getCharacter().getName()
        );
    }
}
