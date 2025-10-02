package com.rpg.settings;

import java.util.regex.Pattern;
import com.rpg.core.Character;

/**
 * Singleton contenant toutes les règles globales (évite les magic numbers).
 */
public class GameSettings {
    private static final GameSettings INSTANCE = new GameSettings();
    public static GameSettings getInstance() { return INSTANCE; }

    // --- Validation personnage ---
    private int maxStatPoints = 60;                   // STR+AGI+INT+ATK+DEF
    private Pattern namePattern = Pattern.compile("^[\\p{L}0-9\\- ]{3,30}$");

    // --- Combat ---
    private int maxRounds = 50;                       // Cap de tours pour "stalemate"
    private int basePowerInvisibility = 5;            // Bonus décorateur
    private int defendBoost = 3;                      // DEF temporaire sur Defend

    // --- Composite ---
    private int maxMembersPerGroup = 10;

    private GameSettings(){}

    public int getMaxStatPoints(){ return maxStatPoints; }
    public void setMaxStatPoints(int v){ this.maxStatPoints = Math.max(1, v); }

    public Pattern getNamePattern(){ return namePattern; }
    public void setNamePattern(Pattern p){ this.namePattern = p; }

    public int getMaxRounds(){ return maxRounds; }
    public void setMaxRounds(int v){ this.maxRounds = Math.max(1, v); }

    public int getBasePowerInvisibility(){ return basePowerInvisibility; }
    public void setBasePowerInvisibility(int v){ this.basePowerInvisibility = Math.max(0, v); }

    public int getDefendBoost(){ return defendBoost; }
    public void setDefendBoost(int v){ this.defendBoost = Math.max(0, v); }

    public int getMaxMembersPerGroup(){ return maxMembersPerGroup; }
    public void setMaxMembersPerGroup(int v){ this.maxMembersPerGroup = Math.max(1, v); }

    /** Validation minimale globale (optionnelle) */
    public boolean isValid(Character c) {
        int sum = c.getStrength() + c.getAgility() + c.getIntelligence()
                + c.getAttack() + c.getDefense();
        return sum <= maxStatPoints && c.getName() != null && !c.getName().isBlank();
    }
}
