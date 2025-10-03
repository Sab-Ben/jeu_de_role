package com.rpg.dao;

import java.util.*;
import java.util.stream.Collectors;

import com.rpg.core.Character;

/**
 * Implémentation simple en mémoire (HashMap) pour stocker des personnages.
 * Pour la démo : pas de persistance disque/BD, mais l'interface permet d'évoluer plus tard.
 */
/** DAO en mémoire (clé = UUID). */
public class CharacterDAO implements DAO<Character> {
    private final Map<UUID, Character> store = new HashMap<>();

    @Override public void save(Character item){ store.put(item.getId(), item); }
    @Override public Optional<Character> findById(UUID id){ return Optional.ofNullable(store.get(id)); }
    public Optional<Character> findByName(String name){
        return store.values().stream().filter(c -> c.getName().equalsIgnoreCase(name)).findFirst();
    }
    @Override public List<Character> findAll(){ return new ArrayList<>(store.values()); }

    @Override public void update(Character item){ store.put(item.getId(), item); }
    @Override public void remove(UUID id){ store.remove(id); }

    @Override
    public List<Character> findAllSortedByName() {
        return store.values().stream()
                .sorted(Comparator.comparing(Character::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    @Override
    public List<Character> findAllSortedByPower() {
        return store.values().stream()
                .sorted(Comparator.comparingInt(Character::getPowerLevel).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Character> searchByName(String q) {
        String needle = q == null ? "" : q.toLowerCase();
        return store.values().stream()
                .filter(c -> c.getName().toLowerCase().contains(needle))
                .collect(Collectors.toList());
    }
}

