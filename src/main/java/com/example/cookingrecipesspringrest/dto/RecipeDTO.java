package com.example.cookingrecipesspringrest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RecipeDTO(
        @Min(value = 0, message = "recipeId должен быть больше 0!")
        long recipeId,
        @NotNull(message = "category должно быть заполнено!")
        CategoryDTO category,
        @NotBlank(message = "name должно быть заполнено!")
        String name,
        List<IngredientDTO> ingredients) {

}
