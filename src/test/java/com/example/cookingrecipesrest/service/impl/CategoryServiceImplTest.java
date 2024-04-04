package com.example.cookingrecipesrest.service.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.db.ConnectionManagerImpl;
import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.repository.CategoryRepository;
import com.example.cookingrecipesrest.repository.impl.CategoryRepositoryImpl;
import com.example.cookingrecipesrest.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    private CategoryRepository categoryRepository;

    private ConnectionManager connectionManager;

    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        connectionManager = mock(ConnectionManagerImpl.class);
        categoryRepository = mock(CategoryRepositoryImpl.class);
        categoryService = new CategoryServiceImpl(categoryRepository, connectionManager);
    }



    @Test
    void save_CorrectCategory_ReturnCategory() {
        Category expected = new Category(1L, "Тест", null);
        when(categoryRepository.save(expected)).thenReturn(expected);

        Category actual = categoryService.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void save_NotCorrectCategory_TrowsRuntimeException() {
        Category expected = new Category(1L, null, null);
        when(categoryRepository.save(expected)).thenThrow(new RuntimeException());

        Assertions.assertThrows(RuntimeException.class, () -> categoryService.save(expected));
    }

    @Test
    void findById_CorrectCategory_ReturnCategory() {
        Category expected = new Category(1L, "Тест", null);
        when(categoryRepository.findById(1L)).thenReturn(expected);

        Category actual = categoryService.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void findById_NotCorrectId_TrowsRuntimeException() {
        when(categoryRepository.findById(12L)).thenThrow(new RuntimeException());
        Assertions.assertThrows(RuntimeException.class, () -> categoryService.findById(12L));
    }

    @Test
    void findAll_CorrectCategory_ReturnCategories() {
        List<Category> expected = new ArrayList<>();
        expected.add(new Category(1L, "Тест", null));
        expected.add(new Category(2L, "Тест1", null));
        expected.add(new Category(3L, "Тест2", null));
        when(categoryRepository.findALL()).thenReturn(expected);

        List<Category> actual = categoryService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void delete_ExistCategory_ReturnTrue() {
        Category expected = new Category(1L, "Тест", null);
        when(categoryRepository.deleteById(expected.getId())).thenReturn(true);

        assertTrue(categoryService.delete(expected));
    }

    @Test
    void delete_NotExistCategory_ReturnFalse() {
        Category expected = new Category(1L, "Тест", null);
        when(categoryRepository.deleteById(expected.getId())).thenReturn(false);

        assertFalse(categoryService.delete(expected));
    }

}