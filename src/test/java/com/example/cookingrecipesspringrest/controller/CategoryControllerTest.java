package com.example.cookingrecipesspringrest.controller;

import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.dto.CategoryDTO;
import com.example.cookingrecipesspringrest.dto.ResponseDTO;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.exception.ServiceException;
import com.example.cookingrecipesspringrest.service.CategoryService;
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
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllCategory_ReturnListOfCategories() {
        List<CategoryDTO> expected = new ArrayList<>();
        expected.add(new CategoryDTO(1L, "тест", new ArrayList<>()));
        expected.add(new CategoryDTO(2L, "тест 2", new ArrayList<>()));
        expected.add(new CategoryDTO(3L, "тест 3", new ArrayList<>()));

        when(categoryService.findAll()).thenReturn(expected);
        List<CategoryDTO> actual = categoryController.findAllCategories();
        assertEquals(expected, actual);
    }

    @Test
    void findCategoryById_CorrectId_ReturnCategory() {
        CategoryDTO expected = new CategoryDTO(1L, "Test Category", new ArrayList<>());
        when(categoryService.findById(1L)).thenReturn(expected);
        CategoryDTO actual = categoryController.findCategoryById(1L);
        assertEquals(expected, actual);
    }

    @Test
    void findCategoryById_NoneExistId_ReturnCategory() {
        when(categoryService.findById(100L)).thenThrow(new RepositoryException("тест"));
        assertThrows(RepositoryException.class, () -> categoryController.findCategoryById(100L));
    }

    @Test
    void findCategoryById_IncorrectId_ReturnCategory() {
        assertThrows(ServiceException.class, () -> categoryController.findCategoryById(-1L));
        verify(categoryService, never()).findById(-1L);
    }

    @Test
    void saveNewCategory_CorrectCategory_ReturnCategory() {
        CategoryDTO expected = new CategoryDTO(0, "тест", new ArrayList<>());
        when(categoryService.save(expected)).thenReturn(expected);
        CategoryDTO actual = categoryController.saveNewCategory(expected);
        assertEquals(expected, actual);
    }

    @Test
    void saveNewCategory_IncorrectCategory_ThrowsServiceException() {
        CategoryDTO expected = new CategoryDTO(1, "тест", new ArrayList<>());
        Assertions.assertThrows(ServiceException.class, () -> categoryController.saveNewCategory(expected));
        verify(categoryService, never()).save(expected);
    }

    @Test
    void updateExistCategory_CorrectCategory_ReturnCategory() {
        CategoryDTO expected = new CategoryDTO(1, "тест", new ArrayList<>());
        when(categoryService.save(expected)).thenReturn(expected);
        CategoryDTO actual = categoryController.updateExistCategory(expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateExistCategory_IncorrectCategory_ThrowsServiceException() {
        CategoryDTO expected = new CategoryDTO(0, "тест", new ArrayList<>());
        Assertions.assertThrows(ServiceException.class, () -> categoryController.updateExistCategory(expected));
        verify(categoryService, never()).save(expected);
    }

    @Test
    void deleteCategoryById_CorrectCategoryId_ReturnTrue() {
        when(categoryService.deleteById(1L)).thenReturn(true);
        assertEquals(new ResponseDTO("Категория успешно удалена!"), categoryController.deleteCategoryById(1L));
    }

    @Test
    void deleteCategoryById_IncorrectCategoryId_ThrowsServiceException() {
        Assertions.assertThrows(ServiceException.class, () -> categoryController.deleteCategoryById(-1L));
        verify(categoryService, never()).deleteById(-1L);
    }

}