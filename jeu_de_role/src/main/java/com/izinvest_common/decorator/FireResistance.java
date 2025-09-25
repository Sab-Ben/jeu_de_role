package com.izinvest_common.decorator;

/**
 * Décorateur "Résistance au feu".
 * - Ajoute une mention à la description
 * - Bonus proportionnel à la DEF du perso (+DEF/10, min 1)
 */
public class FireResistance extends CharacterDecorator {
    public FireResistance(CharacterComponent delegate) {
        super(delegate);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " | Capacité: Résistance au feu";
    }

    @Override
    public int getPowerLevel() {
        int base = super.getPowerLevel();
        int def = delegate.getInner().getDefense();
        return base + Math.max(1, def / 10);
    }
}

