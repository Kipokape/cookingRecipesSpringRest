package com.example.cookingrecipesrest.repository;

import com.example.cookingrecipesrest.model.RecipeIngredients;

import java.util.List;

public interface RecipeIngredientsRepository extends SimpleRepository<RecipeIngredients, Long> {

    List<RecipeIngredients> getRecipeIngredientsByRecipe(Long idRecipe);

    List<RecipeIngredients> getRecipeIngredientsByIngredient(Long idIngredient);
}
