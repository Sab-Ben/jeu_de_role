package com.rpg.validation;

import com.rpg.core.Character;

/**
 * Implémentation de base pour éviter de répéter la logique de chaînage.
 */
public abstract class BaseValidator implements Validator {
    private Validator next;

    @Override
    public void setNext(Validator next) {
        this.next = next;
    }

    /** Appeler ceci en fin de validate() pour continuer la chaîne. */
    protected void next(Character c, ValidationContext ctx, ValidationResult res) {
        if (next != null) next.validate(c, ctx, res);
    }
}

