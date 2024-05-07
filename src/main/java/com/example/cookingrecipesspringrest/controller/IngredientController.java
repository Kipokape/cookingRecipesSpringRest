package com.example.cookingrecipesspringrest.controller;


import com.example.cookingrecipesspringrest.dto.IngredientDTO;
import com.example.cookingrecipesspringrest.dto.ResponseDTO;
import com.example.cookingrecipesspringrest.exception.ServiceException;
import com.example.cookingrecipesspringrest.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ingredient")
public class IngredientController {


    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping(path = "")
    public List<IngredientDTO> findAllIngredients() {
        return ingredientService.findAll();
    }

    @GetMapping("/{ingredientId}")
    public IngredientDTO findIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        if (ingredientId <= 0) {
            throw new ServiceException("Id должен быть больше 0!");
        }
        return ingredientService.findById(ingredientId);
    }

    @PostMapping("")
    public IngredientDTO saveNewIngredient(@RequestBody IngredientDTO dto) {
        if (dto.ingredientId() != 0) {
            throw new ServiceException("Id должен быть 0!");
        }
        return ingredientService.save(dto);
    }

    @PatchMapping("")
    public IngredientDTO updateExistIngredient(@RequestBody IngredientDTO dto) {
        if (dto.ingredientId() == 0) {
            throw new ServiceException("Id не должен быть 0!");
        }
        return ingredientService.save(dto);
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseDTO deleteIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        if (ingredientId <= 0) {
            throw new ServiceException("Id должен быть больше 0! " + ingredientId);
        }
        ingredientService.deleteById(ingredientId);
        return new ResponseDTO("Ингредиент успешно удален!");
    }


}
