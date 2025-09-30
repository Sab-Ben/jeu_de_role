package com.rpg.validation;

import com.rpg.dao.CharacterDAO;
import com.rpg.settings.GameSettings;

/**
 * Contexte passé dans la chaîne de validation (accès aux singletons/services).
 * Ici on expose GameSettings et CharacterDAO. On peut l'étendre plus tard.
 */
public class ValidationContext {
    private final GameSettings settings;
    private final CharacterDAO dao;

    public ValidationContext(GameSettings settings, CharacterDAO dao) {
        this.settings = settings;
        this.dao = dao;
    }

    public GameSettings getSettings() { return settings; }
    public CharacterDAO getDao() { return dao; }
}

