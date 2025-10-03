// src/main/java/com/rpg/battle/RandomizedDamageStrategy.java
package com.rpg.battle;

import java.util.Random;
import com.rpg.core.Character;

/**
 * Dégâts avec légère variance contrôlée par Random injecté (seedable).
 * base = ATK + STR/4 + AGI/6 - DEF/2 ; variance ±10%
 */
public class RandomizedDamageStrategy implements DamageStrategy {
    private final Random rnd;

    public RandomizedDamageStrategy(Random rnd){ this.rnd = rnd; }

    @Override
    public int computeDamage(Character a, Character d) {
        int base = a.getAttack() + (a.getStrength()/4) + (a.getAgility()/6) - (d.getDefense()/2);
        base = Math.max(1, base);
        double factor = 0.9 + 0.2 * rnd.nextDouble();
        return Math.max(1, (int)Math.round(base * factor));
    }
}
