package com.example.cookingrecipesrest.servlet.dto;

public record RecipeIngredientsDTO(long recipeIngredientsId, long recipeId, long ingredientId, int weight) {
}
