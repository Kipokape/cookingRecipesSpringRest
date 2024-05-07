package com.example.cookingrecipesspringrest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CategoryDTO(
        @Min(value = 0, message = "categoryId должен быть больше 0!")
        long categoryId,
        @NotBlank(message = "name должно быть заполнено!")
        String name,
        List<RecipeDTO> recipes) {
}
