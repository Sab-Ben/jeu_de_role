package com.rpg.decorator;

import com.rpg.core.Character;

/**
 * Classe abstraite de décorateur :
 * - Délègue par défaut aux méthodes du composant décoré
 * - Les sous-classes n'ajoutent que la logique différentielle
 */
public abstract class CharacterDecorator implements CharacterComponent {
    protected final CharacterComponent delegate;

    protected CharacterDecorator(CharacterComponent delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getDescription() {
        return delegate.getDescription();
    }

    @Override
    public int getPowerLevel() {
        return delegate.getPowerLevel();
    }

    @Override
    public Character getInner() {
        return delegate.getInner();
    }
}
