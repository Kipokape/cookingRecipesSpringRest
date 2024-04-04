package com.example.cookingrecipesrest.servlet;

import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.service.RecipeService;
import com.example.cookingrecipesrest.service.impl.CategoryServiceImpl;
import com.example.cookingrecipesrest.service.impl.RecipeServiceImpl;
import com.example.cookingrecipesrest.servlet.dto.CategoryDTO;
import com.example.cookingrecipesrest.servlet.dto.RecipeDTO;
import com.example.cookingrecipesrest.servlet.mapper.CategoryDtoMapper;
import com.example.cookingrecipesrest.servlet.mapper.RecipeDtoMapper;
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

class RecipeServletTest {


    private RecipeService recipeService;

    private RecipeDtoMapper recipeDtoMapper;

    private RecipeServlet recipeServlet;

    HttpServletRequest request;
    HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        recipeService = mock(RecipeServiceImpl.class);
        recipeDtoMapper = mock(RecipeDtoMapper.class);
        recipeServlet = new RecipeServlet(recipeService, recipeDtoMapper);
    }

    @Test
    void doGet_allRecipes() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Recipe> recipes = Arrays.asList(new Recipe(1L,1L, "Recipe1", null),
                new Recipe(2L,2L, "Recipe2", null));
        List<RecipeDTO> recipeDTOS = Arrays.asList(new RecipeDTO(1L,1L, "Recipe1", null),
                new RecipeDTO(2L,2L, "Recipe2", null));

        when(recipeService.findAll()).thenReturn(recipes);
        when(recipeDtoMapper.map(any(Recipe.class))).thenAnswer(invocation -> {
            Recipe recipe = invocation.getArgument(0);
            return new RecipeDTO(recipe.getId(), recipe.getIdCategory(), recipe.getName(), null);
        });

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        recipeServlet.doGet(request, response);

        String expectedJson = objectMapper.writeValueAsString(recipeDTOS);

        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doGet_oneRecipe() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        when(request.getPathInfo()).thenReturn("/1");

        Recipe recipe = new Recipe(1L,1L, "Recipe1", null);
        RecipeDTO recipeDTO = new RecipeDTO(1L,1L, "Recipe1", null);
        when(recipeService.findById(1L)).thenReturn(recipe);
        when(recipeDtoMapper.map(recipe)).thenReturn(recipeDTO);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        recipeServlet.doGet(request, response);
        String expectedJson = objectMapper.writeValueAsString(recipeDTO);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doPost_createRecipe() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        String jsonRequest = "{\"idCategory\": 1, \"name\": \"Updated Recipe\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

        when(request.getPathInfo()).thenReturn(null);

        RecipeDTO dtoInp = new RecipeDTO(0L,1L, "Updated Recipe", null);
        Recipe recipe = new Recipe(1L,1L, "Updated Recipe", null);
        when(objectMapper.readValue(jsonRequest, RecipeDTO.class)).thenReturn(dtoInp);
        when(recipeDtoMapper.map(dtoInp)).thenReturn(recipe);
        when(recipeService.save(recipe)).thenReturn(recipe);
        when(recipeDtoMapper.map(recipe)).thenReturn(dtoInp);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        recipeServlet.doPost(request, response);

        objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(dtoInp);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doPut_updateRecipe() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        String jsonRequest = "{\"idCategory\": 1,\"recipeId\": 1, \"name\": \"Updated Recipe\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

        when(request.getPathInfo()).thenReturn(null);

        RecipeDTO dtoInp = new RecipeDTO(1L,1L, "Updated Recipe", null);
        Recipe recipe = new Recipe(1L,1L, "Updated Recipe", null);
        when(objectMapper.readValue(jsonRequest, RecipeDTO.class)).thenReturn(dtoInp);
        when(recipeDtoMapper.map(dtoInp)).thenReturn(recipe);
        when(recipeService.save(recipe)).thenReturn(recipe);
        when(recipeDtoMapper.map(recipe)).thenReturn(dtoInp);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        recipeServlet.doPut(request, response);

        objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(dtoInp);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    void doDelete_ExistRecipeId() throws Exception {

        when(request.getPathInfo()).thenReturn("/1");
        when(recipeService.delete(new Recipe(1,0, "", null))).thenReturn(true);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        recipeServlet.doDelete(request, response);

        assertEquals("Рецепт удален.", stringWriter.toString().trim());
    }

    @Test
    void doDelete_NotExistRecipeId() throws Exception {

        when(request.getPathInfo()).thenReturn("/324");
        when(recipeService.delete(new Recipe(1,0, "", null))).thenReturn(false);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        recipeServlet.doDelete(request, response);

        assertEquals("Рецепта не существует.", stringWriter.toString().trim());
    }

}