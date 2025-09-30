package com.rpg.builder;

import java.util.ArrayList;
import java.util.List;

import com.rpg.core.Character;

/**
 * Pattern BUILDER : construction fluide d'un Character immuable.
 * Avantages :
 * - Lisibilité (chaînage des setters)
 * - Valeurs par défaut sensées
 * - Validation possible avant build() si besoin
 */
public class CharacterBuilder {
    private String name = "SansNom";
    private int strength = 1;
    private int agility = 1;
    private int intelligence = 1;
    private int attack = 1;
    private int defense = 0;
    private final List<String> equipment = new ArrayList<>();

    public CharacterBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CharacterBuilder setStrength(int strength) {
        this.strength = strength;
        return this;
    }

    public CharacterBuilder setAgility(int agility) {
        this.agility = agility;
        return this;
    }

    public CharacterBuilder setIntelligence(int intelligence) {
        this.intelligence = intelligence;
        return this;
    }

    public CharacterBuilder setAttack(int attack) {
        this.attack = attack;
        return this;
    }

    public CharacterBuilder setDefense(int defense) {
        this.defense = defense;
        return this;
    }

    public CharacterBuilder addEquipment(String item) {
        if (item != null && !item.isBlank()) {
            this.equipment.add(item);
        }
        return this;
    }

    public Character build() {
        return new Character(name, strength, agility, intelligence, attack, defense, equipment);
    }
}
