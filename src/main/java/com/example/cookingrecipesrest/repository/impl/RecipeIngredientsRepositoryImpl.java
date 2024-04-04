package com.example.cookingrecipesrest.repository.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.repository.RecipeIngredientsRepository;
import com.example.cookingrecipesrest.repository.mapper.RecipeIngredientsResultSetMapper;
import com.example.cookingrecipesrest.repository.mapper.impl.RecipeIngredientsResultSetMapperImpl;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class RecipeIngredientsRepositoryImpl implements RecipeIngredientsRepository {

    private final ConnectionManager connectionManager;

    private final RecipeIngredientsResultSetMapper resultSetMapper = new RecipeIngredientsResultSetMapperImpl();

    public RecipeIngredientsRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        createRecipeAndIngredientTableIfNotExists();
        createRecipeIngredientsTableIfNotExists();
    }

    @Override
    public RecipeIngredients findById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLRecipeIngredients.GET.query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetMapper.map(resultSet);
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно найти ингредиент рецепта по ID.", e);
        }
    }

    @Override
    public List<RecipeIngredients> getRecipeIngredientsByRecipe(Long idRecipe) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLRecipeIngredients.GET_BY_RECIPE.query)) {
            preparedStatement.setLong(1, idRecipe);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetMapper.mapList(resultSet);
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно найти все ингредиенты рецепта по ID.", e);
        }
    }

    @Override
    public List<RecipeIngredients> getRecipeIngredientsByIngredient(Long idIngredient) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLRecipeIngredients.GET_BY_INGREDIENT.query)) {
            preparedStatement.setLong(1, idIngredient);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetMapper.mapList(resultSet);
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно найти все рецепты ингредиента по ID.", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLRecipeIngredients.DELETE.query)) {
            preparedStatement.setLong(1, id);
            int res = preparedStatement.executeUpdate();
            return res != 0;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно удалить ингредиент рецепта по ID.", e);
        }
    }

    @Override
    public List<RecipeIngredients> findALL() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLRecipeIngredients.GET_ALL.query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetMapper.mapList(resultSet);
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно найти все ингредиенты рецептов.", e);
        }
    }

    @Override
    public RecipeIngredients save(RecipeIngredients recipeIngredients) {
        String query;
        if (recipeIngredients.getId() > 0) {
            query = SQLRecipeIngredients.UPDATE.query;
        } else query = SQLRecipeIngredients.INSERT.query;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, recipeIngredients.getIngredientId());
            preparedStatement.setLong(2, recipeIngredients.getRecipeId());
            preparedStatement.setInt(3, recipeIngredients.getWeight());
            if(query.contains("UPDATE")){
                preparedStatement.setLong(4, recipeIngredients.getId());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            recipeIngredients.setId(resultSetMapper.map(resultSet).getId());
            return recipeIngredients;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно сохранить/обновить ингредиент рецепта.", e);
        }
    }

    private void createRecipeIngredientsTableIfNotExists() {
        try (Connection conn = this.connectionManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQLRecipeIngredients.CREATE_TABLE.query)) {
            preparedStatement.execute();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createRecipeAndIngredientTableIfNotExists() {
        try (Connection conn = this.connectionManager.getConnection();
             Statement statement = conn.createStatement()) {
            statement.addBatch(SQLRecipeIngredients.CREATE_RECIPE_TABLE.query);
            statement.addBatch(SQLRecipeIngredients.CREATE_INGREDIENT_TABLE.query);
            statement.executeBatch();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void truncateTable() {
        try (Connection conn = this.connectionManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQLRecipeIngredients.TRUNCATE_TABLE.query)) {
            preparedStatement.execute();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    enum SQLRecipeIngredients {
        GET("SELECT * FROM recipe_ingredients WHERE id_recipe_ingredients = (?);"),
        GET_BY_RECIPE("SELECT * FROM recipe_ingredients WHERE id_recipe = (?);"),
        GET_BY_INGREDIENT("SELECT * FROM recipe_ingredients WHERE id_ingredient = (?);"),
        GET_ALL("SELECT * FROM recipe_ingredients;"),
        INSERT("INSERT INTO recipe_ingredients(id_ingredient, id_recipe, weight) VALUES ((?),(?),(?)) RETURNING *;"),
        DELETE("DELETE FROM recipe_ingredients WHERE id_recipe_ingredients = (?);"),
        UPDATE("UPDATE recipe_ingredients " +
                "SET id_ingredient = (?), id_recipe = (?), weight = (?) " +
                "WHERE id_recipe_ingredients = (?) RETURNING *;"),
        CREATE_TABLE("CREATE TABLE IF NOT EXISTS recipe_ingredients" +
                "(id_recipe_ingredients BIGSERIAL PRIMARY KEY, " +
                "id_ingredient BIGINT NOT NULL, " +
                "id_recipe BIGINT NOT NULL, " +
                "weight INTEGER NOT NULL," +
                "FOREIGN KEY (id_ingredient) REFERENCES ingredient ON DELETE SET NULL ON UPDATE CASCADE," +
                " FOREIGN KEY (id_recipe) REFERENCES recipe ON DELETE SET NULL ON UPDATE CASCADE);"),
        CREATE_RECIPE_TABLE("CREATE TABLE IF NOT EXISTS " +
                "recipe(id_recipe BIGSERIAL PRIMARY KEY, " +
                "name_recipe VARCHAR(100) NOT NULL, " +
                "id_category BIGINT NOT NULL, " +
                "FOREIGN KEY (id_category) REFERENCES category ON DELETE SET NULL ON UPDATE CASCADE);"),
        CREATE_INGREDIENT_TABLE("CREATE TABLE IF NOT EXISTS ingredient" +
                "(id_ingredient BIGSERIAL PRIMARY KEY," +
                " name_ingredient VARCHAR(100) NOT NULL);"),

        TRUNCATE_TABLE("TRUNCATE TABLE recipe_ingredients RESTART IDENTITY CASCADE;");

        final String query;

        SQLRecipeIngredients(String query) {
            this.query = query;
        }
    }
}
