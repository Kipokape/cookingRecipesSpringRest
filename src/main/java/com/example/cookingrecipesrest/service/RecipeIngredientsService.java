package com.example.cookingrecipesrest.service;

import com.example.cookingrecipesrest.model.RecipeIngredients;

import java.util.List;

public interface RecipeIngredientsService {

    RecipeIngredients save(RecipeIngredients recipeIngredients);

    RecipeIngredients findById(Long id);

    List<RecipeIngredients> findAll();

    boolean delete(RecipeIngredients recipeIngredients);
}
