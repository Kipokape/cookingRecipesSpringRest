package com.example.cookingrecipesspringrest.controller;

import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.dto.CategoryDTO;
import com.example.cookingrecipesspringrest.dto.RecipeDTO;
import com.example.cookingrecipesspringrest.dto.ResponseDTO;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.exception.ServiceException;
import com.example.cookingrecipesspringrest.service.RecipeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringJUnitConfig
@SpringJUnitWebConfig
@ContextConfiguration(classes = SpringConfig.class)
class RecipeControllerTest {


    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    private final CategoryDTO testCategoryDto = new CategoryDTO(1L, "категория 1", new ArrayList<>());

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllRecipes_ReturnListOfRecipes() {
        List<RecipeDTO> expected = new ArrayList<>();
        expected.add(new RecipeDTO(1L, testCategoryDto, "тест", new ArrayList<>()));
        expected.add(new RecipeDTO(2L, testCategoryDto, "тест 2", new ArrayList<>()));
        expected.add(new RecipeDTO(3L, testCategoryDto, "тест 3", new ArrayList<>()));

        when(recipeService.findAll()).thenReturn(expected);
        List<RecipeDTO> actual = recipeController.findAllRecipes();
        assertEquals(expected, actual);
    }

    @Test
    void findRecipeById_CorrectId_ReturnRecipe() {
        RecipeDTO expected = new RecipeDTO(1L, testCategoryDto, "тест", new ArrayList<>());
        when(recipeService.findById(1L)).thenReturn(expected);
        RecipeDTO actual = recipeController.findRecipeById(1L);
        assertEquals(expected, actual);
    }

    @Test
    void findRecipeById_NoneExistId_ThrowsRepositoryException() {
        when(recipeService.findById(100L)).thenThrow(new RepositoryException("тест"));
        assertThrows(RepositoryException.class, () -> recipeController.findRecipeById(100L));
    }

    @Test
    void findRecipeById_IncorrectId_ThrowsServiceException() {
        assertThrows(ServiceException.class, () -> recipeController.findRecipeById(-1L));
        verify(recipeService, never()).findById(-1L);
    }

    @Test
    void saveNewRecipe_CorrectRecipe_ReturnRecipe() {
        RecipeDTO expected = new RecipeDTO(0, testCategoryDto, "тест", new ArrayList<>());
        when(recipeService.save(expected)).thenReturn(expected);
        RecipeDTO actual = recipeController.saveNewRecipe(expected);
        assertEquals(expected, actual);
    }

    @Test
    void saveNewRecipe_IncorrectRecipe_ThrowsServiceException() {
        RecipeDTO expected = new RecipeDTO(1, testCategoryDto, "тест", new ArrayList<>());
        Assertions.assertThrows(ServiceException.class, () -> recipeController.saveNewRecipe(expected));
        verify(recipeService, never()).save(expected);
    }

    @Test
    void updateExistRecipe_CorrectRecipe_ReturnRecipe() {
        RecipeDTO expected = new RecipeDTO(1, testCategoryDto, "тест", new ArrayList<>());
        when(recipeService.save(expected)).thenReturn(expected);
        RecipeDTO actual = recipeController.updateExistRecipe(expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateExistRecipe_IncorrectRecipe_ThrowsServiceException() {
        RecipeDTO expected = new RecipeDTO(0, testCategoryDto, "тест", new ArrayList<>());
        Assertions.assertThrows(ServiceException.class, () -> recipeController.updateExistRecipe(expected));
        verify(recipeService, never()).save(expected);
    }

    @Test
    void deleteRecipeById_CorrectRecipeId_ReturnTrue() {
        when(recipeService.deleteById(1L)).thenReturn(true);
        assertEquals(new ResponseDTO("Рецепт успешно удален!"), recipeController.deleteRecipeById(1L));
    }

    @Test
    void deleteCategoryById_IncorrectCategoryId_ThrowsServiceException() {
        Assertions.assertThrows(ServiceException.class, () -> recipeController.deleteRecipeById(-1L));
        verify(recipeService, never()).deleteById(-1L);
    }

}