package com.rpg.validation.chain;

import com.rpg.validation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Permet de composer dynamiquement une chaîne de validateurs.
 * Exemple:
 *   Validator chain = ValidationChainBuilder.start()
 *        .add(new NameFormatValidator())
 *        .add(new UniqueNameValidator())
 *        .add(new StatTotalValidator())
 *        .build();
 */
public class ValidationChainBuilder {
    private final List<Validator> chain = new ArrayList<>();

    public static ValidationChainBuilder start() { return new ValidationChainBuilder(); }
    public ValidationChainBuilder add(Validator v) { chain.add(v); return this; }

    public Validator build() {
        if (chain.isEmpty()) throw new IllegalStateException("Chaîne vide");
        for (int i = 0; i < chain.size() - 1; i++) {
            chain.get(i).setNext(chain.get(i + 1));
        }
        return chain.get(0);
    }
}
