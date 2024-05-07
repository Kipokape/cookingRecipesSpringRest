package com.example.cookingrecipesspringrest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecipeIngredientsDTO(
        @Min(value = 0, message = "recipeIngredientsId должен быть больше 0!")
        long recipeIngredientsId,
        @NotNull(message = "recipe должно быть заполнено!")
        RecipeDTO recipe,
        @NotNull(message = "ingredient должно быть заполнено!")
        IngredientDTO ingredient,
        @Positive(message = "weight должен быть больше 0!")
        @NotNull
        int weight) {
}
