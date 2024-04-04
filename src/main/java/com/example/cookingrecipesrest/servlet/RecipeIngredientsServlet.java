package com.example.cookingrecipesrest.servlet;

import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.service.RecipeIngredientsService;
import com.example.cookingrecipesrest.service.impl.RecipeIngredientsServiceImpl;
import com.example.cookingrecipesrest.servlet.dto.RecipeIngredientsDTO;
import com.example.cookingrecipesrest.servlet.mapper.RecipeIngredientsDtoMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RecipeIngredientsServlet", value = "/recipe-ingredient/*")
public class RecipeIngredientsServlet extends HttpServlet {


    private final RecipeIngredientsService service;

    private final RecipeIngredientsDtoMapper mapper;

    private static final String ENCODING = "UTF-8";

    private static final String CONTENT_TYPE = "application/json";

    private static final String REQUEST_MESSAGE_WRONG = "Неверный запрос!";

    private final ObjectMapper objectMapper;

    public RecipeIngredientsServlet() {
        service = new RecipeIngredientsServiceImpl();
        mapper = RecipeIngredientsDtoMapper.INSTANCE;
        objectMapper = new ObjectMapper();
    }

    public RecipeIngredientsServlet(RecipeIngredientsService service, RecipeIngredientsDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
        objectMapper = new ObjectMapper();
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(ENCODING);
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<RecipeIngredients> ingredients = service.findAll();
            List<RecipeIngredientsDTO> recipeIngredientsDTOS = new ArrayList<>();
            for (RecipeIngredients recipeIngredients : ingredients) {
                recipeIngredientsDTOS.add(mapper.map(recipeIngredients));
            }
            resp.getWriter().print(objectMapper.writeValueAsString(recipeIngredientsDTOS));
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 2) {
                try {
                    Long id = Long.parseLong(parts[1]);
                    RecipeIngredients recipeIngredients = service.findById(id);
                    RecipeIngredientsDTO dto = mapper.map(recipeIngredients);
                    resp.getWriter().print(objectMapper.writeValueAsString(dto));
                } catch (RuntimeException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().print("Ошибка чтения ингредиента рецепта. " + e.getMessage());
                }

            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(REQUEST_MESSAGE_WRONG);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(ENCODING);
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            try {
                RecipeIngredientsDTO dtoInp = objectMapper.readValue(req.getReader(), RecipeIngredientsDTO.class);
                if (dtoInp.recipeIngredientsId() != 0) throw new RuntimeException("ID нового объекта должен быть 0.");
                checkDtoAndSave(resp, dtoInp);
            } catch (JsonParseException | UnrecognizedPropertyException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Неправильный json! " + e.getMessage());
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Ошибка создания ингредиента рецепта. " + e.getMessage());
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(REQUEST_MESSAGE_WRONG);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(ENCODING);
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            try {
                RecipeIngredientsDTO dtoInp = objectMapper.readValue(req.getReader(), RecipeIngredientsDTO.class);
                if (dtoInp.recipeIngredientsId() == 0) throw new RuntimeException("ID объекта не должен быть 0.");
                checkDtoAndSave(resp, dtoInp);
            } catch (JsonParseException | UnrecognizedPropertyException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Неправильный json! " + e.getMessage());
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Ошибка изменения ингредиента рецепта. " + e.getMessage());
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(REQUEST_MESSAGE_WRONG);
        }
    }

    private void checkDtoAndSave(HttpServletResponse resp, RecipeIngredientsDTO dtoInp) throws IOException {
        if (dtoInp.recipeId() == 0) throw new RuntimeException("recipeId не может быть 0.");
        if (dtoInp.ingredientId() == 0) throw new RuntimeException("ingredientId не может быть 0.");
        if (dtoInp.weight() == 0) throw new RuntimeException("weight не может быть 0.");
        RecipeIngredients recipeIngredients = mapper.map(dtoInp);
        RecipeIngredients savedRecipeIngredients = service.save(recipeIngredients);
        RecipeIngredientsDTO dtoOut = mapper.map(savedRecipeIngredients);
        resp.getWriter().print(objectMapper.writeValueAsString(dtoOut));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(ENCODING);
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        String pathInfo = req.getPathInfo();
        String[] parts;
        if (pathInfo != null && (parts = pathInfo.split("/")).length == 2) {
            try {
                long id = Long.parseLong(parts[1]);
                if(service.delete(new RecipeIngredients(id, 0, 0,0))){
                    resp.getWriter().print("Ингредиент рецепта удален.");
                } else {
                    resp.getWriter().print("Ингредиента рецепта не существует.");
                }
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print("Ошибка удаления ингредиента рецепта. " + e.getMessage());
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(REQUEST_MESSAGE_WRONG);
        }
    }
}
