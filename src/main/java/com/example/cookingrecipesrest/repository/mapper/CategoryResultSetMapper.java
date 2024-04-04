package com.example.cookingrecipesrest.repository.mapper;

import com.example.cookingrecipesrest.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface CategoryResultSetMapper {
    Category map(ResultSet resultSet) throws SQLException;

    List<Category> mapList(ResultSet resultSet) throws SQLException;
}
