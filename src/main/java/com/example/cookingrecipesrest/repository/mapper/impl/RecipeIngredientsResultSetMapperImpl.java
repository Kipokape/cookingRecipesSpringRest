package com.example.cookingrecipesrest.repository.mapper.impl;

import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.repository.mapper.RecipeIngredientsResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientsResultSetMapperImpl implements RecipeIngredientsResultSetMapper {
    @Override
    public RecipeIngredients map(ResultSet resultSet) throws SQLException {
        if(resultSet.isBeforeFirst()) resultSet.next();
        RecipeIngredients recipeIngredients = new RecipeIngredients();
        recipeIngredients.setId(resultSet.getLong("id_recipe_ingredients"));
        recipeIngredients.setIngredientId(resultSet.getLong("id_ingredient"));
        recipeIngredients.setRecipeId(resultSet.getLong("id_recipe"));
        recipeIngredients.setWeight(resultSet.getInt("weight"));
        return recipeIngredients;
    }

    @Override
    public List<RecipeIngredients> mapList(ResultSet resultSet) throws SQLException {
        List<RecipeIngredients> recipeIngredients = new ArrayList<>();
        while (resultSet.next()){
            recipeIngredients.add(map(resultSet));
        }
        return recipeIngredients;
    }
}
