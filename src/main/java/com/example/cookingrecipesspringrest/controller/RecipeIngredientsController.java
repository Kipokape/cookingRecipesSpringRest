package com.example.cookingrecipesspringrest.controller;


import com.example.cookingrecipesspringrest.dto.RecipeIngredientsDTO;
import com.example.cookingrecipesspringrest.dto.ResponseDTO;
import com.example.cookingrecipesspringrest.exception.ServiceException;
import com.example.cookingrecipesspringrest.service.RecipeIngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/recipe-ingredient")
public class RecipeIngredientsController {

    private final RecipeIngredientsService recipeIngredientsService;

    @Autowired
    public RecipeIngredientsController(RecipeIngredientsService recipeIngredientsService) {
        this.recipeIngredientsService = recipeIngredientsService;
    }

    @GetMapping(path = "")
    public List<RecipeIngredientsDTO> findAllRecipeIngredients() {
        return recipeIngredientsService.findAll();
    }

    @GetMapping("/{recipeIngredientsId}")
    public RecipeIngredientsDTO findRecipeIngredientsById(@PathVariable("recipeIngredientsId") Long recipeIngredientsId) {
        if (recipeIngredientsId <= 0) {
            throw new ServiceException("Id должен быть больше 0!");
        }
        return recipeIngredientsService.findById(recipeIngredientsId);
    }

    @PostMapping("")
    public RecipeIngredientsDTO saveNewRecipeIngredients(@RequestBody RecipeIngredientsDTO dto) {
        if (dto.recipeIngredientsId() != 0) {
            throw new ServiceException("Id должен быть 0!");
        }
        return recipeIngredientsService.save(dto);
    }

    @PatchMapping("")
    public RecipeIngredientsDTO updateExistRecipeIngredients(@RequestBody RecipeIngredientsDTO dto) {
        if (dto.recipeIngredientsId() == 0) {
            throw new ServiceException("Id не должен быть 0!");
        }
        return recipeIngredientsService.save(dto);
    }

    @DeleteMapping("/{recipeIngredientsId}")
    public ResponseDTO deleteRecipeIngredientsById(@PathVariable("recipeIngredientsId") Long recipeIngredientsId) {
        if (recipeIngredientsId <= 0) {
            throw new ServiceException("Id должен быть больше 0! " + recipeIngredientsId);
        }
        recipeIngredientsService.deleteById(recipeIngredientsId);
        return new ResponseDTO("Ингредиент рецепта успешно удален!");
    }
}
