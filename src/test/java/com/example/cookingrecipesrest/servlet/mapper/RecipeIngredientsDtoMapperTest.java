package com.example.cookingrecipesrest.servlet.mapper;

import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.servlet.dto.IngredientDTO;
import com.example.cookingrecipesrest.servlet.dto.RecipeIngredientsDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeIngredientsDtoMapperTest {


    @Test
    void map_RecipeIngredients_ReturnDto() {
        RecipeIngredients recipeIngredients = new RecipeIngredients(1L,1L,1L, 333);

        RecipeIngredientsDTO dto = RecipeIngredientsDtoMapper.INSTANCE.map(recipeIngredients);

        Assertions.assertEquals(recipeIngredients.getId(), dto.recipeIngredientsId());
        Assertions.assertEquals(recipeIngredients.getRecipeId(), dto.recipeId());
        Assertions.assertEquals(recipeIngredients.getIngredientId(), dto.ingredientId());
        Assertions.assertEquals(recipeIngredients.getWeight(), dto.weight());
    }

    @Test
    void map_RecipeIngredientsDto_ReturnRecipeIngredients() {
        RecipeIngredientsDTO dto = new RecipeIngredientsDTO(1L,1L,1L, 333);

        RecipeIngredients recipeIngredients = RecipeIngredientsDtoMapper.INSTANCE.map(dto);

        Assertions.assertEquals(recipeIngredients.getId(), dto.recipeIngredientsId());
        Assertions.assertEquals(recipeIngredients.getRecipeId(), dto.recipeId());
        Assertions.assertEquals(recipeIngredients.getIngredientId(), dto.ingredientId());
        Assertions.assertEquals(recipeIngredients.getWeight(), dto.weight());
    }



}