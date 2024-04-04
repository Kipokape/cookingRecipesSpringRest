package com.example.cookingrecipesrest.servlet.mapper;

import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.servlet.dto.IngredientDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class IngredientDtoMapperTest {

    @Test
    void map_Ingredient_ReturnDto() {
        Ingredient ingredient = new Ingredient(1L,"тест", null);

        IngredientDTO dto = IngredientDtoMapper.INSTANCE.map(ingredient);

        Assertions.assertEquals(ingredient.getId(), dto.ingredientId());
        Assertions.assertEquals(ingredient.getName(), dto.name());
        Assertions.assertEquals(ingredient.getRecipeIngredients(), dto.recipeIngredients());
    }

    @Test
    void map_IngredientDto_ReturnIngredient() {
        IngredientDTO dto = new IngredientDTO(1L,"тест", null);

        Ingredient ingredient = IngredientDtoMapper.INSTANCE.map(dto);

        Assertions.assertEquals(ingredient.getId(), dto.ingredientId());
        Assertions.assertEquals(ingredient.getName(), dto.name());
        Assertions.assertEquals(ingredient.getRecipeIngredients(), dto.recipeIngredients());
    }

}