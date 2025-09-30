package com.rpg.composite;

import com.rpg.core.Character;
import java.util.Collections;
import java.util.List;

/**
 * Feuille : encapsule un Character existant.
 */
public class CharacterLeaf implements GroupComponent {
    private final Character character;

    public CharacterLeaf(Character character) {
        this.character = character;
    }

    @Override public String getName() { return character.getName(); }
    @Override public int getPowerLevel() { return character.getPowerLevel(); }
    @Override public List<Character> flatten() { return Collections.singletonList(character); }

    public Character getCharacter() { return character; }
}

