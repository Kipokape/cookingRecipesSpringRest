package com.example.cookingrecipesrest.service;

import com.example.cookingrecipesrest.model.Recipe;

import java.util.List;

public interface RecipeService {

    Recipe save(Recipe recipe);

    Recipe findById(Long id);

    List<Recipe> findAll();

    boolean delete(Recipe recipe);
}
