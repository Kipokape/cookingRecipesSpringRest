package com.example.cookingrecipesrest.servlet.dto;

import com.example.cookingrecipesrest.model.Ingredient;

import java.util.List;

public record RecipeDTO(long recipeId, long idCategory, String name, List<Ingredient> ingredients) {

}
