package com.example.cookingrecipesrest.repository;

import com.example.cookingrecipesrest.model.Ingredient;


import java.util.List;

public interface IngredientRepository extends SimpleRepository<Ingredient, Long>{

    List<Ingredient> getIngredientsByRecipe(Long idRecipe);
}
