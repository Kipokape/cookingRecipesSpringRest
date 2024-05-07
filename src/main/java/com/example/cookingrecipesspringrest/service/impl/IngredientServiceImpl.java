package com.example.cookingrecipesspringrest.service.impl;


import com.example.cookingrecipesspringrest.ValidationUtils;
import com.example.cookingrecipesspringrest.dto.IngredientDTO;
import com.example.cookingrecipesspringrest.dto.mapper.IngredientDtoMapper;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.model.Ingredient;
import com.example.cookingrecipesspringrest.repository.IngredientRepository;
import com.example.cookingrecipesspringrest.repository.RecipeIngredientsRepository;
import com.example.cookingrecipesspringrest.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {


    private final IngredientRepository ingredientRepository;

    private final RecipeIngredientsRepository recipeIngredientsRepository;

    private final IngredientDtoMapper ingredientDtoMapper;

    private final ValidationUtils validationUtils;

    @Autowired
    public IngredientServiceImpl(
            IngredientRepository ingredientRepository,
            RecipeIngredientsRepository recipeIngredientsRepository,
            IngredientDtoMapper ingredientDtoMapper,
            ValidationUtils validationUtils
    ) {
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientsRepository = recipeIngredientsRepository;
        this.ingredientDtoMapper = ingredientDtoMapper;
        this.validationUtils = validationUtils;
    }


    @Override
    public IngredientDTO save(IngredientDTO entityDTO) {
        try {
            validationUtils.validation(entityDTO);
            IngredientDTO returnDto;
            Ingredient ingredient = ingredientRepository.save(ingredientDtoMapper.toEntity(entityDTO));
            if (entityDTO.ingredientId() == 0) ingredient.setRecipes(null);
            returnDto = ingredientDtoMapper.toDto(ingredient);
            return returnDto;

        } catch (DataIntegrityViolationException e) {
            throw new RepositoryException("Невозможно сохранить ингредиент!");
        }
    }

    @Override
    public IngredientDTO findById(Long id) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);

        IngredientDTO ingredientDTO;
        if (optionalIngredient.isPresent()) {
            ingredientDTO = ingredientDtoMapper.toDto(optionalIngredient.get());
        } else {
            throw new RepositoryException("Ингредиента с таким id не существует: " + id);
        }
        return ingredientDTO;
    }

    @Override
    public List<IngredientDTO> findAll() {
        return ingredientDtoMapper.toListDto(ingredientRepository.findAll())
                .stream()
                .sorted(Comparator.comparingLong(IngredientDTO::ingredientId))
                .toList();
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        findById(id);
        recipeIngredientsRepository
                .deleteAllInBatch(recipeIngredientsRepository.findRecipeIngredientsByIngredientIdOrderById(id));
        ingredientRepository.deleteById(id);
        return true;
    }


}
