package com.izinvest_common.decorator;

/**
 * Décorateur "Invisibilité".
 * - Ajoute une mention à la description
 * - +5 de puissance (valeur arbitraire)
 */
public class Invisibility extends CharacterDecorator {
    public Invisibility(CharacterComponent delegate) {
        super(delegate);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " | Capacité: Invisibilité";
    }

    @Override
    public int getPowerLevel() {
        return super.getPowerLevel() + 5;
    }
}

