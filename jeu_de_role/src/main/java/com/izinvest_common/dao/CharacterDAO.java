package com.izinvest_common.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.izinvest_common.core.Character;

/**
 * Implémentation simple en mémoire (HashMap) pour stocker des personnages.
 * Pour la démo : pas de persistance disque/BD, mais l'interface permet d'évoluer plus tard.
 */
public class CharacterDAO implements DAO<Character> {

    private final Map<String, Character> store = new HashMap<>();

    @Override
    public void save(Character item) {
        if (item == null) return;
        store.put(item.getName(), item);
    }

    @Override
    public Optional<Character> findByName(String name) {
        return Optional.ofNullable(store.get(name));
    }

    @Override
    public List<Character> findAll() {
        return new ArrayList<>(store.values());
    }
}

