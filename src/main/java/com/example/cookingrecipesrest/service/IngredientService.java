package com.example.cookingrecipesrest.service;

import com.example.cookingrecipesrest.model.Ingredient;

import java.util.List;

public interface IngredientService {

    Ingredient save(Ingredient ingredient);

    Ingredient findById(Long id);

    List<Ingredient> findAll();

    boolean delete(Ingredient ingredient);
}
