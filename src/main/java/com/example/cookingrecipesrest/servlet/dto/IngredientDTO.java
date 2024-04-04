package com.example.cookingrecipesrest.servlet.dto;

import com.example.cookingrecipesrest.model.RecipeIngredients;

import java.util.List;

public record IngredientDTO(long ingredientId, String name, List<RecipeIngredients> recipeIngredients) {
}
