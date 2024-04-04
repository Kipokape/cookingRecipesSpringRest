package com.example.cookingrecipesrest.repository.mapper.impl;

import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.repository.mapper.IngredientResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientResultSetMapperImpl implements IngredientResultSetMapper {
    @Override
    public Ingredient map(ResultSet resultSet) throws SQLException {
        if(resultSet.isBeforeFirst()) resultSet.next();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(resultSet.getLong("id_ingredient"));
        ingredient.setName(resultSet.getString("name_ingredient"));
        return ingredient;
    }

    @Override
    public List<Ingredient> mapList(ResultSet resultSet) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        while (resultSet.next()){
            ingredients.add(map(resultSet));
        }
        return ingredients;
    }
}
