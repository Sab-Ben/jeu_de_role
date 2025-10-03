package com.rpg.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface DAO générique minimale.
 * - save(T)
 * - findByName(String)
 * - findAll()
 */
public interface DAO<T> {
    void save(T item);                 // create
    Optional<T> findById(UUID id);
    List<T> findAll();
    void update(T item);               // update
    void remove(UUID id);              // delete

    // utilitaires
    List<T> findAllSortedByName();
    List<T> findAllSortedByPower();
    List<T> searchByName(String q);
}

