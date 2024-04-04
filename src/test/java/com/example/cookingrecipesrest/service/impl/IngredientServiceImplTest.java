package com.example.cookingrecipesrest.service.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.db.ConnectionManagerImpl;
import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.repository.IngredientRepository;
import com.example.cookingrecipesrest.repository.impl.CategoryRepositoryImpl;
import com.example.cookingrecipesrest.repository.impl.IngredientRepositoryImpl;
import com.example.cookingrecipesrest.service.IngredientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IngredientServiceImplTest {
    private IngredientRepository ingredientRepository;

    private ConnectionManager connectionManager;

    private IngredientService ingredientService;

    @BeforeEach
    public void setUp() {
        connectionManager = mock(ConnectionManagerImpl.class);
        ingredientRepository = mock(IngredientRepositoryImpl.class);
        ingredientService = new IngredientServiceImpl(ingredientRepository, connectionManager);
    }



    @Test
    void save_CorrectIngredient_ReturnIngredient() {
        Ingredient expected = new Ingredient(1L, "Тест", null);
        when(ingredientRepository.save(expected)).thenReturn(expected);

        Ingredient actual = ingredientService.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void save_NotCorrectIngredient_TrowsRuntimeException() {
        Ingredient expected = new Ingredient(1L, null, null);
        when(ingredientRepository.save(expected)).thenThrow(new RuntimeException());

        Assertions.assertThrows(RuntimeException.class, () -> ingredientService.save(expected));
    }

    @Test
    void findById_CorrectIngredient_ReturnIngredient() {
        Ingredient expected = new Ingredient(1L, "Тест", null);
        when(ingredientRepository.findById(1L)).thenReturn(expected);

        Ingredient actual = ingredientService.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void findById_NotCorrectId_TrowsRuntimeException() {
        when(ingredientRepository.findById(12L)).thenThrow(new RuntimeException());
        Assertions.assertThrows(RuntimeException.class, () -> ingredientService.findById(12L));
    }

    @Test
    void findAll_CorrectIngredient_ReturnCategories() {
        List<Ingredient> expected = new ArrayList<>();
        expected.add(new Ingredient(1L, "Тест", null));
        expected.add(new Ingredient(2L, "Тест1", null));
        expected.add(new Ingredient(3L, "Тест2", null));
        when(ingredientRepository.findALL()).thenReturn(expected);

        List<Ingredient> actual = ingredientService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void delete_ExistIngredient_ReturnTrue() {
        Ingredient expected = new Ingredient(1L, "Тест", null);
        when(ingredientRepository.deleteById(expected.getId())).thenReturn(true);

        assertTrue(ingredientService.delete(expected));
    }

    @Test
    void delete_NotExistIngredient_ReturnFalse() {
        Ingredient expected = new Ingredient(1L, "Тест", null);
        when(ingredientRepository.deleteById(expected.getId())).thenReturn(false);

        assertFalse(ingredientService.delete(expected));
    }
}