package com.rpg.validation;

import com.rpg.core.Character;
import com.rpg.settings.GameSettings;

/**
 * Vérifie la limite globale STR+AGI+INT+ATK+DEF imposée par GameSettings (Singleton).
 */
public class StatTotalValidator extends BaseValidator {
    @Override
    public void validate(Character c, ValidationContext ctx, ValidationResult res) {
        GameSettings s = ctx.getSettings();
        int sum = c.getStrength() + c.getAgility() + c.getIntelligence()
                + c.getAttack() + c.getDefense();
        if (sum > s.getMaxStatPoints()) {
            res.addError("Somme des stats (" + sum + ") > max autorisé (" + s.getMaxStatPoints() + ").");
        }
        next(c, ctx, res);
    }
}

