package com.example.cookingrecipesspringrest.service;

import java.util.List;

public interface BaseService<T> {

    T save(T entityDTO);

    T findById(Long id);

    List<T> findAll();

    boolean deleteById(Long id);
}
