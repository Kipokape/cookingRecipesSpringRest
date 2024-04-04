package com.example.cookingrecipesrest.repository.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.repository.IngredientRepository;
import com.example.cookingrecipesrest.repository.RecipeIngredientsRepository;
import com.example.cookingrecipesrest.repository.mapper.IngredientResultSetMapper;
import com.example.cookingrecipesrest.repository.mapper.impl.IngredientResultSetMapperImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class IngredientRepositoryImpl implements IngredientRepository {

    private final ConnectionManager connectionManager;

    private final IngredientResultSetMapper resultSetMapper = new IngredientResultSetMapperImpl();

    private final RecipeIngredientsRepository recipeIngredientsRepository;

    public IngredientRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        createIngredientTableIfNotExists();
        recipeIngredientsRepository = new RecipeIngredientsRepositoryImpl(connectionManager);
    }

    @Override
    public Ingredient findById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLIngredient.GET.query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Ingredient ingredient = resultSetMapper.map(resultSet);
            ingredient.setRecipeIngredients(recipeIngredientsRepository.getRecipeIngredientsByIngredient(ingredient.getId()));
            return ingredient;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно найти ингредиент по ID.", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLIngredient.DELETE.query)) {
            preparedStatement.setLong(1, id);
            List<RecipeIngredients> recipeIngredients = recipeIngredientsRepository.getRecipeIngredientsByIngredient(id);
            for (RecipeIngredients recipeIngredient : recipeIngredients) {
                recipeIngredientsRepository.deleteById(recipeIngredient.getId());
            }
            int res = preparedStatement.executeUpdate();
            return res != 0;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно удалить ингредиент по ID.", e);
        }
    }

    @Override
    public List<Ingredient> findALL() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLIngredient.GET_ALL.query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Ingredient> ingredients = resultSetMapper.mapList(resultSet);
            for (Ingredient ingredient : ingredients) {
                ingredient.setRecipeIngredients(recipeIngredientsRepository.getRecipeIngredientsByRecipe(ingredient.getId()));
            }
            return ingredients;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно найти все ингредиенты.", e);
        }
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        String query;
        if (ingredient.getId() > 0) {
            query = SQLIngredient.UPDATE.query;
        } else query = SQLIngredient.INSERT.query;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, ingredient.getName());
            if (query.contains("UPDATE")) {
                preparedStatement.setLong(2, ingredient.getId());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if (ingredient.getRecipeIngredients() != null && !ingredient.getRecipeIngredients().isEmpty()) {
                ingredient.getRecipeIngredients().replaceAll(recipeIngredientsRepository::save);
            }
            ingredient.setId(resultSetMapper.map(resultSet).getId());
            return ingredient;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно сохранить/обновить ингредиент.", e);
        }
    }

    private void createIngredientTableIfNotExists() {
        try (Connection conn = this.connectionManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQLIngredient.CREATE_TABLE.query)) {
            preparedStatement.execute();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void truncateTable() {
        try (Connection conn = this.connectionManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQLIngredient.TRUNCATE_TABLE.query)) {
            preparedStatement.execute();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    enum SQLIngredient {
        GET("SELECT * FROM ingredient WHERE id_ingredient = (?);"),
        GET_ALL("SELECT * FROM ingredient;"),
        INSERT("INSERT INTO ingredient(name_ingredient) VALUES ((?)) RETURNING *;"),
        DELETE("DELETE FROM ingredient WHERE id_ingredient = (?);"),
        UPDATE("UPDATE ingredient SET name_ingredient = (?) WHERE id_ingredient = (?) RETURNING *;"),
        CREATE_TABLE("CREATE TABLE IF NOT EXISTS ingredient" +
                "(id_ingredient BIGSERIAL PRIMARY KEY," +
                " name_ingredient VARCHAR(100) NOT NULL);"),
        TRUNCATE_TABLE("TRUNCATE TABLE ingredient RESTART IDENTITY CASCADE;");

        final String query;

        SQLIngredient(String query) {
            this.query = query;
        }
    }
}
