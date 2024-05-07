package com.example.cookingrecipesspringrest.controller;

import com.example.cookingrecipesspringrest.dto.CategoryDTO;
import com.example.cookingrecipesspringrest.dto.ResponseDTO;
import com.example.cookingrecipesspringrest.exception.ServiceException;
import com.example.cookingrecipesspringrest.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(path = "")
    public List<CategoryDTO> findAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/{categoryId}")
    public CategoryDTO findCategoryById(@PathVariable("categoryId") Long categoryId) {
        if (categoryId <= 0) {
            throw new ServiceException("Id должен быть больше 0!");
        }
        return categoryService.findById(categoryId);
    }

    @PostMapping("")
    public CategoryDTO saveNewCategory(@RequestBody CategoryDTO dto) {
        if (dto.categoryId() != 0) {
            throw new ServiceException("Id должен быть 0!");
        }
        return categoryService.save(dto);
    }

    @PatchMapping("")
    public CategoryDTO updateExistCategory(@RequestBody CategoryDTO dto) {
        if (dto.categoryId() == 0) {
            throw new ServiceException("Id не должен быть 0!");
        }
        return categoryService.save(dto);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseDTO deleteCategoryById(@PathVariable("categoryId") Long categoryId) {
        if (categoryId <= 0) {
            throw new ServiceException("Id должен быть больше 0! " + categoryId);
        }
        categoryService.deleteById(categoryId);
        return new ResponseDTO("Категория успешно удалена!");
    }

}
