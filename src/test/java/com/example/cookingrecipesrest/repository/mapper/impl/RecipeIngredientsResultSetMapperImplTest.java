package com.example.cookingrecipesrest.repository.mapper.impl;

import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.repository.mapper.RecipeIngredientsResultSetMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecipeIngredientsResultSetMapperImplTest {

    private RecipeIngredientsResultSetMapper mapper;
    private ResultSet resultSet;

    private ResultSet emptyResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        mapper = new RecipeIngredientsResultSetMapperImpl();
        resultSet = mock(ResultSet.class);
        emptyResultSet = mock(ResultSet.class);
        when(emptyResultSet.next()).thenReturn(false);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getLong("id_recipe_ingredients")).thenReturn(1L,2L);
        when(resultSet.getLong("id_ingredient")).thenReturn(1L,2L);
        when(resultSet.getLong("id_recipe")).thenReturn(1L,2L);
        when(resultSet.getInt("weight")).thenReturn(100,200);
    }



    @Test
    void map_ExistData_ReturnRecipe() throws SQLException {
        RecipeIngredients recipeIngredients = mapper.map(resultSet);
        Assertions.assertEquals(1L, recipeIngredients.getId());
        Assertions.assertEquals(1L, recipeIngredients.getRecipeId());
        Assertions.assertEquals(1L, recipeIngredients.getIngredientId());
        Assertions.assertEquals(100, recipeIngredients.getWeight());
    }

    @Test
    void map_NotExistData_ReturnNone() throws SQLException {
        RecipeIngredients recipeIngredients = mapper.map(emptyResultSet);
        Assertions.assertEquals(0, recipeIngredients.getId());
        Assertions.assertEquals(0, recipeIngredients.getRecipeId());
        Assertions.assertEquals(0, recipeIngredients.getIngredientId());
        Assertions.assertEquals(0, recipeIngredients.getWeight());
    }

    @Test
    void mapList_ExistData_ReturnRecipes() throws SQLException {
        List<RecipeIngredients> expected = new ArrayList<>();
        expected.add(new RecipeIngredients(1L,1L, 1L,100));
        expected.add(new RecipeIngredients(2L,2L, 2L,200));

        List<RecipeIngredients> actual = mapper.mapList(resultSet);

        Assertions.assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getId(), actual.get(i).getId());
            Assertions.assertEquals(expected.get(i).getRecipeId(), actual.get(i).getRecipeId());
            Assertions.assertEquals(expected.get(i).getIngredientId(), actual.get(i).getIngredientId());
            Assertions.assertEquals(expected.get(i).getWeight(), actual.get(i).getWeight());
        }
    }

    @Test
    void mapList_NotExistData_ReturnNone() throws SQLException {
        List<RecipeIngredients> actual = mapper.mapList(emptyResultSet);
        Assertions.assertEquals(0,actual.size());
    }
}