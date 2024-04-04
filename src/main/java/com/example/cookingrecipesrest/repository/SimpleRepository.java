package com.example.cookingrecipesrest.repository;

import java.util.List;

public interface SimpleRepository<T, K> {
    T findById(K id);

    boolean deleteById(K id);

    List<T> findALL();

    T save(T t);

    void truncateTable();
}
