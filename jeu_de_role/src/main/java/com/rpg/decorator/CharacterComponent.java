package com.rpg.decorator;

import com.rpg.core.Character;

/**
 * Interface décorée : ce que les décorateurs peuvent enrichir.
 * - getDescription() : texte cumulatif
 * - getPowerLevel()  : score cumulatif
 * - getInner()       : accès au Character d'origine si besoin
 */
public interface CharacterComponent {
    String getDescription();
    int getPowerLevel();
    Character getInner();
}
