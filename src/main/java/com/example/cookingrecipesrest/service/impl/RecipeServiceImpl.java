package com.example.cookingrecipesrest.service.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.db.ConnectionManagerImpl;
import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.repository.RecipeRepository;
import com.example.cookingrecipesrest.repository.impl.IngredientRepositoryImpl;
import com.example.cookingrecipesrest.repository.impl.RecipeRepositoryImpl;
import com.example.cookingrecipesrest.service.RecipeService;

import java.util.List;

public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final ConnectionManager connectionManager;


    public RecipeServiceImpl() {
        connectionManager = new ConnectionManagerImpl();
        recipeRepository = new RecipeRepositoryImpl(
                connectionManager,
                new IngredientRepositoryImpl(connectionManager, null));
    }

    public RecipeServiceImpl(RecipeRepository recipeRepository, ConnectionManager connectionManager) {
        this.recipeRepository = recipeRepository;
        this.connectionManager = connectionManager;
    }


    @Override
    public Recipe save(Recipe recipe) {
        try {
            return recipeRepository.save(recipe);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка сохранения рецепта. " + e.getMessage());
        }
    }

    @Override
    public Recipe findById(Long id) {
        try {
            return recipeRepository.findById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка чтения данных. " + e.getMessage());
        }
    }

    @Override
    public List<Recipe> findAll() {
        try {
            return recipeRepository.findALL();
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка чтения данных. " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Recipe recipe) {
        try {
            return recipeRepository.deleteById(recipe.getId());
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка удаления. " + e.getMessage());
        }
    }
}
