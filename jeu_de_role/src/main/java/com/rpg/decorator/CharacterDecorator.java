// src/main/java/com/rpg/decorator/CharacterDecorator.java
package com.rpg.decorator;

import com.rpg.core.Character;

/**
 * Décorateur de personnage empilable + accès au décoré.
 * Implémente AbilityHook par défaut (no-op) pour que chaque décorateur puisse réagir.
 */
public abstract class CharacterDecorator implements CharacterComponent, AbilityHook {
    protected final CharacterComponent delegate;

    protected CharacterDecorator(CharacterComponent delegate) { this.delegate = delegate; }

    /** Accès au décoré pour parcourir la pile. */
    public CharacterComponent getDecorated() { return delegate; }

    @Override public String getDescription() { return delegate.getDescription(); }
    @Override public int getPowerLevel() { return delegate.getPowerLevel(); }
    @Override public Character getInner() { return delegate.getInner(); }
}
