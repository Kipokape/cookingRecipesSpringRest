package com.example.cookingrecipesrest.repository.mapper.impl;

import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.repository.mapper.IngredientResultSetMapper;
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

class IngredientResultSetMapperImplTest {

    private IngredientResultSetMapper mapper;
    private ResultSet resultSet;

    private ResultSet emptyResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        mapper = new IngredientResultSetMapperImpl();
        resultSet = mock(ResultSet.class);
        emptyResultSet = mock(ResultSet.class);
        when(emptyResultSet.next()).thenReturn(false);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getLong("id_ingredient")).thenReturn(1L,2L);
        when(resultSet.getString("name_ingredient")).thenReturn("Первая","Вторая");
    }



    @Test
    void map_ExistData_ReturnIngredient() throws SQLException {
        Ingredient ingredient = mapper.map(resultSet);
        Assertions.assertEquals(1L, ingredient.getId());
        Assertions.assertEquals("Первая", ingredient.getName());
    }

    @Test
    void map_NotExistData_ReturnNone() throws SQLException {
        Ingredient ingredient = mapper.map(emptyResultSet);
        Assertions.assertEquals(0, ingredient.getId());
        Assertions.assertNull(ingredient.getName());
    }

    @Test
    void mapList_ExistData_ReturnIngredients() throws SQLException {
        List<Ingredient> expected = new ArrayList<>();
        expected.add(new Ingredient(1L,"Первая",null));
        expected.add(new Ingredient(2L,"Вторая",null));

        List<Ingredient> actual = mapper.mapList(resultSet);

        Assertions.assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getId(), actual.get(i).getId());
            Assertions.assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }

    @Test
    void mapList_NotExistData_ReturnNone() throws SQLException {
        List<Ingredient> actual = mapper.mapList(emptyResultSet);
        Assertions.assertEquals(0,actual.size());
    }
}