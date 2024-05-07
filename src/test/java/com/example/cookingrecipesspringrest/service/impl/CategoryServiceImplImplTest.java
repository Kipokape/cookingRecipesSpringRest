package com.example.cookingrecipesspringrest.service.impl;

import com.example.cookingrecipesspringrest.ValidationUtils;
import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.dto.CategoryDTO;
import com.example.cookingrecipesspringrest.dto.mapper.CategoryDtoMapper;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.model.Category;
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

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringJUnitConfig
@SpringJUnitWebConfig
@ContextConfiguration(classes = SpringConfig.class)
@TestPropertySource("classpath:test-db.properties")
class CategoryServiceImplImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CategoryDtoMapper categoryDtoMapper;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_CorrectCategory_ReturnCategory() {
        CategoryDTO expected = new CategoryDTO(1L, "Test Category", new ArrayList<>());

        Category expectedEntity = new Category(1L, "Test Category", new ArrayList<>());

        when(categoryDtoMapper.toEntity(expected)).thenReturn(expectedEntity);
        when(categoryRepository.save(expectedEntity)).thenReturn(expectedEntity);
        when(categoryDtoMapper.toDto(expectedEntity)).thenReturn(expected);

        CategoryDTO actual = categoryService.save(expected);

        Assertions.assertEquals(expected, actual);
        verify(categoryDtoMapper, times(1)).toEntity(expected);
        verify(categoryRepository, times(1)).save(expectedEntity);
        verify(categoryDtoMapper, times(1)).toDto(expectedEntity);
    }

    @Test
    void save_IncorrectCategory_ThrowsServiceException() {
        CategoryDTO categoryDTO = new CategoryDTO(1L, null, new ArrayList<>());

        doThrow(new RepositoryException("Неверные данные")).when(validationUtils).validation(categoryDTO);

        Assertions.assertThrows(RepositoryException.class, () -> categoryService.save(categoryDTO));
        verify(categoryRepository, never()).save(categoryDtoMapper.toEntity(categoryDTO));
    }


    @Test
    void findById_CorrectCategory_ReturnCategory() {
        CategoryDTO expected = new CategoryDTO(1L, "Test Category", new ArrayList<>());
        Optional<Category> expectedEntity = Optional.of(new Category(1L, "Test Category", new ArrayList<>()));

        when(categoryRepository.findById(1L)).thenReturn(expectedEntity);
        when(categoryDtoMapper.toDto(expectedEntity.get())).thenReturn(expected);

        CategoryDTO actual = categoryService.findById(1L);

        Assertions.assertEquals(expected, actual);
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryDtoMapper, times(1)).toDto(expectedEntity.get());
    }

    @Test
    void findById_NotCorrectId_TrowsRepositoryException() {
        Optional<Category> expectedEntity = Optional.empty();
        when(categoryRepository.findById(1000L)).thenReturn(expectedEntity);

        Assertions.assertThrows(RepositoryException.class, () -> categoryService.findById(1000L));
        verify(categoryRepository, times(1)).findById(1000L);
    }

    @Test
    void findAll_CorrectCategory_ReturnCategories() {
        List<Category> expectedEntityList = new ArrayList<>();
        expectedEntityList.add(new Category(1L, "тест 1", new ArrayList<>()));
        expectedEntityList.add(new Category(2L, "тест 2", new ArrayList<>()));
        List<CategoryDTO> expectedDtoList = new ArrayList<>();
        expectedDtoList.add(new CategoryDTO(1L, "тест 1", new ArrayList<>()));
        expectedDtoList.add(new CategoryDTO(2L, "тест 2", new ArrayList<>()));

        when(categoryRepository.findAll()).thenReturn(expectedEntityList);
        when(categoryDtoMapper.toListDto(expectedEntityList)).thenReturn(expectedDtoList);

        Assertions.assertEquals(expectedDtoList, categoryService.findAll());
        verify(categoryRepository, times(1)).findAll();
        verify(categoryDtoMapper, times(1)).toListDto(expectedEntityList);
    }

    @Test
    void delete_ExistCategory_ReturnTrue() {
        Optional<Category> expectedEntity = Optional.of(new Category(1L, "Test Category", new ArrayList<>()));

        when(categoryRepository.findById(1L)).thenReturn(expectedEntity);

        Assertions.assertTrue(categoryService.deleteById(1L));
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NotExistCategory_ReturnFalse() {
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RepositoryException.class, () -> categoryService.deleteById(100L));
        verify(categoryRepository, times(1)).findById(100L);
        verify(categoryRepository, never()).deleteById(anyLong());
    }


}