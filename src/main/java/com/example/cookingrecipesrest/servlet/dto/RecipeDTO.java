package com.example.cookingrecipesrest.servlet.dto;

import com.example.cookingrecipesrest.model.RecipeIngredients;

import java.util.List;

public record RecipeDTO(long recipeId, long idCategory, String name, List<RecipeIngredients> recipeIngredients) {

}
