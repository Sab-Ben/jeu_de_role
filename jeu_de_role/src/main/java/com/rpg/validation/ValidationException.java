package com.rpg.validation;

import java.util.List;

/**
 * Exception lancée par le Builder quand la validation agrégée échoue.
 * Contient toutes les erreurs pour une remontée claire côté IHM/tests.
 */
public class ValidationException extends RuntimeException {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super("Validation échouée: " + String.join(" | ", errors));
        this.errors = List.copyOf(errors);
    }

    public List<String> getErrors() { return errors; }
}
