package com.izinvest_common.decorator;

import com.izinvest_common.core.Character;

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
