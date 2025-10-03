// src/main/java/com/rpg/decorator/Invisibility.java
package com.rpg.decorator;

import com.rpg.settings.GameSettings;

/** +X puissance, et atténuation de dégâts via hook onBeforeAttack (si c’est la cible). */
public class Invisibility extends CharacterDecorator {
    public Invisibility(CharacterComponent delegate) { super(delegate); }

    @Override public String getDescription() {
        return super.getDescription() + " | Capacité: Invisibilité";
    }

    @Override public int getPowerLevel() {
        return super.getPowerLevel() + GameSettings.getInstance().getBasePowerInvisibility();
    }

    @Override
    public void onBeforeAttack(OnAttackContext ctx) {
        // Si ce décorateur enveloppe la défense, réduire légèrement le dommage avant application
        if (getInner().equals(ctx.defender())) {
            ctx.setBaseDamage(Math.max(1, (int)Math.floor(ctx.baseDamage() * 0.9))); // -10%
        }
    }
}
