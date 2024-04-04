package com.example.cookingrecipesrest.repository.mapper.impl;

import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.repository.mapper.CategoryResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryResultSetMapperImpl implements CategoryResultSetMapper {


    @Override
    public Category map(ResultSet resultSet) throws SQLException{

        if(resultSet.isBeforeFirst()) resultSet.next();
        Category category = new Category();
        category.setId(resultSet.getLong("id_category"));
        category.setName(resultSet.getString("name_category"));
        return category;
    }

    @Override
    public List<Category> mapList(ResultSet resultSet) throws SQLException{
        List<Category> categories = new ArrayList<>();
        while (resultSet.next()){
            categories.add(map(resultSet));
        }
        return categories;
    }
}
