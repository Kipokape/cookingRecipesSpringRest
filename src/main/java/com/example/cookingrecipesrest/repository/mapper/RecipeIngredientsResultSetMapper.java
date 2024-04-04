package com.example.cookingrecipesrest.repository.mapper;

import com.example.cookingrecipesrest.model.RecipeIngredients;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface RecipeIngredientsResultSetMapper {

    RecipeIngredients map(ResultSet resultSet) throws SQLException;

    List<RecipeIngredients> mapList(ResultSet resultSet) throws SQLException;
}
