package com.example.cookingrecipesrest.service.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.db.ConnectionManagerImpl;
import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.repository.RecipeIngredientsRepository;
import com.example.cookingrecipesrest.repository.impl.RecipeIngredientsRepositoryImpl;
import com.example.cookingrecipesrest.service.RecipeIngredientsService;

import java.util.List;

public class RecipeIngredientsServiceImpl implements RecipeIngredientsService {


    private final RecipeIngredientsRepository recipeIngredientsRepository;

    private final ConnectionManager connectionManager;


    public RecipeIngredientsServiceImpl() {
        connectionManager = new ConnectionManagerImpl();
        recipeIngredientsRepository = new RecipeIngredientsRepositoryImpl(connectionManager);
    }

    public RecipeIngredientsServiceImpl(RecipeIngredientsRepository recipeIngredientsRepository, ConnectionManager connectionManager) {
        this.recipeIngredientsRepository = recipeIngredientsRepository;
        this.connectionManager = connectionManager;
    }

    @Override
    public RecipeIngredients save(RecipeIngredients recipeIngredients) {
        try {
            return recipeIngredientsRepository.save(recipeIngredients);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка сохранения ингредиента блюда. " + e.getMessage());
        }
    }

    @Override
    public RecipeIngredients findById(Long id) {
        try {
            return recipeIngredientsRepository.findById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка чтения данных. " + e.getMessage());
        }
    }

    @Override
    public List<RecipeIngredients> findAll() {
        try {
            return recipeIngredientsRepository.findALL();
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка чтения данных. " + e.getMessage());
        }
    }

    @Override
    public boolean delete(RecipeIngredients recipeIngredients) {
        try {
            return recipeIngredientsRepository.deleteById(recipeIngredients.getId());
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка удаления. " + e.getMessage());
        }
    }
}
