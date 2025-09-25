package com.izinvest_common.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Représente une équipe (groupe) de personnages.
 * - Ajout/suppression
 * - Lecture immuable de la liste
 * - Calcul de la puissance totale
 * - Tri par nom ou par puissance
 */
public class Party {
    private final List<Character> members = new ArrayList<>();

    public void add(Character c) {
        if (c != null) members.add(c);
    }

    public void remove(Character c) {
        members.remove(c);
    }

    public List<Character> getMembers() {
        return List.copyOf(members);
    }

    /** Puissance totale (somme des PowerLevel). */
    public int getTotalPower() {
        return members.stream().mapToInt(Character::getPowerLevel).sum();
    }

    /** Tri sur place par nom. */
    public void sortByName() {
        members.sort(Comparator.comparing(Character::getName));
    }

    /** Tri sur place par puissance décroissante. */
    public void sortByPowerDesc() {
        members.sort(Comparator.comparingInt(Character::getPowerLevel).reversed());
    }
}

