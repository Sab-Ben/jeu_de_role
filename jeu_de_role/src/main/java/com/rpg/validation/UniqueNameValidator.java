package com.rpg.validation;

import com.rpg.core.Character;

/**
 * Vérifie l'unicité du nom dans le DAO.
 */
public class UniqueNameValidator extends BaseValidator {
    @Override
    public void validate(Character c, ValidationContext ctx, ValidationResult res) {
        boolean exists = ctx.getDao().findByName(c.getName()).isPresent();
        if (exists) {
            res.addError("Un personnage nommé '" + c.getName() + "' existe déjà.");
        }
        next(c, ctx, res);
    }
}
