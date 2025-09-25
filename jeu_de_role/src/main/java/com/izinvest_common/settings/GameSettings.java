package com.izinvest_common.settings;

import com.izinvest_common.core.Character;

/**
 * Singleton pour les règles globales du jeu.
 * - Ici : limite sur la somme STR+AGI+INT+ATK+DEF
 * - Méthode isValid(Character) pour vérifier un perso
 */
public class GameSettings {

    // Instance unique (instantiation EAGER pour simplicité)
    private static final GameSettings INSTANCE = new GameSettings();

    /** Somme max autorisée pour STR+AGI+INT+ATK+DEF (ajustable). */
    private int maxStatPoints = 250;

    private GameSettings() {}

    public static GameSettings getInstance() {
        return INSTANCE;
    }

    public int getMaxStatPoints() {
        return maxStatPoints;
    }

    public void setMaxStatPoints(int maxStatPoints) {
        this.maxStatPoints = maxStatPoints;
    }

    /**
     * Vérifie si le personnage respecte la règle de somme max + nom non vide.
     */
    public boolean isValid(Character c) {
        if (c == null) return false;
        int sum = c.getStrength() + c.getAgility() + c.getIntelligence()
                + c.getAttack() + c.getDefense();
        return sum <= maxStatPoints && c.getName() != null && !c.getName().isBlank();
    }
}

