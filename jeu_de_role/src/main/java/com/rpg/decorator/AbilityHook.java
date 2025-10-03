// src/main/java/com/rpg/decorator/AbilityHook.java
package com.rpg.decorator;

import com.rpg.core.Character;

/**
 * Hooks optionnels appelés par le moteur de combat (Battle).
 * Permettent de modifier le flux sans instanceof.
 */
public interface AbilityHook {
    default void onBeforeAttack(OnAttackContext ctx) { /* no-op */ }
    default void onAfterDamage(OnAttackContext ctx) { /* no-op */ }

    interface OnAttackContext {
        Character attacker();
        Character defender();
        int baseDamage();
        void setBaseDamage(int v); // permet d’ajuster les dégâts avant application
    }
}
