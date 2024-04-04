package com.example.cookingrecipesrest.repository.mapper.impl;

import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.repository.mapper.CategoryResultSetMapper;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CategoryResultSetMapperImplTest {

    private CategoryResultSetMapper mapper;
    private ResultSet resultSet;

    private ResultSet emptyResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        mapper = new CategoryResultSetMapperImpl();
        resultSet = mock(ResultSet.class);
        emptyResultSet = mock(ResultSet.class);
        when(emptyResultSet.next()).thenReturn(false);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getLong("id_category")).thenReturn(1L,2L);
        when(resultSet.getString("name_category")).thenReturn("Первая","Вторая");
    }

    @Test
    void map_ExistData_ReturnCategory() throws SQLException {
        Category category = mapper.map(resultSet);
        Assertions.assertEquals(1L, category.getId());
        Assertions.assertEquals("Первая", category.getName());
    }

    @Test
    void map_NotExistData_ReturnNone() throws SQLException {
        Category category = mapper.map(emptyResultSet);
        Assertions.assertEquals(0, category.getId());
        Assertions.assertNull(category.getName());
    }

    @Test
    void mapList_ExistData_ReturnCategories() throws SQLException {
        List<Category> expected = new ArrayList<>();
        expected.add(new Category(1L, "Первая",null));
        expected.add(new Category(2L, "Вторая",null));

        List<Category> actual = mapper.mapList(resultSet);

        Assertions.assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getId(), actual.get(i).getId());
            Assertions.assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }

    @Test
    void mapList_NotExistData_ReturnNone() throws SQLException {
        List<Category> actual = mapper.mapList(emptyResultSet);
        Assertions.assertEquals(0,actual.size());
    }


}