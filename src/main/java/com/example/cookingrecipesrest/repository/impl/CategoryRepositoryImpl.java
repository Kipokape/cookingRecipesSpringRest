package com.example.cookingrecipesrest.repository.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.repository.CategoryRepository;
import com.example.cookingrecipesrest.repository.RecipeRepository;
import com.example.cookingrecipesrest.repository.mapper.CategoryResultSetMapper;
import com.example.cookingrecipesrest.repository.mapper.impl.CategoryResultSetMapperImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {


    private final ConnectionManager connectionManager;

    private final CategoryResultSetMapper resultSetMapper = new CategoryResultSetMapperImpl();

    private final RecipeRepository recipeRepository;

    public CategoryRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        createCategoryTableIfNotExists();
        recipeRepository = new RecipeRepositoryImpl(connectionManager, null);
    }

    @Override
    public Category findById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLCategory.GET.query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Category category = resultSetMapper.map(resultSet);
            category.setRecipes(recipeRepository.getRecipesByCategory(category.getId()));
            return category;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно найти категорию по ID.", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLCategory.DELETE.query)) {
            preparedStatement.setLong(1, id);
            List<Recipe> recipes = recipeRepository.getRecipesByCategory(id);
            for (Recipe recipe : recipes) {
                recipeRepository.deleteById(recipe.getId());
            }
            int res = preparedStatement.executeUpdate();
            return res != 0;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно удалить категорию по ID.", e);
        }
    }

    @Override
    public List<Category> findALL() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLCategory.GET_ALL.query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Category> categories= resultSetMapper.mapList(resultSet);
            for (Category category : categories) {
                category.setRecipes(recipeRepository.getRecipesByCategory(category.getId()));
            }
            return categories;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно найти все категории.", e);
        }
    }

    @Override
    public Category save(Category category) {
        String query;
        if (category.getId() > 0) {
            query = SQLCategory.UPDATE.query;
        } else query = SQLCategory.INSERT.query;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, category.getName());
            if(query.contains("UPDATE")){
                preparedStatement.setLong(2, category.getId());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            category.setId(resultSetMapper.map(resultSet).getId());
            if(category.getRecipes() != null && !category.getRecipes().isEmpty()){
                for (Recipe recipe: category.getRecipes()) {
                    recipe.setIdCategory(category.getId());
                }
                category.getRecipes().replaceAll(recipeRepository::save);
            }
            return category;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно сохранить/обновить категорию.", e);
        }
    }

    private void createCategoryTableIfNotExists() {
        try (Connection conn = this.connectionManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQLCategory.CREATE_TABLE.query)) {
            preparedStatement.execute();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void truncateTable() {
        try (Connection conn = this.connectionManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQLCategory.TRUNCATE_TABLE.query)) {
            preparedStatement.execute();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    enum SQLCategory {
        GET("SELECT * FROM category WHERE id_category = (?) ORDER BY id_category;"),
        GET_ALL("SELECT * FROM category ORDER BY id_category;"),
        INSERT("INSERT INTO category(name_category) VALUES ((?)) RETURNING *;"),
        DELETE("DELETE FROM category WHERE id_category = (?);"),
        UPDATE("UPDATE category SET name_category = (?) WHERE id_category = (?) RETURNING *;"),
        CREATE_TABLE("CREATE TABLE IF NOT EXISTS " +
                "category(id_category BIGSERIAL PRIMARY KEY," +
                "name_category VARCHAR(100) NOT NULL);"),
        TRUNCATE_TABLE("TRUNCATE TABLE category RESTART IDENTITY CASCADE;");

        final String query;

        SQLCategory(String query) {
            this.query = query;
        }
    }


}
