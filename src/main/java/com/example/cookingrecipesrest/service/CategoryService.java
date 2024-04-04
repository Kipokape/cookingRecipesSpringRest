package com.example.cookingrecipesrest.service;

import com.example.cookingrecipesrest.model.Category;

import java.util.List;

public interface CategoryService {

    Category save(Category category);

    Category findById(Long id);

    List<Category> findAll();

    boolean delete(Category category);
}
