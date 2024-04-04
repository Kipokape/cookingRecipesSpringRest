package com.example.cookingrecipesrest.service.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.db.ConnectionManagerImpl;
import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.repository.RecipeIngredientsRepository;
import com.example.cookingrecipesrest.repository.impl.RecipeIngredientsRepositoryImpl;
import com.example.cookingrecipesrest.repository.impl.RecipeRepositoryImpl;
import com.example.cookingrecipesrest.service.RecipeIngredientsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecipeIngredientsServiceImplTest {

    private RecipeIngredientsRepository recipeIngredientsRepository;

    private ConnectionManager connectionManager;

    private RecipeIngredientsService recipeIngredientsService;

    @BeforeEach
    public void setUp() {
        connectionManager = mock(ConnectionManagerImpl.class);
        recipeIngredientsRepository = mock(RecipeIngredientsRepositoryImpl.class);
        recipeIngredientsService = new RecipeIngredientsServiceImpl(recipeIngredientsRepository, connectionManager);
    }



    @Test
    void save_CorrectRecipeIngredients_ReturnRecipeIngredients() {
        RecipeIngredients expected = new RecipeIngredients(1L,1L, 1L, 333);
        when(recipeIngredientsRepository.save(expected)).thenReturn(expected);

        RecipeIngredients actual = recipeIngredientsService.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void save_NotCorrectRecipeIngredients_TrowsRuntimeException() {
        RecipeIngredients expected = new RecipeIngredients(1L,1L, 1L, 333);
        when(recipeIngredientsRepository.save(expected)).thenThrow(new RuntimeException());

        Assertions.assertThrows(RuntimeException.class, () -> recipeIngredientsService.save(expected));
    }

    @Test
    void findById_CorrectRecipeIngredients_ReturnRecipeIngredients() {
        RecipeIngredients expected = new RecipeIngredients(1L,1L, 1L, 333);
        when(recipeIngredientsRepository.findById(1L)).thenReturn(expected);

        RecipeIngredients actual = recipeIngredientsService.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void findById_NotCorrectId_TrowsRuntimeException() {
        when(recipeIngredientsRepository.findById(12L)).thenThrow(new RuntimeException());
        Assertions.assertThrows(RuntimeException.class, () -> recipeIngredientsService.findById(12L));
    }

    @Test
    void findAll_CorrectRecipeIngredients_ReturnRecipeIngredients() {
        List<RecipeIngredients> expected = new ArrayList<>();
        expected.add(new RecipeIngredients(1L,1L, 1L, 333));
        expected.add(new RecipeIngredients(2L,2L, 2L, 555));
        expected.add(new RecipeIngredients(3L,3L, 3L, 222));
        when(recipeIngredientsRepository.findALL()).thenReturn(expected);

        List<RecipeIngredients> actual = recipeIngredientsService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void delete_ExistRecipeIngredients_ReturnTrue() {
        RecipeIngredients expected = new RecipeIngredients(1L,1L, 1L, 333);
        when(recipeIngredientsRepository.deleteById(expected.getId())).thenReturn(true);

        assertTrue(recipeIngredientsService.delete(expected));
    }

    @Test
    void delete_NotExistRecipeIngredients_ReturnFalse() {
        RecipeIngredients expected = new RecipeIngredients(1L,1L, 1L, 455);
        when(recipeIngredientsRepository.deleteById(expected.getId())).thenReturn(false);

        assertFalse(recipeIngredientsService.delete(expected));
    }

}