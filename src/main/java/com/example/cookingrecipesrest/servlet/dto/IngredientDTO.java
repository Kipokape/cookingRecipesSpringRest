package com.example.cookingrecipesrest.servlet.dto;

import com.example.cookingrecipesrest.model.Recipe;

import java.util.List;

public record IngredientDTO(long ingredientId, String name, List<Recipe> recipes) {
}
