package com.rpg.decorator;

import com.rpg.core.Character;

/**
 * Adaptateur simple : wrap un Character "nu" en CharacterComponent.
 * Point de départ de la chaîne de décorateurs.
 */
public class BaseComponent implements CharacterComponent {
    private final Character base;

    public BaseComponent(Character base) {
        this.base = base;
    }

    @Override
    public String getDescription() {
        return base.getDescription();
    }

    @Override
    public int getPowerLevel() {
        return base.getPowerLevel();
    }

    @Override
    public Character getInner() {
        return base;
    }
}

