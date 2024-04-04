package com.example.cookingrecipesrest.servlet;

import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.service.IngredientService;
import com.example.cookingrecipesrest.service.impl.IngredientServiceImpl;
import com.example.cookingrecipesrest.servlet.dto.IngredientDTO;
import com.example.cookingrecipesrest.servlet.mapper.IngredientDtoMapper;
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

@WebServlet(name = "IngredientServlet", value = "/ingredient/*")
public class IngredientServlet extends HttpServlet {


    private final IngredientService service;

    private final IngredientDtoMapper mapper;

    private static final String ENCODING = "UTF-8";

    private static final String CONTENT_TYPE = "application/json";

    private static final String REQUEST_MESSAGE_WRONG = "Неверный запрос!";

    private final ObjectMapper objectMapper;

    public IngredientServlet() {
        service = new IngredientServiceImpl();
        mapper = IngredientDtoMapper.INSTANCE;
        objectMapper = new ObjectMapper();
    }

    public IngredientServlet(IngredientService service, IngredientDtoMapper  mapper) {
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
            List<Ingredient> ingredients = service.findAll();
            List<IngredientDTO> ingredientDTOS = new ArrayList<>();
            for (Ingredient ingredient : ingredients) {
                ingredientDTOS.add(mapper.map(ingredient));
            }
            resp.getWriter().print(objectMapper.writeValueAsString(ingredientDTOS));
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 2) {
                try {
                    Long id = Long.parseLong(parts[1]);
                    Ingredient ingredient = service.findById(id);
                    IngredientDTO dto = mapper.map(ingredient);
                    resp.getWriter().print(objectMapper.writeValueAsString(dto));
                } catch (RuntimeException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().print("Ошибка чтения ингредиента. " + e.getMessage());
                }

            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(REQUEST_MESSAGE_WRONG);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        req.setCharacterEncoding(ENCODING);
        resp.setCharacterEncoding(ENCODING);
        resp.setContentType(CONTENT_TYPE);
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            try {
                IngredientDTO dtoInp = objectMapper.readValue(req.getReader(), IngredientDTO.class);
                if (dtoInp.ingredientId() != 0) throw new RuntimeException("ID нового объекта должен быть 0.");
                checkDtoAndSave(resp, dtoInp);
            } catch (JsonParseException | UnrecognizedPropertyException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Неправильный json! " + e.getMessage());
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Ошибка создания ингредиента. " + e.getMessage());
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
                IngredientDTO dtoInp = objectMapper.readValue(req.getReader(), IngredientDTO.class);
                if (dtoInp.ingredientId() == 0) throw new RuntimeException("ID объекта не должен быть 0.");
                checkDtoAndSave(resp, dtoInp);
            } catch (JsonParseException | UnrecognizedPropertyException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Неправильный json! " + e.getMessage());
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Ошибка изменения ингредиента. " + e.getMessage());
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(REQUEST_MESSAGE_WRONG);
        }
    }

    private void checkDtoAndSave(HttpServletResponse resp, IngredientDTO dtoInp) throws IOException {
        if (dtoInp.name() == null || dtoInp.name().isEmpty()) throw new RuntimeException("name не может быть пустым.");
        Ingredient ingredient = mapper.map(dtoInp);
        Ingredient savedIngredient = service.save(ingredient);
        IngredientDTO dtoOut = mapper.map(savedIngredient);
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
                if(service.delete(new Ingredient(id, "",null))){
                    resp.getWriter().print("Ингредиент удален.");
                } else {
                    resp.getWriter().print("Ингредиента не существует.");
                }
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print("Ошибка удаления ингредиента. " + e.getMessage());
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(REQUEST_MESSAGE_WRONG);
        }
    }
}
