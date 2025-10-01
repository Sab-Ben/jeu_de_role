package com.rpg.battle;

import com.rpg.core.Character;

/**
 * Représente un "participant" en combat :
 * - référence au Character immuable
 * - points de vie courants (HP) calculés à partir des stats au départ
 * - états temporaires (ex : garde/defenseBoost)
 */
public class BattleUnit {
    private final Character character;
    private int hp;
    private int defenseBoost = 0;

    public BattleUnit(Character character) {
        this.character = character;
        // Base HP simple : 50 + STR*2 + DEF
        this.hp = 50 + character.getStrength()*2 + character.getDefense();
    }

    public Character getCharacter() { return character; }
    public int getHp() { return hp; }
    public void damage(int amount) { this.hp = Math.max(0, hp - Math.max(0, amount)); }
    public boolean isAlive() { return hp > 0; }

    public void addDefenseBoost(int amount) { defenseBoost += amount; }
    public int consumeDefenseBoost() {
        int tmp = defenseBoost;
        defenseBoost = 0;
        return tmp;
    }
}

