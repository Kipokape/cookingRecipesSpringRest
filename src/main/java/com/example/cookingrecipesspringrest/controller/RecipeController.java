package com.example.cookingrecipesspringrest.controller;

import com.example.cookingrecipesspringrest.dto.RecipeDTO;
import com.example.cookingrecipesspringrest.dto.ResponseDTO;
import com.example.cookingrecipesspringrest.exception.ServiceException;
import com.example.cookingrecipesspringrest.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping(path = "")
    public List<RecipeDTO> findAllRecipes() {
        return recipeService.findAll();
    }

    @GetMapping("/{recipeId}")
    public RecipeDTO findRecipeById(@PathVariable("recipeId") Long recipeId) {
        if (recipeId <= 0) {
            throw new ServiceException("Id должен быть больше 0!");
        }
        return recipeService.findById(recipeId);
    }

    @PostMapping("")
    public RecipeDTO saveNewRecipe(@RequestBody RecipeDTO dto) {
        if (dto.recipeId() != 0) {
            throw new ServiceException("Id должен быть 0!");
        }
        return recipeService.save(dto);
    }

    @PatchMapping("")
    public RecipeDTO updateExistRecipe(@RequestBody RecipeDTO dto) {
        if (dto.recipeId() == 0) {
            throw new ServiceException("Id не должен быть 0!");
        }
        return recipeService.save(dto);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseDTO deleteRecipeById(@PathVariable("recipeId") Long recipeId) {
        if (recipeId <= 0) {
            throw new ServiceException("Id должен быть больше 0! " + recipeId);
        }
        recipeService.deleteById(recipeId);
        return new ResponseDTO("Рецепт успешно удален!");
    }
}
