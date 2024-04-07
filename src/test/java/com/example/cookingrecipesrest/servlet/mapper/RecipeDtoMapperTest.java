package com.example.cookingrecipesrest.servlet.mapper;

import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.servlet.dto.RecipeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RecipeDtoMapperTest {

    @Test
    void map_Recipe_ReturnDto() {
        Recipe recipe = new Recipe(1L,1L,"тест", null);

        RecipeDTO dto = RecipeDtoMapper.INSTANCE.map(recipe);

        Assertions.assertEquals(recipe.getId(), dto.recipeId());
        Assertions.assertEquals(recipe.getName(), dto.name());
        Assertions.assertEquals(recipe.getIdCategory(), dto.idCategory());
        Assertions.assertEquals(recipe.getIngredients(), dto.ingredients());
    }

    @Test
    void map_RecipeDto_ReturnRecipe() {
        RecipeDTO dto = new RecipeDTO(1L,1L,"тест", null);

        Recipe recipe = RecipeDtoMapper.INSTANCE.map(dto);

        Assertions.assertEquals(recipe.getId(), dto.recipeId());
        Assertions.assertEquals(recipe.getName(), dto.name());
        Assertions.assertEquals(recipe.getIdCategory(), dto.idCategory());
        Assertions.assertEquals(recipe.getIngredients(), dto.ingredients());
    }
}