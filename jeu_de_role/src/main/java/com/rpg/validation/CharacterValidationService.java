package com.rpg.validation;

import com.rpg.core.Character;

/**
 * Service qui construit et exécute la chaîne de responsabilité.
 * Utilisation :
 *   ValidationResult vr = new CharacterValidationService(ctx).validate(c);
 */
public class CharacterValidationService {
    private final ValidationContext context;
    private final Validator head;

    public CharacterValidationService(ValidationContext context) {
        this.context = context;

        // Construction de la chaîne : format → unicité → total stats
        NameFormatValidator v1 = new NameFormatValidator();
        UniqueNameValidator v2 = new UniqueNameValidator();
        StatTotalValidator v3 = new StatTotalValidator();

        v1.setNext(v2); v2.setNext(v3);
        this.head = v1;
    }

    public ValidationResult validate(Character c) {
        ValidationResult result = new ValidationResult();
        head.validate(c, context, result);
        return result;
    }
}

