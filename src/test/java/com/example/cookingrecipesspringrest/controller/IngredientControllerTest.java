package com.example.cookingrecipesspringrest.controller;

import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.dto.IngredientDTO;
import com.example.cookingrecipesspringrest.dto.ResponseDTO;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.exception.ServiceException;
import com.example.cookingrecipesspringrest.service.IngredientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
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
@TestPropertySource("classpath:test-db.properties")
class IngredientControllerTest {
    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllIngredients_ReturnListOfIngredients() {
        List<IngredientDTO> expected = new ArrayList<>();
        expected.add(new IngredientDTO(1L, "ингредиент 1", new ArrayList<>()));
        expected.add(new IngredientDTO(2L, "ингредиент 2", new ArrayList<>()));
        expected.add(new IngredientDTO(3L, "ингредиент 3", new ArrayList<>()));

        when(ingredientService.findAll()).thenReturn(expected);
        List<IngredientDTO> actual = ingredientController.findAllIngredients();
        assertEquals(expected, actual);
    }

    @Test
    void findIngredientById_CorrectId_ReturnIngredient() {
        IngredientDTO expected = new IngredientDTO(1L, "ингредиент 1", new ArrayList<>());
        when(ingredientService.findById(1L)).thenReturn(expected);
        IngredientDTO actual = ingredientController.findIngredientById(1L);
        assertEquals(expected, actual);
    }

    @Test
    void findIngredientById_NoneExistId_ThrowsRepositoryException() {
        when(ingredientService.findById(100L)).thenThrow(new RepositoryException("тест"));
        assertThrows(RepositoryException.class, () -> ingredientController.findIngredientById(100L));
    }

    @Test
    void findIngredientById_IncorrectId_ThrowsServiceException() {
        assertThrows(ServiceException.class, () -> ingredientController.findIngredientById(-1L));
        verify(ingredientService, never()).findById(-1L);
    }

    @Test
    void saveNewIngredient_CorrectIngredient_ReturnIngredient() {
        IngredientDTO expected = new IngredientDTO(0, "ингредиент 1", new ArrayList<>());
        when(ingredientService.save(expected)).thenReturn(expected);
        IngredientDTO actual = ingredientController.saveNewIngredient(expected);
        assertEquals(expected, actual);
    }

    @Test
    void saveNewIngredient_IncorrectIngredient_ThrowsServiceException() {
        IngredientDTO expected = new IngredientDTO(1, "ингредиент 1", new ArrayList<>());
        Assertions.assertThrows(ServiceException.class, () -> ingredientController.saveNewIngredient(expected));
        verify(ingredientService, never()).save(expected);
    }

    @Test
    void updateExistIngredient_CorrectIngredient_ReturnIngredient() {
        IngredientDTO expected = new IngredientDTO(1, "ингредиент 1", new ArrayList<>());
        when(ingredientService.save(expected)).thenReturn(expected);
        IngredientDTO actual = ingredientController.updateExistIngredient(expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateExistIngredient_IncorrectIngredient_ThrowsServiceException() {
        IngredientDTO expected = new IngredientDTO(0, "ингредиент 1", new ArrayList<>());
        Assertions.assertThrows(ServiceException.class, () -> ingredientController.updateExistIngredient(expected));
        verify(ingredientService, never()).save(expected);
    }

    @Test
    void deleteIngredientById_CorrectIngredientId_ReturnTrue() {
        when(ingredientService.deleteById(1L)).thenReturn(true);
        assertEquals(new ResponseDTO("Ингредиент успешно удален!"), ingredientController.deleteIngredientById(1L));
    }

    @Test
    void deleteIngredientById_IncorrectIngredientId_ThrowsServiceException() {
        Assertions.assertThrows(ServiceException.class, () -> ingredientController.deleteIngredientById(-1L));
        verify(ingredientService, never()).deleteById(-1L);
    }

}