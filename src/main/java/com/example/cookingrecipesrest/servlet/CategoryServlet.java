package com.example.cookingrecipesrest.servlet;

import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.service.CategoryService;
import com.example.cookingrecipesrest.service.impl.CategoryServiceImpl;
import com.example.cookingrecipesrest.servlet.dto.CategoryDTO;
import com.example.cookingrecipesrest.servlet.mapper.CategoryDtoMapper;
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

@WebServlet(name = "CategoryServlet", value = "/category/*")
public class CategoryServlet extends HttpServlet {

    private final CategoryService service;

    private final CategoryDtoMapper mapper;

    private static final String ENCODING = "UTF-8";

    private static final String CONTENT_TYPE = "application/json";

    private static final String REQUEST_MESSAGE_WRONG = "Неверный запрос!";

    private final ObjectMapper objectMapper;

    public CategoryServlet() {
        service = new CategoryServiceImpl();
        mapper = CategoryDtoMapper.INSTANCE;
        objectMapper = new ObjectMapper();
    }

    public CategoryServlet(CategoryService service, CategoryDtoMapper mapper) {
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
            List<Category> categories = service.findAll();
            List<CategoryDTO> categoryDTOS = new ArrayList<>();
            for (Category category : categories) {
                categoryDTOS.add(mapper.map(category));
            }
            resp.getWriter().print(objectMapper.writeValueAsString(categoryDTOS));
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 2) {
                try {
                    Long id = Long.parseLong(parts[1]);
                    Category category = service.findById(id);
                    CategoryDTO dto = mapper.map(category);
                    resp.getWriter().print(objectMapper.writeValueAsString(dto));
                } catch (RuntimeException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().print("Ошибка чтения категории. " + e.getMessage());
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
                CategoryDTO dtoInp = objectMapper.readValue(req.getReader(), CategoryDTO.class);
                if (dtoInp.categoryId() != 0) throw new RuntimeException("ID нового объекта должен быть 0.");
                checkDtoAndSave(resp, dtoInp);
            } catch (JsonParseException | UnrecognizedPropertyException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Неправильный json! " + e.getMessage());
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Ошибка создания категории. " + e.getMessage());
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
                CategoryDTO dtoInp = objectMapper.readValue(req.getReader(), CategoryDTO.class);
                if (dtoInp.categoryId() == 0) throw new RuntimeException("ID объекта не должен быть 0.");
                checkDtoAndSave(resp, dtoInp);
            } catch (JsonParseException | UnrecognizedPropertyException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Неправильный json! " + e.getMessage());
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Ошибка изменения категории. " + e.getMessage());
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(REQUEST_MESSAGE_WRONG);
        }
    }

    private void checkDtoAndSave(HttpServletResponse resp, CategoryDTO dtoInp) throws IOException {
        if (dtoInp.name() == null || dtoInp.name().isEmpty()) throw new RuntimeException("name не может быть пустым.");
        Category category = mapper.map(dtoInp);
        Category savedCategory = service.save(category);
        CategoryDTO dtoOut = mapper.map(savedCategory);
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
                if(service.delete(new Category(id,"",null))){
                    resp.getWriter().print("Категория удалена.");
                } else {
                    resp.getWriter().print("Категории не существует.");
                }
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print("Ошибка удаления категории. " + e.getMessage());
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(REQUEST_MESSAGE_WRONG);
        }
    }
}
