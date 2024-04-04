package com.example.cookingrecipesrest.servlet;

import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.service.CategoryService;
import com.example.cookingrecipesrest.service.impl.CategoryServiceImpl;
import com.example.cookingrecipesrest.servlet.dto.CategoryDTO;
import com.example.cookingrecipesrest.servlet.mapper.CategoryDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServletTest {

    private CategoryService categoryService;

    private CategoryDtoMapper categoryDtoMapper;

    private CategoryServlet categoryServlet;

    HttpServletRequest request;
    HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        categoryService = mock(CategoryServiceImpl.class);
        categoryDtoMapper = mock(CategoryDtoMapper.class);
        categoryServlet = new CategoryServlet(categoryService, categoryDtoMapper);
    }

    @Test
    void doGet_allCategories() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Category> categories = Arrays.asList(new Category(1L, "Category1", null),
                new Category(2L, "Category2", null));
        List<CategoryDTO> categoryDTOs = Arrays.asList(new CategoryDTO(1L, "Category1", null),
                new CategoryDTO(2L, "Category2", null));

        when(categoryService.findAll()).thenReturn(categories);
        when(categoryDtoMapper.map(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            return new CategoryDTO(category.getId(), category.getName(), null);
        });

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        categoryServlet.doGet(request, response);

        String expectedJson = objectMapper.writeValueAsString(categoryDTOs);

        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doGet_oneCategory() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        when(request.getPathInfo()).thenReturn("/1");

        Category category = new Category(1L, "Category1", null);
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Category1", null);
        when(categoryService.findById(1L)).thenReturn(category);
        when(categoryDtoMapper.map(category)).thenReturn(categoryDTO);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        categoryServlet.doGet(request, response);
        String expectedJson = objectMapper.writeValueAsString(categoryDTO);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doPost_createCategory() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        String jsonRequest = "{\"name\": \"New Category\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

        when(request.getPathInfo()).thenReturn(null);

        CategoryDTO dtoInp = new CategoryDTO(0L, "New Category", null);
        Category category = new Category(1L, "New Category", null);
        when(objectMapper.readValue(jsonRequest, CategoryDTO.class)).thenReturn(dtoInp);
        when(categoryDtoMapper.map(dtoInp)).thenReturn(category);
        when(categoryService.save(category)).thenReturn(category);
        when(categoryDtoMapper.map(category)).thenReturn(dtoInp);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        categoryServlet.doPost(request, response);

        objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(dtoInp);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doPut_updateCategory() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        String jsonRequest = "{\"categoryId\": 1, \"name\": \"Updated Category\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

        when(request.getPathInfo()).thenReturn(null);

        CategoryDTO dtoInp = new CategoryDTO(1L, "Updated Category", null);
        Category category = new Category(1L, "Updated Category", null);
        when(objectMapper.readValue(jsonRequest, CategoryDTO.class)).thenReturn(dtoInp);
        when(categoryDtoMapper.map(dtoInp)).thenReturn(category);
        when(categoryService.save(category)).thenReturn(category);
        when(categoryDtoMapper.map(category)).thenReturn(dtoInp);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        categoryServlet.doPut(request, response);

        objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(dtoInp);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doDelete_ExistCategoryId() throws Exception {

        when(request.getPathInfo()).thenReturn("/1");
        when(categoryService.delete(new Category(1, "", null))).thenReturn(true);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        categoryServlet.doDelete(request, response);

        assertEquals("Категория удалена.", stringWriter.toString().trim());
    }

    @Test
    void doDelete_NotExistCategoryId() throws Exception {

        when(request.getPathInfo()).thenReturn("/324");
        when(categoryService.delete(new Category(1, "", null))).thenReturn(false);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        categoryServlet.doDelete(request, response);

        assertEquals("Категории не существует.", stringWriter.toString().trim());
    }


}