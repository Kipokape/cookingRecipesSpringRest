package com.example.cookingrecipesspringrest.service.impl;

import com.example.cookingrecipesspringrest.ValidationUtils;
import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.dto.IngredientDTO;
import com.example.cookingrecipesspringrest.dto.mapper.IngredientDtoMapper;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.model.Ingredient;
import com.example.cookingrecipesspringrest.repository.IngredientRepository;
import com.example.cookingrecipesspringrest.repository.RecipeIngredientsRepository;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringJUnitConfig
@SpringJUnitWebConfig
@ContextConfiguration(classes = SpringConfig.class)
class IngredientServiceImplTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeIngredientsRepository recipeIngredientsRepository;

    @Mock
    private IngredientDtoMapper ingredientDtoMapper;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private IngredientServiceImpl ingredientService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_CorrectIngredient_ReturnIngredient() {
        IngredientDTO expected = new IngredientDTO(1L, "ингредиент 1", new ArrayList<>());

        Ingredient expectedEntity = new Ingredient(1L, "ингредиент 1", new ArrayList<>());

        when(ingredientDtoMapper.toEntity(expected)).thenReturn(expectedEntity);
        when(ingredientRepository.save(expectedEntity)).thenReturn(expectedEntity);
        when(ingredientDtoMapper.toDto(expectedEntity)).thenReturn(expected);

        IngredientDTO actual = ingredientService.save(expected);

        Assertions.assertEquals(expected, actual);
        verify(ingredientDtoMapper, times(1)).toEntity(expected);
        verify(ingredientRepository, times(1)).save(expectedEntity);
        verify(ingredientDtoMapper, times(1)).toDto(expectedEntity);
    }

    @Test
    void save_IncorrectIngredient_ThrowsServiceException() {
        IngredientDTO expected = new IngredientDTO(1L, "", new ArrayList<>());

        doThrow(new RepositoryException("Неверные данные")).when(validationUtils).validation(expected);

        Assertions.assertThrows(RepositoryException.class, () -> ingredientService.save(expected));
        verify(ingredientRepository, never()).save(ingredientDtoMapper.toEntity(expected));
    }


    @Test
    void findById_CorrectIngredient_ReturnIngredient() {
        IngredientDTO expected = new IngredientDTO(1L, "ингредиент 1", new ArrayList<>());
        Optional<Ingredient> expectedEntity = Optional.of(new Ingredient(1L, "ингредиент 1", new ArrayList<>()));

        when(ingredientRepository.findById(1L)).thenReturn(expectedEntity);
        when(ingredientDtoMapper.toDto(expectedEntity.get())).thenReturn(expected);

        IngredientDTO actual = ingredientService.findById(1L);

        Assertions.assertEquals(expected, actual);
        verify(ingredientRepository, times(1)).findById(1L);
        verify(ingredientDtoMapper, times(1)).toDto(expectedEntity.get());
    }

    @Test
    void findById_NotCorrectId_TrowsRepositoryException() {
        Optional<Ingredient> expectedEntity = Optional.empty();
        when(ingredientRepository.findById(1000L)).thenReturn(expectedEntity);

        Assertions.assertThrows(RepositoryException.class, () -> ingredientService.findById(1000L));
        verify(ingredientRepository, times(1)).findById(1000L);
    }

    @Test
    void findAll_CorrectIngredients_ReturnIngredients() {
        List<Ingredient> expectedEntityList = new ArrayList<>();
        expectedEntityList.add(new Ingredient(1L, "ингредиент 1", new ArrayList<>()));
        expectedEntityList.add(new Ingredient(1L, "ингредиент 2", new ArrayList<>()));
        List<IngredientDTO> expectedDtoList = new ArrayList<>();
        expectedDtoList.add(new IngredientDTO(1L, "ингредиент 1", new ArrayList<>()));
        expectedDtoList.add(new IngredientDTO(1L, "ингредиент 2", new ArrayList<>()));

        when(ingredientRepository.findAll()).thenReturn(expectedEntityList);
        when(ingredientDtoMapper.toListDto(expectedEntityList)).thenReturn(expectedDtoList);

        Assertions.assertEquals(expectedDtoList, ingredientService.findAll());
        verify(ingredientRepository, times(1)).findAll();
        verify(ingredientDtoMapper, times(1)).toListDto(expectedEntityList);
    }

    @Test
    void delete_ExistIngredient_ReturnTrue() {
        Optional<Ingredient> expectedEntity = Optional.of(new Ingredient(1L, "ингредиент 1", new ArrayList<>()));

        when(ingredientRepository.findById(1L)).thenReturn(expectedEntity);

        Assertions.assertTrue(ingredientService.deleteById(1L));
        verify(ingredientRepository, times(1)).findById(1L);
        verify(ingredientRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NotExistRecipe_ReturnFalse() {
        when(ingredientRepository.findById(100L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RepositoryException.class, () -> ingredientService.deleteById(100L));
        verify(ingredientRepository, times(1)).findById(100L);
        verify(ingredientRepository, never()).deleteById(anyLong());
    }
}