package com.rpg.validation;

import com.rpg.core.Character;

/**
 * Valide le nom :
 * - non vide
 * - 3..30 caractères alphanum + espaces et tirets
 */
public class NameFormatValidator extends BaseValidator {
    @Override
    public void validate(Character c, ValidationContext ctx, ValidationResult res) {
        String name = c.getName();
        if (name == null || name.isBlank()) {
            res.addError("Le nom est obligatoire.");
        } else if (!name.matches("[\\p{L}\\p{N} \\-]{3,30}")) {
            res.addError("Nom invalide (3..30 caractères, lettres/chiffres/espaces/tirets).");
        }
        next(c, ctx, res);
    }
}

