package com.izinvest_common.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface DAO générique minimale.
 * - save(T)
 * - findByName(String)
 * - findAll()
 */
public interface DAO<T> {
    void save(T item);
    Optional<T> findByName(String name);
    List<T> findAll();
}

