package com.example.cookingrecipesrest.service.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.db.ConnectionManagerImpl;
import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.repository.RecipeRepository;
import com.example.cookingrecipesrest.repository.impl.CategoryRepositoryImpl;
import com.example.cookingrecipesrest.repository.impl.RecipeRepositoryImpl;
import com.example.cookingrecipesrest.service.RecipeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecipeServiceImplTest {

    private RecipeRepository recipeRepository;

    private ConnectionManager connectionManager;

    private RecipeService recipeService;

    @BeforeEach
    public void setUp() {
        connectionManager = mock(ConnectionManagerImpl.class);
        recipeRepository = mock(RecipeRepositoryImpl.class);
        recipeService = new RecipeServiceImpl(recipeRepository, connectionManager);
    }



    @Test
    void save_CorrectRecipe_ReturnRecipe() {
        Recipe expected = new Recipe(1L,1L, "Тест", null);
        when(recipeRepository.save(expected)).thenReturn(expected);

        Recipe actual = recipeService.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void save_NotCorrectRecipe_TrowsRuntimeException() {
        Recipe expected = new Recipe(1L,66L, null, null);
        when(recipeRepository.save(expected)).thenThrow(new RuntimeException());

        Assertions.assertThrows(RuntimeException.class, () -> recipeService.save(expected));
    }

    @Test
    void findById_CorrectRecipe_ReturnRecipe() {
        Recipe expected = new Recipe(1L,1L, "Тест", null);
        when(recipeRepository.findById(1L)).thenReturn(expected);

        Recipe actual = recipeService.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void findById_NotCorrectId_TrowsRuntimeException() {
        when(recipeRepository.findById(12L)).thenThrow(new RuntimeException());
        Assertions.assertThrows(RuntimeException.class, () -> recipeService.findById(12L));
    }

    @Test
    void findAll_CorrectRecipe_ReturnRecipes() {
        List<Recipe> expected = new ArrayList<>();
        expected.add(new Recipe(1L,1L, "Тест", null));
        expected.add(new Recipe(2L,2L, "Тест1", null));
        expected.add(new Recipe(3L,3L, "Тест2", null));
        when(recipeRepository.findALL()).thenReturn(expected);

        List<Recipe> actual = recipeService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void delete_ExistRecipe_ReturnTrue() {
        Recipe expected = new Recipe(1L,1L, "Тест", null);
        when(recipeRepository.deleteById(expected.getId())).thenReturn(true);

        assertTrue(recipeService.delete(expected));
    }

    @Test
    void delete_NotExistRecipe_ReturnFalse() {
        Recipe expected = new Recipe(1L,1L, "Тест", null);
        when(recipeRepository.deleteById(expected.getId())).thenReturn(false);

        assertFalse(recipeService.delete(expected));
    }

}