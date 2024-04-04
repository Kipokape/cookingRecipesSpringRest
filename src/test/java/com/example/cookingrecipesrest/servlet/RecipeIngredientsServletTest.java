package com.example.cookingrecipesrest.servlet;

import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.service.RecipeIngredientsService;
import com.example.cookingrecipesrest.service.impl.RecipeIngredientsServiceImpl;
import com.example.cookingrecipesrest.service.impl.RecipeServiceImpl;
import com.example.cookingrecipesrest.servlet.dto.RecipeDTO;
import com.example.cookingrecipesrest.servlet.dto.RecipeIngredientsDTO;
import com.example.cookingrecipesrest.servlet.mapper.RecipeDtoMapper;
import com.example.cookingrecipesrest.servlet.mapper.RecipeIngredientsDtoMapper;
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

class RecipeIngredientsServletTest {
    private RecipeIngredientsService recipeIngredientsService;

    private RecipeIngredientsDtoMapper recipeIngredientsDtoMapper;

    private RecipeIngredientsServlet recipeIngredientsServlet;

    HttpServletRequest request;
    HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        recipeIngredientsService = mock(RecipeIngredientsServiceImpl.class);
        recipeIngredientsDtoMapper = mock(RecipeIngredientsDtoMapper.class);
        recipeIngredientsServlet = new RecipeIngredientsServlet(recipeIngredientsService, recipeIngredientsDtoMapper);
    }

    @Test
    void doGet_allRecipeIngredients() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<RecipeIngredients> recipeIngredients = Arrays.asList(new RecipeIngredients(1L,1L, 1L, 333),
                new RecipeIngredients(2L,2L, 2L, 444));
        List<RecipeIngredientsDTO> recipeIngredientsDTOS = Arrays.asList(new RecipeIngredientsDTO(1L,1L, 1L, 333),
                new RecipeIngredientsDTO(2L,2L, 2L, 444));

        when(recipeIngredientsService.findAll()).thenReturn(recipeIngredients);
        when(recipeIngredientsDtoMapper.map(any(RecipeIngredients.class))).thenAnswer(invocation -> {
            RecipeIngredients recipeIngredients1 = invocation.getArgument(0);
            return new RecipeIngredientsDTO(
                    recipeIngredients1.getId(), recipeIngredients1.getRecipeId(),
                    recipeIngredients1.getIngredientId(), recipeIngredients1.getWeight());
        });

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        recipeIngredientsServlet.doGet(request, response);

        String expectedJson = objectMapper.writeValueAsString(recipeIngredientsDTOS);

        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doGet_oneRecipeIngredients() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        when(request.getPathInfo()).thenReturn("/1");

        RecipeIngredients recipeIngredients = new RecipeIngredients(1L,1L, 1L, 222);
        RecipeIngredientsDTO recipeIngredientsDTO = new RecipeIngredientsDTO(1L,1L, 1L, 222);
        when(recipeIngredientsService.findById(1L)).thenReturn(recipeIngredients);
        when(recipeIngredientsDtoMapper.map(recipeIngredients)).thenReturn(recipeIngredientsDTO);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        recipeIngredientsServlet.doGet(request, response);
        String expectedJson = objectMapper.writeValueAsString(recipeIngredientsDTO);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doPost_createRecipeIngredients() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        String jsonRequest = "{\"recipeId\": 1, \"ingredientId\": 1, \"weight\": 333}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

        when(request.getPathInfo()).thenReturn(null);

        RecipeIngredientsDTO dtoInp = new RecipeIngredientsDTO(0L,1L, 1L, 333);
        RecipeIngredients recipeIngredients = new RecipeIngredients(1L,1L, 1L, 333);
        when(objectMapper.readValue(jsonRequest, RecipeIngredientsDTO.class)).thenReturn(dtoInp);
        when(recipeIngredientsDtoMapper.map(dtoInp)).thenReturn(recipeIngredients);
        when(recipeIngredientsService.save(recipeIngredients)).thenReturn(recipeIngredients);
        when(recipeIngredientsDtoMapper.map(recipeIngredients)).thenReturn(dtoInp);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        recipeIngredientsServlet.doPost(request, response);

        objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(dtoInp);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doPut_updateRecipeIngredients() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        String jsonRequest = "{\"recipeIngredientsId\": 1, \"recipeId\": 1,\"ingredientId\": 1, \"weight\": 333}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

        when(request.getPathInfo()).thenReturn(null);

        RecipeIngredientsDTO dtoInp = new RecipeIngredientsDTO(1L,1L, 1L, 333);
        RecipeIngredients recipeIngredients = new RecipeIngredients(1L,1L, 1L, 0);
        when(objectMapper.readValue(jsonRequest, RecipeIngredientsDTO.class)).thenReturn(dtoInp);
        when(recipeIngredientsDtoMapper.map(dtoInp)).thenReturn(recipeIngredients);
        when(recipeIngredientsService.save(recipeIngredients)).thenReturn(recipeIngredients);
        when(recipeIngredientsDtoMapper.map(recipeIngredients)).thenReturn(dtoInp);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        recipeIngredientsServlet.doPut(request, response);

        objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(dtoInp);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doDelete_ExistRecipeIngredientsId() throws Exception {

        when(request.getPathInfo()).thenReturn("/1");
        when(recipeIngredientsService.delete(new RecipeIngredients(1,0, 0, 0))).thenReturn(true);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        recipeIngredientsServlet.doDelete(request, response);

        assertEquals("Ингредиент рецепта удален.", stringWriter.toString().trim());
    }

    @Test
    void doDelete_NotExistRecipeIngredientsId() throws Exception {

        when(request.getPathInfo()).thenReturn("/324");
        when(recipeIngredientsService.delete(new RecipeIngredients(1,0, 0, 0))).thenReturn(false);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        recipeIngredientsServlet.doDelete(request, response);

        assertEquals("Ингредиента рецепта не существует.", stringWriter.toString().trim());
    }

}