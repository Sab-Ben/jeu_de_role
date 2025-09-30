package com.rpg.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Petit objet de résultat cumulant les erreurs de validation.
 * - isValid() => vrai si aucune erreur.
 * - addError(...) pour ajouter un message lisible côté IHM/console.
 */
public class ValidationResult {
    private final List<String> errors = new ArrayList<>();

    public void addError(String msg) {
        if (msg != null && !msg.isBlank()) {
            errors.add(msg);
        }
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    @Override
    public String toString() {
        return isValid() ? "OK" : String.join("; ", errors);
    }
}

