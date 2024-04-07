package com.example.cookingrecipesrest.service.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.db.ConnectionManagerImpl;
import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.repository.IngredientRepository;
import com.example.cookingrecipesrest.repository.impl.IngredientRepositoryImpl;
import com.example.cookingrecipesrest.repository.impl.RecipeRepositoryImpl;
import com.example.cookingrecipesrest.service.IngredientService;

import java.util.List;

public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    private final ConnectionManager connectionManager;





    public IngredientServiceImpl() {
        connectionManager = new ConnectionManagerImpl();
        ingredientRepository = new IngredientRepositoryImpl(
                connectionManager,
                new RecipeRepositoryImpl(connectionManager, null));
    }

    public IngredientServiceImpl(IngredientRepository ingredientRepository, ConnectionManager connectionManager) {
        this.ingredientRepository = ingredientRepository;
        this.connectionManager = connectionManager;
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        try {
            return ingredientRepository.save(ingredient);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка сохранения ингредиента. " + e.getMessage());
        }
    }

    @Override
    public Ingredient findById(Long id) {
        try {
            return ingredientRepository.findById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка чтения данных. " + e.getMessage());
        }
    }

    @Override
    public List<Ingredient> findAll() {
        try {
            return ingredientRepository.findALL();
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка чтения данных. " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Ingredient ingredient) {
        try {
            return ingredientRepository.deleteById(ingredient.getId());
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка удаления. " + e.getMessage());
        }
    }
}
