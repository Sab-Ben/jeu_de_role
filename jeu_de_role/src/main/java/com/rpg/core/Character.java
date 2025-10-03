package com.rpg.core;

import java.util.*;

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

public final class Character implements com.rpg.decorator.CharacterComponent {
    private final UUID id;
    private final String name;
    private final int strength, agility, intelligence, attack, defense;
    private final List<String> equipment;

    public Character(String name, int strength, int agility, int intelligence, int attack, int defense, List<String> equipment) {
        this(UUID.randomUUID(), name, strength, agility, intelligence, attack, defense, equipment);
    }
    public Character(UUID id, String name, int strength, int agility, int intelligence, int attack, int defense, List<String> equipment) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.strength = strength; this.agility = agility; this.intelligence = intelligence;
        this.attack = attack; this.defense = defense;
        this.equipment = List.copyOf(equipment == null ? List.of() : equipment);
    }

    // -- getters
    public UUID getId(){ return id; }
    public String getName(){ return name; }
    public int getStrength(){ return strength; }
    public int getAgility(){ return agility; }
    public int getIntelligence(){ return intelligence; }
    public int getAttack(){ return attack; }
    public int getDefense(){ return defense; }
    public List<String> getEquipment(){ return Collections.unmodifiableList(equipment); }

    public int getPowerLevel(){
        return strength*2 + agility + intelligence + attack*2 + defense;
    }

    public String getDescription() {
        String items = equipment.isEmpty() ? "aucun" : String.join(", ", equipment);
        return String.format("%s [STR=%d, AGI=%d, INT=%d, ATK=%d, DEF=%d] | Equipement: %s",
                name, strength, agility, intelligence, attack, defense, items);
    }

    // -- pour "rename": créer une nouvelle instance avec même id
    public Character withName(String newName) {
        return new Character(this.id, newName, strength, agility, intelligence, attack, defense, equipment);
    }

    // Impl. décorateur minimal (le "nu" se renvoie lui-même)
    @Override public String toString(){ return getDescription() + " | Power=" + getPowerLevel(); }
    @Override public Character getInner(){ return this; }
}

