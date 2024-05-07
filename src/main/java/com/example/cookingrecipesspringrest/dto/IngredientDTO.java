package com.example.cookingrecipesspringrest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record IngredientDTO(
        @Min(value = 0, message = "ingredientId должен быть больше 0!")
        long ingredientId,
        @NotBlank(message = "name должно быть заполнено!")
        String name,
        List<RecipeDTO> recipes) {
}
