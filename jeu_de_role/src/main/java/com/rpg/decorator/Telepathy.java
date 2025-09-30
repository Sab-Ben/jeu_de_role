package com.rpg.decorator;

/**
 * Décorateur "Télépathie".
 * - Ajoute une mention à la description
 * - Bonus proportionnel à l'INT (+INT/5, min 2)
 */
public class Telepathy extends CharacterDecorator {
    public Telepathy(CharacterComponent delegate) {
        super(delegate);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " | Capacité: Télépathie";
    }

    @Override
    public int getPowerLevel() {
        int base = super.getPowerLevel();
        int intel = delegate.getInner().getIntelligence();
        return base + Math.max(2, intel / 5);
    }
}

