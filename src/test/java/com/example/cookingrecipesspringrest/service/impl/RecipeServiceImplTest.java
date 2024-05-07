package com.example.cookingrecipesspringrest.service.impl;

import com.example.cookingrecipesspringrest.ValidationUtils;
import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.dto.CategoryDTO;
import com.example.cookingrecipesspringrest.dto.RecipeDTO;
import com.example.cookingrecipesspringrest.dto.mapper.RecipeDtoMapper;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.model.Category;
import com.example.cookingrecipesspringrest.model.Recipe;
import com.example.cookingrecipesspringrest.repository.CategoryRepository;
import com.example.cookingrecipesspringrest.repository.RecipeRepository;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringJUnitConfig
@SpringJUnitWebConfig
@ContextConfiguration(classes = SpringConfig.class)
@TestPropertySource("classpath:test-db.properties")
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RecipeDtoMapper recipeDtoMapper;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private final CategoryDTO testCategoryDto = new CategoryDTO(1L, "категория 1", new ArrayList<>());

    private final Category testCategory = new Category(1L, "категория 1", new ArrayList<>());

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void save_CorrectRecipe_ReturnRecipe() {
        RecipeDTO expected = new RecipeDTO(1L, testCategoryDto, "рецепт 1", new ArrayList<>());

        Recipe expectedEntity = new Recipe(1L, testCategory, "рецепт 1", new ArrayList<>());

        when(recipeDtoMapper.toEntity(expected)).thenReturn(expectedEntity);
        when(categoryRepository.findById(expected.category().categoryId())).thenReturn(Optional.of(testCategory));
        when(recipeRepository.save(expectedEntity)).thenReturn(expectedEntity);
        when(recipeDtoMapper.toDto(expectedEntity)).thenReturn(expected);

        RecipeDTO actual = recipeService.save(expected);

        Assertions.assertEquals(expected, actual);
        verify(recipeDtoMapper, times(1)).toEntity(expected);
        verify(recipeRepository, times(1)).save(expectedEntity);
        verify(recipeDtoMapper, times(1)).toDto(expectedEntity);
        verify(categoryRepository, times(1)).findById(expected.category().categoryId());
    }

    @Test
    void save_IncorrectRecipe_ThrowsServiceException() {
        RecipeDTO expected = new RecipeDTO(1L, testCategoryDto, "", new ArrayList<>());

        doThrow(new RepositoryException("Неверные данные")).when(validationUtils).validation(expected);

        Assertions.assertThrows(RepositoryException.class, () -> recipeService.save(expected));
        verify(recipeRepository, never()).save(recipeDtoMapper.toEntity(expected));
    }


    @Test
    void findById_CorrectRecipe_ReturnRecipe() {
        RecipeDTO expected = new RecipeDTO(1L, testCategoryDto, "рецепт 1", new ArrayList<>());
        Optional<Recipe> expectedEntity = Optional.of(new Recipe(1L, testCategory, "рецепт 1", new ArrayList<>()));

        when(recipeRepository.findById(1L)).thenReturn(expectedEntity);
        when(recipeDtoMapper.toDto(expectedEntity.get())).thenReturn(expected);

        RecipeDTO actual = recipeService.findById(1L);

        Assertions.assertEquals(expected, actual);
        verify(recipeRepository, times(1)).findById(1L);
        verify(recipeDtoMapper, times(1)).toDto(expectedEntity.get());
    }

    @Test
    void findById_NotCorrectId_TrowsRepositoryException() {
        Optional<Recipe> expectedEntity = Optional.empty();
        when(recipeRepository.findById(1000L)).thenReturn(expectedEntity);

        Assertions.assertThrows(RepositoryException.class, () -> recipeService.findById(1000L));
        verify(recipeRepository, times(1)).findById(1000L);
    }

    @Test
    void findAll_CorrectRecipes_ReturnRecipes() {
        List<Recipe> expectedEntityList = new ArrayList<>();
        expectedEntityList.add(new Recipe(1L, testCategory, "рецепт 1", new ArrayList<>()));
        expectedEntityList.add(new Recipe(1L, testCategory, "рецепт 2", new ArrayList<>()));
        List<RecipeDTO> expectedDtoList = new ArrayList<>();
        expectedDtoList.add(new RecipeDTO(1L, testCategoryDto, "рецепт 1", new ArrayList<>()));
        expectedDtoList.add(new RecipeDTO(1L, testCategoryDto, "рецепт 2", new ArrayList<>()));

        when(recipeRepository.findAll()).thenReturn(expectedEntityList);
        when(recipeDtoMapper.toListDto(expectedEntityList)).thenReturn(expectedDtoList);

        Assertions.assertEquals(expectedDtoList, recipeService.findAll());
        verify(recipeRepository, times(1)).findAll();
        verify(recipeDtoMapper, times(1)).toListDto(expectedEntityList);
    }

    @Test
    void delete_ExistRecipe_ReturnTrue() {
        Optional<Recipe> expectedEntity = Optional.of(new Recipe(1L, testCategory, "рецепт 1", new ArrayList<>()));

        when(recipeRepository.findById(1L)).thenReturn(expectedEntity);

        Assertions.assertTrue(recipeService.deleteById(1L));
        verify(recipeRepository, times(1)).findById(1L);
        verify(recipeRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NotExistRecipe_ReturnFalse() {
        when(recipeRepository.findById(100L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RepositoryException.class, () -> recipeService.deleteById(100L));
        verify(recipeRepository, times(1)).findById(100L);
        verify(recipeRepository, never()).deleteById(anyLong());
    }

}