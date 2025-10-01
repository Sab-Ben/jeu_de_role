package com.rpg.battle;

import com.rpg.core.Character;

/**
 * Stratégie par défaut :
 * dmg = ATK + STR/4 + AGI/6 - DEF/2, au minimum 1.
 */
public class SimpleDamageStrategy implements DamageStrategy {
    @Override
    public int computeDamage(Character a, Character d) {
        int dmg = a.getAttack() + (a.getStrength()/4) + (a.getAgility()/6) - (d.getDefense()/2);
        return Math.max(1, dmg);
    }
}
