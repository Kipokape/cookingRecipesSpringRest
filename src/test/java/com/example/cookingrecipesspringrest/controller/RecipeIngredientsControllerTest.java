package com.example.cookingrecipesspringrest.controller;

import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.dto.*;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.exception.ServiceException;
import com.example.cookingrecipesspringrest.service.RecipeIngredientsService;
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
class RecipeIngredientsControllerTest {

    @Mock
    private RecipeIngredientsService recipeIngredientsService;

    @InjectMocks
    private RecipeIngredientsController recipeIngredientsController;

    private final CategoryDTO testCategoryDto = new CategoryDTO(1L, "категория 1", new ArrayList<>());

    private final RecipeDTO testRecipeDto = new RecipeDTO(1L, testCategoryDto, "рецепт 1", new ArrayList<>());

    private final IngredientDTO testIngredientDto = new IngredientDTO(1L, "ингредиент 1", new ArrayList<>());


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllRecipeIngredients_ReturnListOfRecipeIngredients() {
        List<RecipeIngredientsDTO> expected = new ArrayList<>();
        expected.add(new RecipeIngredientsDTO(1L, testRecipeDto, testIngredientDto, 100));
        expected.add(new RecipeIngredientsDTO(2L, testRecipeDto, testIngredientDto, 200));
        expected.add(new RecipeIngredientsDTO(3L, testRecipeDto, testIngredientDto, 300));

        when(recipeIngredientsService.findAll()).thenReturn(expected);
        List<RecipeIngredientsDTO> actual = recipeIngredientsController.findAllRecipeIngredients();
        assertEquals(expected, actual);
    }

    @Test
    void findRecipeIngredientsById_CorrectId_ReturnRecipeIngredients() {
        RecipeIngredientsDTO expected = new RecipeIngredientsDTO(1L, testRecipeDto, testIngredientDto, 100);
        when(recipeIngredientsService.findById(1L)).thenReturn(expected);
        RecipeIngredientsDTO actual = recipeIngredientsController.findRecipeIngredientsById(1L);
        assertEquals(expected, actual);
    }

    @Test
    void findRecipeIngredientsById_NoneExistId_ThrowsRepositoryException() {
        when(recipeIngredientsService.findById(100L)).thenThrow(new RepositoryException("тест"));
        assertThrows(RepositoryException.class, () -> recipeIngredientsController.findRecipeIngredientsById(100L));
    }

    @Test
    void findRecipeIngredientsById_IncorrectId_ThrowsServiceException() {
        assertThrows(ServiceException.class, () -> recipeIngredientsController.findRecipeIngredientsById(-1L));
        verify(recipeIngredientsService, never()).findById(-1L);
    }

    @Test
    void saveNewRecipeIngredients_CorrectRecipeIngredients_ReturnRecipeIngredients() {
        RecipeIngredientsDTO expected = new RecipeIngredientsDTO(0L, testRecipeDto, testIngredientDto, 100);
        when(recipeIngredientsService.save(expected)).thenReturn(expected);
        RecipeIngredientsDTO actual = recipeIngredientsController.saveNewRecipeIngredients(expected);
        assertEquals(expected, actual);
    }

    @Test
    void saveNewRecipeIngredients_IncorrectRecipeIngredients_ThrowsServiceException() {
        RecipeIngredientsDTO expected = new RecipeIngredientsDTO(1L, testRecipeDto, testIngredientDto, 100);
        Assertions.assertThrows(ServiceException.class, () -> recipeIngredientsController.saveNewRecipeIngredients(expected));
        verify(recipeIngredientsService, never()).save(expected);
    }

    @Test
    void updateExistRecipeIngredients_CorrectRecipeIngredients_ReturnRecipeIngredients() {
        RecipeIngredientsDTO expected = new RecipeIngredientsDTO(1L, testRecipeDto, testIngredientDto, 100);
        when(recipeIngredientsService.save(expected)).thenReturn(expected);
        RecipeIngredientsDTO actual = recipeIngredientsController.updateExistRecipeIngredients(expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateExistRecipeIngredients_IncorrectRecipeIngredients_ThrowsServiceException() {
        RecipeIngredientsDTO expected = new RecipeIngredientsDTO(0L, testRecipeDto, testIngredientDto, 0);
        Assertions.assertThrows(ServiceException.class, () -> recipeIngredientsController.updateExistRecipeIngredients(expected));
        verify(recipeIngredientsService, never()).save(expected);
    }

    @Test
    void deleteRecipeIngredientsById_CorrectRecipeIngredientsId_ReturnTrue() {
        when(recipeIngredientsService.deleteById(1L)).thenReturn(true);
        assertEquals(new ResponseDTO("Ингредиент рецепта успешно удален!"), recipeIngredientsController.deleteRecipeIngredientsById(1L));
    }

    @Test
    void deleteRecipeIngredientsById_IncorrectRecipeIngredientsId_ThrowsServiceException() {
        Assertions.assertThrows(ServiceException.class, () -> recipeIngredientsController.deleteRecipeIngredientsById(-1L));
        verify(recipeIngredientsService, never()).deleteById(-1L);
    }

}