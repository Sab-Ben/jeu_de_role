package com.rpg.validation;

import com.rpg.core.Character;

/**
 * Contrat d'un maillon de la chaîne de validation.
 * Chaque Validator :
 *   - consulte le Character (et éventuellement des services fournis via contexte)
 *   - ajoute des erreurs au ValidationResult
 *   - délègue au maillon suivant
 */
public interface Validator {
    void setNext(Validator next);

    /**
     * Valide le personnage et transmet au maillon suivant si présent.
     * @param character personnage à valider
     * @param context   contexte optionnel (DAO, settings, etc.)
     * @param result    panier d'erreurs cumulées
     */
    void validate(Character character, ValidationContext context, ValidationResult result);
}
