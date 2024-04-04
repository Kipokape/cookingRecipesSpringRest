package com.example.cookingrecipesrest.servlet;

import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.service.RecipeService;
import com.example.cookingrecipesrest.service.impl.RecipeServiceImpl;
import com.example.cookingrecipesrest.servlet.dto.RecipeDTO;
import com.example.cookingrecipesrest.servlet.mapper.RecipeDtoMapper;
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

@WebServlet(name = "RecipeServlet", value = "/recipe/*")
public class RecipeServlet extends HttpServlet {

    private final RecipeService service;

    private final RecipeDtoMapper mapper;

    private static final String ENCODING = "UTF-8";

    private static final String CONTENT_TYPE = "application/json";

    private static final String REQUEST_MESSAGE_WRONG = "Неверный запрос!";

    private final ObjectMapper objectMapper;

    public RecipeServlet() {
        service = new RecipeServiceImpl();
        mapper = RecipeDtoMapper.INSTANCE;
        objectMapper = new ObjectMapper();
    }

    public RecipeServlet(RecipeService service, RecipeDtoMapper mapper) {
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
            List<Recipe> recipes = service.findAll();
            List<RecipeDTO> recipeDTOS = new ArrayList<>();
            for (Recipe recipe : recipes) {
                recipeDTOS.add(mapper.map(recipe));
            }
            resp.getWriter().print(objectMapper.writeValueAsString(recipeDTOS));
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 2) {
                try {
                    Long id = Long.parseLong(parts[1]);
                    Recipe recipe = service.findById(id);
                    RecipeDTO dto = mapper.map(recipe);
                    resp.getWriter().print(objectMapper.writeValueAsString(dto));
                } catch (RuntimeException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().print("Ошибка чтения рецепта. " + e.getMessage());
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
                RecipeDTO dtoInp = objectMapper.readValue(req.getReader(), RecipeDTO.class);
                if (dtoInp.recipeId() != 0) throw new RuntimeException("ID нового объекта должен быть 0.");
                checkDtoAndSave(resp, dtoInp);
            } catch (JsonParseException | UnrecognizedPropertyException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Неправильный json! " + e.getMessage());
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Ошибка создания рецепта. " + e.getMessage());
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
                RecipeDTO dtoInp = objectMapper.readValue(req.getReader(), RecipeDTO.class);
                if (dtoInp.recipeId() == 0) throw new RuntimeException("ID объекта не должен быть 0.");
                checkDtoAndSave(resp, dtoInp);
            } catch (JsonParseException | UnrecognizedPropertyException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Неправильный json! " + e.getMessage());
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Ошибка изменения рецепта. " + e.getMessage());
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(REQUEST_MESSAGE_WRONG);
        }
    }

    private void checkDtoAndSave(HttpServletResponse resp, RecipeDTO dtoInp) throws IOException {
        if (dtoInp.name() == null || dtoInp.name().isEmpty()) throw new RuntimeException("name не может быть пустым.");
        if (dtoInp.idCategory() == 0 ) throw new RuntimeException("idCategory не может быть 0.");
        Recipe recipe = mapper.map(dtoInp);
        Recipe savedRecipe = service.save(recipe);
        RecipeDTO dtoOut = mapper.map(savedRecipe);
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
                if(service.delete(new Recipe(id, 0,"",null))){
                    resp.getWriter().print("Рецепт удален.");
                } else {
                    resp.getWriter().print("Рецепта не существует.");
                }
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print("Ошибка удаления рецепта. " + e.getMessage());
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(REQUEST_MESSAGE_WRONG);
        }
    }
}
