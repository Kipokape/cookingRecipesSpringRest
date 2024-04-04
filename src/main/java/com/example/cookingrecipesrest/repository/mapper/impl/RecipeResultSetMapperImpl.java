package com.example.cookingrecipesrest.repository.mapper.impl;

import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.repository.mapper.RecipeResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeResultSetMapperImpl implements RecipeResultSetMapper {

    @Override
    public Recipe map(ResultSet resultSet) throws SQLException {

        if(resultSet.isBeforeFirst()) resultSet.next();
        Recipe recipe = new Recipe();
        recipe.setId(resultSet.getLong("id_recipe"));
        recipe.setIdCategory(resultSet.getLong("id_category"));
        recipe.setName(resultSet.getString("name_recipe"));
        return recipe;
    }

    @Override
    public List<Recipe> mapList(ResultSet resultSet) throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        while (resultSet.next()){
            recipes.add(map(resultSet));
        }
        return recipes;
    }
}
