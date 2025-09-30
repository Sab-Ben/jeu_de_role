package com.rpg.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe de base "Character" (entité du domaine).
 *
 * Exigences du sujet :
 * - Attributs : name, strength, agility, intelligence
 * - Méthodes : getPowerLevel(), getDescription()
 *
 * Conception :
 * - Classe immuable (tous les champs final) → construction via le Builder.
 * - getPowerLevel() propose une formule simple que tu peux ajuster.
 */
public class Character {

    private final String name;
    private final int strength;
    private final int agility;
    private final int intelligence;
    private final int attack;
    private final int defense;
    private final List<String> equipment;

    // Constructeur package-private : on force l'usage du Builder.
    public Character(String name, int strength, int agility, int intelligence, int attack, int defense, List<String> equipment) {
        this.name = Objects.requireNonNull(name, "name");
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.attack = attack;
        this.defense = defense;
        // Copie immuable pour garantir l'immutabilité
        this.equipment = Collections.unmodifiableList(new ArrayList<>(equipment == null ? List.of() : equipment));
    }

    // Getters
    public String getName() { return name; }
    public int getStrength() { return strength; }
    public int getAgility() { return agility; }
    public int getIntelligence() { return intelligence; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public List<String> getEquipment() { return equipment; }

    /**
     * Score de puissance "global".
     * - STR/AGI/INT comptent fort (×2)
     * - ATK ajoute un bonus
     * - DEF apporte la moitié en bonus
     * - +1 par item d'équipement
     */
    public int getPowerLevel() {
        int base = 2 * (strength + agility + intelligence);
        int gear = equipment.size();
        return base + attack + (defense / 2) + gear;
    }

    /**
     * Description textuelle lisible du personnage.
     * Les décorateurs concatèneront leur propre description.
     */
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(name).append("] ")
                .append("(Force = ").append(strength)
                .append(", Agilité = ").append(agility)
                .append(", Intelligence = ").append(intelligence)
                .append(", Attaque = ").append(attack)
                .append(", Défense = ").append(defense)
                .append(")");
        if (!equipment.isEmpty()) {
            sb.append(" | Equipement: ").append(String.join(", ", equipment));
        } else {
            sb.append(" | Equipement: aucun");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getDescription() + " | Power=" + getPowerLevel();
    }
}

