package com.example.cookingrecipesspringrest.service.impl;

import com.example.cookingrecipesspringrest.ValidationUtils;
import com.example.cookingrecipesspringrest.dto.RecipeDTO;
import com.example.cookingrecipesspringrest.dto.mapper.RecipeDtoMapper;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.model.Category;
import com.example.cookingrecipesspringrest.model.Recipe;
import com.example.cookingrecipesspringrest.repository.CategoryRepository;
import com.example.cookingrecipesspringrest.repository.RecipeRepository;
import com.example.cookingrecipesspringrest.service.RecipeService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeDtoMapper recipeDtoMapper;

    private final ValidationUtils validationUtils;

    private final CategoryRepository categoryRepository;


    public RecipeServiceImpl(
            RecipeRepository recipeRepository,
            RecipeDtoMapper recipeDtoMapper,
            ValidationUtils validationUtils,
            CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeDtoMapper = recipeDtoMapper;
        this.validationUtils = validationUtils;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public RecipeDTO save(RecipeDTO entityDTO) {
        try {
            validationUtils.validation(entityDTO);
            RecipeDTO returnDto;
            Optional<Category> category = categoryRepository.findById(entityDTO.category().categoryId());
            Recipe recipe = recipeDtoMapper.toEntity(entityDTO);
            if (category.isPresent()) {
                recipe.setCategory(category.get());
                recipeRepository.save(recipe);
            } else {
                throw new RepositoryException("Категории с таким id не существует: " + entityDTO.category().categoryId());
            }
            if (entityDTO.recipeId() == 0) {
                recipe.setIngredients(null);
            }
            returnDto = recipeDtoMapper.toDto(recipe);
            return returnDto;

        } catch (DataIntegrityViolationException e) {
            throw new RepositoryException("Невозможно сохранить рецепт!");
        }
    }

    @Override
    public RecipeDTO findById(Long id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);

        RecipeDTO recipeDTO;
        if (optionalRecipe.isPresent()) {
            recipeDTO = recipeDtoMapper.toDto(optionalRecipe.get());
        } else {
            throw new RepositoryException("Рецепта с таким id не существует: " + id);
        }
        return recipeDTO;
    }

    @Override
    public List<RecipeDTO> findAll() {
        return recipeDtoMapper.toListDto(recipeRepository.findAll())
                .stream()
                .sorted(Comparator.comparingLong(RecipeDTO::recipeId))
                .toList();
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        findById(id);
        recipeRepository.deleteById(id);
        return true;
    }
}
