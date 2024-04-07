package com.example.cookingrecipesrest.repository.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.repository.IngredientRepository;
import com.example.cookingrecipesrest.repository.RecipeIngredientsRepository;
import com.example.cookingrecipesrest.repository.RecipeRepository;
import com.example.cookingrecipesrest.repository.mapper.RecipeResultSetMapper;
import com.example.cookingrecipesrest.repository.mapper.impl.RecipeResultSetMapperImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RecipeRepositoryImpl implements RecipeRepository {

    private final ConnectionManager connectionManager;

    private final RecipeResultSetMapper resultSetMapper = new RecipeResultSetMapperImpl();

    private final RecipeIngredientsRepository recipeIngredientsRepository;

    private final IngredientRepository ingredientRepository;

    public RecipeRepositoryImpl(ConnectionManager connectionManager, IngredientRepository ingredientRepository) {
        this.connectionManager = connectionManager;
        createRecipeTableIfNotExists();
        recipeIngredientsRepository = new RecipeIngredientsRepositoryImpl(connectionManager);
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Recipe findById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLRecipe.GET.query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Recipe recipe = resultSetMapper.map(resultSet);
            recipe.setIngredients(ingredientRepository.getIngredientsByRecipe(recipe.getId()));
            return recipe;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно найти рецепт по ID.", e);
        }
    }

    @Override
    public List<Recipe> getRecipesByCategory(Long idCategory) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLRecipe.GET_BY_CATEGORY.query)) {
            preparedStatement.setLong(1, idCategory);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetMapper.mapList(resultSet);
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно найти все рецепты по категории.", e);
        }
    }

    @Override
    public List<Recipe> getRecipesByIngredient(Long idIngredient) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLRecipe.GET_BY_INGREDIENT.query)) {
            preparedStatement.setLong(1, idIngredient);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetMapper.mapList(resultSet);
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно найти все рецепты по ингредиенту.", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLRecipe.DELETE.query)) {
            preparedStatement.setLong(1, id);
            List<RecipeIngredients> recipeIngredients = recipeIngredientsRepository.getRecipeIngredientsByRecipe(id);
            for (RecipeIngredients recipeIngredient : recipeIngredients) {
                recipeIngredientsRepository.deleteById(recipeIngredient.getId());
            }
            int res = preparedStatement.executeUpdate();
            return res != 0;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно удалить рецепт по ID.", e);
        }
    }

    @Override
    public List<Recipe> findALL() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLRecipe.GET_ALL.query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Recipe> recipes = resultSetMapper.mapList(resultSet);
            for (Recipe recipe : recipes) {
                recipe.setIngredients(ingredientRepository.getIngredientsByRecipe(recipe.getId()));
            }
            return recipes;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно найти все рецепты.", e);
        }
    }

    @Override
    public Recipe save(Recipe recipe) {
        String query;
        if (recipe.getId() > 0) {
            query = SQLRecipe.UPDATE.query;
        } else query = SQLRecipe.INSERT.query;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, recipe.getName());
            preparedStatement.setLong(2, recipe.getIdCategory());
            if (query.contains("UPDATE")) {
                preparedStatement.setLong(3, recipe.getId());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            recipe.setId(resultSetMapper.map(resultSet).getId());
            if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
                for (Ingredient ingredient : recipe.getIngredients()) {
                    recipeIngredientsRepository.save(new RecipeIngredients(recipe.getId(), ingredient.getId(), 0));
                }
            }
            return recipe;
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Невозможно сохранить/обновить рецепт.", e);
        }
    }


    private void createRecipeTableIfNotExists() {
        try (Connection conn = this.connectionManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQLRecipe.CREATE_TABLE.query)) {
            preparedStatement.execute();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void truncateTable() {
        try (Connection conn = this.connectionManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQLRecipe.TRUNCATE_TABLE.query)) {
            preparedStatement.execute();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    enum SQLRecipe {
        GET("SELECT * FROM recipe WHERE id_recipe = (?) ORDER BY id_recipe;"),
        GET_BY_CATEGORY("SELECT * FROM recipe WHERE id_category = (?) ORDER BY id_recipe;"),

        GET_BY_INGREDIENT("SELECT r.id_recipe, r.id_category, r.name_recipe, ri.id_ingredient " +
                "FROM recipe r " +
                "LEFT JOIN recipe_ingredients ri ON r.id_recipe = ri.id_recipe " +
                "WHERE ri.id_ingredient = (?)" +
                "ORDER BY id_recipe;"),
        GET_ALL("SELECT * FROM recipe ORDER BY id_recipe;"),
        INSERT("INSERT INTO recipe(name_recipe, id_category) VALUES ((?), (?)) RETURNING *;"),
        DELETE("DELETE FROM recipe WHERE id_recipe = (?);"),
        UPDATE("UPDATE recipe SET name_recipe = (?), id_category = (?) WHERE id_recipe = (?) RETURNING *;"),
        CREATE_TABLE("CREATE TABLE IF NOT EXISTS " +
                "recipe(id_recipe BIGSERIAL PRIMARY KEY, " +
                "name_recipe VARCHAR(100) NOT NULL, " +
                "id_category BIGINT NOT NULL, " +
                "FOREIGN KEY (id_category) REFERENCES category ON DELETE SET NULL ON UPDATE CASCADE);"),
        TRUNCATE_TABLE("TRUNCATE TABLE recipe RESTART IDENTITY CASCADE;");

        final String query;

        SQLRecipe(String query) {
            this.query = query;
        }
    }
}
