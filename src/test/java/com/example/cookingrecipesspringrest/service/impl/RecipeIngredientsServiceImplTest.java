package com.example.cookingrecipesspringrest.service.impl;

import com.example.cookingrecipesspringrest.ValidationUtils;
import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.dto.CategoryDTO;
import com.example.cookingrecipesspringrest.dto.IngredientDTO;
import com.example.cookingrecipesspringrest.dto.RecipeDTO;
import com.example.cookingrecipesspringrest.dto.RecipeIngredientsDTO;
import com.example.cookingrecipesspringrest.dto.mapper.RecipeIngredientsDtoMapper;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.model.Category;
import com.example.cookingrecipesspringrest.model.Ingredient;
import com.example.cookingrecipesspringrest.model.Recipe;
import com.example.cookingrecipesspringrest.model.RecipeIngredients;
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
class RecipeIngredientsServiceImplTest {

    @Mock
    private RecipeIngredientsRepository recipeIngredientsRepository;

    @Mock
    private RecipeIngredientsDtoMapper recipeIngredientsDtoMapper;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private RecipeIngredientsServiceImpl recipeIngredientsService;

    private final CategoryDTO testCategoryDto = new CategoryDTO(1L, "категория 1", new ArrayList<>());

    private final Category testCategory = new Category(1L, "категория 1", new ArrayList<>());

    private final RecipeDTO testRecipeDto = new RecipeDTO(1L, testCategoryDto, "рецепт 1", new ArrayList<>());

    private final Recipe testRecipe = new Recipe(1L, testCategory, "рецепт 1", new ArrayList<>());

    private final IngredientDTO testIngredientDto = new IngredientDTO(1L, "ингредиент 1", new ArrayList<>());

    private final Ingredient testIngredient = new Ingredient(1L, "ингредиент 1", new ArrayList<>());

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_CorrectRecipeIngredients_ReturnRecipeIngredients() {
        RecipeIngredientsDTO expected = new RecipeIngredientsDTO(1L, testRecipeDto, testIngredientDto, 100);

        RecipeIngredients expectedEntity = new RecipeIngredients(1L, testRecipe, testIngredient, 100);

        when(recipeIngredientsDtoMapper.toEntity(expected)).thenReturn(expectedEntity);
        when(recipeIngredientsRepository.save(expectedEntity)).thenReturn(expectedEntity);
        when(recipeIngredientsDtoMapper.toDto(expectedEntity)).thenReturn(expected);

        RecipeIngredientsDTO actual = recipeIngredientsService.save(expected);

        Assertions.assertEquals(expected, actual);
        verify(recipeIngredientsDtoMapper, times(1)).toEntity(expected);
        verify(recipeIngredientsRepository, times(1)).save(expectedEntity);
        verify(recipeIngredientsDtoMapper, times(1)).toDto(expectedEntity);
    }

    @Test
    void save_IncorrectRecipeIngredients_ThrowsServiceException() {
        RecipeIngredientsDTO expected = new RecipeIngredientsDTO(1L, testRecipeDto, testIngredientDto, 0);

        doThrow(new RepositoryException("Неверные данные")).when(validationUtils).validation(expected);

        Assertions.assertThrows(RepositoryException.class, () -> recipeIngredientsService.save(expected));
        verify(recipeIngredientsRepository, never()).save(recipeIngredientsDtoMapper.toEntity(expected));
    }


    @Test
    void findById_CorrectRecipeIngredients_ReturnRecipeIngredients() {
        RecipeIngredientsDTO expected = new RecipeIngredientsDTO(1L, testRecipeDto, testIngredientDto, 100);
        Optional<RecipeIngredients> expectedEntity = Optional.of(new RecipeIngredients(1L, testRecipe, testIngredient, 100));

        when(recipeIngredientsRepository.findById(1L)).thenReturn(expectedEntity);
        when(recipeIngredientsDtoMapper.toDto(expectedEntity.get())).thenReturn(expected);

        RecipeIngredientsDTO actual = recipeIngredientsService.findById(1L);

        Assertions.assertEquals(expected, actual);
        verify(recipeIngredientsRepository, times(1)).findById(1L);
        verify(recipeIngredientsDtoMapper, times(1)).toDto(expectedEntity.get());
    }

    @Test
    void findById_NotCorrectId_TrowsRepositoryException() {
        Optional<RecipeIngredients> expectedEntity = Optional.empty();
        when(recipeIngredientsRepository.findById(1000L)).thenReturn(expectedEntity);

        Assertions.assertThrows(RepositoryException.class, () -> recipeIngredientsService.findById(1000L));
        verify(recipeIngredientsRepository, times(1)).findById(1000L);
    }

    @Test
    void findAll_CorrectRecipeIngredients_ReturnRecipeIngredients() {
        List<RecipeIngredients> expectedEntityList = new ArrayList<>();
        expectedEntityList.add(new RecipeIngredients(1L, testRecipe, testIngredient, 100));
        expectedEntityList.add(new RecipeIngredients(2L, testRecipe, testIngredient, 200));
        List<RecipeIngredientsDTO> expectedDtoList = new ArrayList<>();
        expectedDtoList.add(new RecipeIngredientsDTO(1L, testRecipeDto, testIngredientDto, 100));
        expectedDtoList.add(new RecipeIngredientsDTO(2L, testRecipeDto, testIngredientDto, 200));

        when(recipeIngredientsRepository.findAll()).thenReturn(expectedEntityList);
        when(recipeIngredientsDtoMapper.toListDto(expectedEntityList)).thenReturn(expectedDtoList);

        Assertions.assertEquals(expectedDtoList, recipeIngredientsService.findAll());
        verify(recipeIngredientsRepository, times(1)).findAll();
        verify(recipeIngredientsDtoMapper, times(1)).toListDto(expectedEntityList);
    }

    @Test
    void delete_ExistRecipeIngredients_ReturnTrue() {
        Optional<RecipeIngredients> expectedEntity = Optional.of(new RecipeIngredients(1L, testRecipe, testIngredient, 100));

        when(recipeIngredientsRepository.findById(1L)).thenReturn(expectedEntity);

        Assertions.assertTrue(recipeIngredientsService.deleteById(1L));
        verify(recipeIngredientsRepository, times(1)).findById(1L);
        verify(recipeIngredientsRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NotExistRecipeIngredients_ReturnFalse() {
        when(recipeIngredientsRepository.findById(100L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RepositoryException.class, () -> recipeIngredientsService.deleteById(100L));
        verify(recipeIngredientsRepository, times(1)).findById(100L);
        verify(recipeIngredientsRepository, never()).deleteById(anyLong());
    }

}