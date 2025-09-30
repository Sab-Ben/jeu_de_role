package com.rpg.composite;

import com.rpg.core.Character;
import java.util.List;

/**
 * Composant racine du Composite.
 * Permet d'obtenir une puissance cumulée et d'itérer tous les personnages feuilles.
 */
public interface GroupComponent {
    String getName();
    int getPowerLevel();

    /** Liste aplatie de tous les personnages contenus dans ce sous-arbre. */
    List<Character> flatten();

    /** Opérations d'édition (nop sur une feuille). */
    default void add(GroupComponent child) { /* nop par défaut */ }
    default void remove(GroupComponent child) { /* nop par défaut */ }
}

