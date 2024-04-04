package com.example.cookingrecipesrest.servlet;

import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.service.IngredientService;
import com.example.cookingrecipesrest.service.impl.CategoryServiceImpl;
import com.example.cookingrecipesrest.service.impl.IngredientServiceImpl;
import com.example.cookingrecipesrest.servlet.dto.CategoryDTO;
import com.example.cookingrecipesrest.servlet.dto.IngredientDTO;
import com.example.cookingrecipesrest.servlet.mapper.CategoryDtoMapper;
import com.example.cookingrecipesrest.servlet.mapper.IngredientDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IngredientServletTest {

    private IngredientService ingredientService;

    private IngredientDtoMapper ingredientDtoMapper;

    private IngredientServlet ingredientServlet;

    HttpServletRequest request;
    HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        ingredientService = mock(IngredientServiceImpl.class);
        ingredientDtoMapper = mock(IngredientDtoMapper.class);
        ingredientServlet = new IngredientServlet(ingredientService, ingredientDtoMapper);
    }

    @Test
    void doGet_allIngredients() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Ingredient> ingredients = Arrays.asList(new Ingredient(1L, "Ingredient1", null),
                new Ingredient(2L, "Ingredient2", null));
        List<IngredientDTO> ingredientDTOS = Arrays.asList(new IngredientDTO(1L, "Ingredient1", null),
                new IngredientDTO(2L, "Ingredient2", null));

        when(ingredientService.findAll()).thenReturn(ingredients);
        when(ingredientDtoMapper.map(any(Ingredient.class))).thenAnswer(invocation -> {
            Ingredient ingredient = invocation.getArgument(0);
            return new IngredientDTO(ingredient.getId(), ingredient.getName(), null);
        });

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        ingredientServlet.doGet(request, response);

        String expectedJson = objectMapper.writeValueAsString(ingredientDTOS);

        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doGet_oneIngredient() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        when(request.getPathInfo()).thenReturn("/1");

        Ingredient ingredient = new Ingredient(1L, "Ingredient1", null);
        IngredientDTO ingredientDTO = new IngredientDTO(1L, "Ingredient1", null);
        when(ingredientService.findById(1L)).thenReturn(ingredient);
        when(ingredientDtoMapper.map(ingredient)).thenReturn(ingredientDTO);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        ingredientServlet.doGet(request, response);
        String expectedJson = objectMapper.writeValueAsString(ingredientDTO);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doPost_createIngredient() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        String jsonRequest = "{\"name\": \"New Ingredient\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

        when(request.getPathInfo()).thenReturn(null);

        IngredientDTO dtoInp = new IngredientDTO(0L, "New Ingredient", null);
        Ingredient ingredient = new Ingredient(1L, "New Ingredient", null);
        when(objectMapper.readValue(jsonRequest, IngredientDTO.class)).thenReturn(dtoInp);
        when(ingredientDtoMapper.map(dtoInp)).thenReturn(ingredient);
        when(ingredientService.save(ingredient)).thenReturn(ingredient);
        when(ingredientDtoMapper.map(ingredient)).thenReturn(dtoInp);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        ingredientServlet.doPost(request, response);

        objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(dtoInp);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doPut_updateIngredient() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        String jsonRequest = "{\"ingredientId\": 1, \"name\": \"Updated Ingredient\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

        when(request.getPathInfo()).thenReturn(null);

        IngredientDTO dtoInp = new IngredientDTO(1L, "Updated Ingredient", null);
        Ingredient ingredient = new Ingredient(1L, "Updated Ingredient", null);
        when(objectMapper.readValue(jsonRequest, IngredientDTO.class)).thenReturn(dtoInp);
        when(ingredientDtoMapper.map(dtoInp)).thenReturn(ingredient);
        when(ingredientService.save(ingredient)).thenReturn(ingredient);
        when(ingredientDtoMapper.map(ingredient)).thenReturn(dtoInp);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        ingredientServlet.doPut(request, response);

        objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(dtoInp);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doDelete_ExistIngredientId() throws Exception {

        when(request.getPathInfo()).thenReturn("/1");
        when(ingredientService.delete(new Ingredient(1, "", null))).thenReturn(true);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        ingredientServlet.doDelete(request, response);

        assertEquals("Ингредиент удален.", stringWriter.toString().trim());
    }

    @Test
    void doDelete_NotExistIngredientId() throws Exception {

        when(request.getPathInfo()).thenReturn("/324");
        when(ingredientService.delete(new Ingredient(1, "", null))).thenReturn(false);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        ingredientServlet.doDelete(request, response);

        assertEquals("Ингредиента не существует.", stringWriter.toString().trim());
    }

}