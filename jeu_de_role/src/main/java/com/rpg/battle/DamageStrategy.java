package com.rpg.battle;
import com.rpg.core.Character;

/**
 * (Strategy) Responsable du calcul des dégâts d'une attaque.
 * Permet de changer la formule sans toucher au moteur ni aux commandes.
 */
public interface DamageStrategy {
    int computeDamage(Character attacker, Character defender);
}


