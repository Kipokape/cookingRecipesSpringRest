package com.example.cookingrecipesrest.repository.mapper;

import com.example.cookingrecipesrest.model.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IngredientResultSetMapper {

    Ingredient map(ResultSet resultSet) throws SQLException;

    List<Ingredient> mapList(ResultSet resultSet) throws SQLException;
}
