package com.example.cookingrecipesspringrest.service.impl;

import com.example.cookingrecipesspringrest.ValidationUtils;
import com.example.cookingrecipesspringrest.dto.RecipeIngredientsDTO;
import com.example.cookingrecipesspringrest.dto.mapper.RecipeIngredientsDtoMapper;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.model.RecipeIngredients;
import com.example.cookingrecipesspringrest.repository.RecipeIngredientsRepository;
import com.example.cookingrecipesspringrest.service.RecipeIngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeIngredientsServiceImpl implements RecipeIngredientsService {

    private final RecipeIngredientsRepository recipeIngredientsRepository;

    private final RecipeIngredientsDtoMapper recipeIngredientsDtoMapper;

    private final ValidationUtils validationUtils;

    @Autowired
    public RecipeIngredientsServiceImpl(
            RecipeIngredientsRepository recipeIngredientsRepository,
            RecipeIngredientsDtoMapper recipeIngredientsDtoMapper,
            ValidationUtils validationUtils
    ) {
        this.recipeIngredientsRepository = recipeIngredientsRepository;
        this.recipeIngredientsDtoMapper = recipeIngredientsDtoMapper;
        this.validationUtils = validationUtils;
    }


    @Override
    public RecipeIngredientsDTO save(RecipeIngredientsDTO entityDTO) {
        try {
            validationUtils.validation(entityDTO);
            RecipeIngredientsDTO returnDto;
            RecipeIngredients recipeIngredients = recipeIngredientsRepository.save(recipeIngredientsDtoMapper.toEntity(entityDTO));
            if (entityDTO.recipeIngredientsId() == 0) {
                recipeIngredients.getRecipe().setIngredients(null);
                recipeIngredients.getIngredient().setRecipes(null);
            }
            returnDto = recipeIngredientsDtoMapper.toDto(recipeIngredients);
            return returnDto;

        } catch (DataIntegrityViolationException e) {
            throw new RepositoryException("Невозможно сохранить ингредиент рецепта!");
        }
    }

    @Override
    public RecipeIngredientsDTO findById(Long id) {
        Optional<RecipeIngredients> optionalIngredient = recipeIngredientsRepository.findById(id);

        RecipeIngredientsDTO recipeIngredientsDTO;
        if (optionalIngredient.isPresent()) {
            recipeIngredientsDTO = recipeIngredientsDtoMapper.toDto(optionalIngredient.get());
        } else {
            throw new RepositoryException("Ингредиента рецепта с таким id не существует: " + id);
        }
        return recipeIngredientsDTO;
    }

    @Override
    public List<RecipeIngredientsDTO> findAll() {
        return recipeIngredientsDtoMapper.toListDto(recipeIngredientsRepository.findAll())
                .stream()
                .sorted(Comparator.comparingLong(RecipeIngredientsDTO::recipeIngredientsId))
                .toList();
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        findById(id);
        recipeIngredientsRepository.deleteById(id);
        return true;
    }
}
