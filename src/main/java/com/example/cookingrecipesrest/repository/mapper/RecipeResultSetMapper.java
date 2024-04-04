package com.example.cookingrecipesrest.repository.mapper;

import com.example.cookingrecipesrest.model.Recipe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface RecipeResultSetMapper {

    Recipe map(ResultSet resultSet) throws SQLException;

    List<Recipe> mapList(ResultSet resultSet) throws SQLException;
}
