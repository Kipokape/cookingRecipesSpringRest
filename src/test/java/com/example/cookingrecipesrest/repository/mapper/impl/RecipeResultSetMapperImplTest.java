package com.example.cookingrecipesrest.repository.mapper.impl;

import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.repository.mapper.CategoryResultSetMapper;
import com.example.cookingrecipesrest.repository.mapper.RecipeResultSetMapper;
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

class RecipeResultSetMapperImplTest {

    private RecipeResultSetMapper mapper;
    private ResultSet resultSet;

    private ResultSet emptyResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        mapper = new RecipeResultSetMapperImpl();
        resultSet = mock(ResultSet.class);
        emptyResultSet = mock(ResultSet.class);
        when(emptyResultSet.next()).thenReturn(false);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getLong("id_recipe")).thenReturn(1L,2L);
        when(resultSet.getLong("id_category")).thenReturn(1L,2L);
        when(resultSet.getString("name_recipe")).thenReturn("Первая","Вторая");
    }



    @Test
    void map_ExistData_ReturnRecipe() throws SQLException {
        Recipe recipe = mapper.map(resultSet);
        Assertions.assertEquals(1L, recipe.getId());
        Assertions.assertEquals(1L, recipe.getIdCategory());
        Assertions.assertEquals("Первая", recipe.getName());
    }

    @Test
    void map_NotExistData_ReturnNone() throws SQLException {
        Recipe recipe = mapper.map(emptyResultSet);
        Assertions.assertEquals(0, recipe.getId());
        Assertions.assertEquals(0, recipe.getIdCategory());
        Assertions.assertNull(recipe.getName());
    }

    @Test
    void mapList_ExistData_ReturnRecipes() throws SQLException {
        List<Recipe> expected = new ArrayList<>();
        expected.add(new Recipe(1L,1L, "Первая",null));
        expected.add(new Recipe(2L,2L, "Вторая",null));

        List<Recipe> actual = mapper.mapList(resultSet);

        Assertions.assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getId(), actual.get(i).getId());
            Assertions.assertEquals(expected.get(i).getIdCategory(), actual.get(i).getIdCategory());
            Assertions.assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }

    @Test
    void mapList_NotExistData_ReturnNone() throws SQLException {
        List<Recipe> actual = mapper.mapList(emptyResultSet);
        Assertions.assertEquals(0,actual.size());
    }
}