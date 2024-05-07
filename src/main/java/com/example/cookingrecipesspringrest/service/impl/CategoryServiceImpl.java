package com.example.cookingrecipesspringrest.service.impl;


import com.example.cookingrecipesspringrest.ValidationUtils;
import com.example.cookingrecipesspringrest.dto.CategoryDTO;
import com.example.cookingrecipesspringrest.dto.mapper.CategoryDtoMapper;
import com.example.cookingrecipesspringrest.exception.RepositoryException;
import com.example.cookingrecipesspringrest.model.Category;
import com.example.cookingrecipesspringrest.repository.CategoryRepository;
import com.example.cookingrecipesspringrest.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements com.example.cookingrecipesspringrest.service.CategoryService {

    private final CategoryRepository categoryRepository;

    private final RecipeRepository recipeRepository;

    private final CategoryDtoMapper categoryDtoMapper;

    private final ValidationUtils validationUtils;


    @Autowired
    public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            RecipeRepository recipeRepository,
            CategoryDtoMapper categoryDtoMapper,
            ValidationUtils validationUtils
    ) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.categoryDtoMapper = categoryDtoMapper;
        this.validationUtils = validationUtils;
    }

    @Override
    public CategoryDTO save(CategoryDTO entityDTO) {
        try {
            validationUtils.validation(entityDTO);
            CategoryDTO returnDto;
            Category category = categoryRepository.save(categoryDtoMapper.toEntity(entityDTO));
            if (entityDTO.categoryId() == 0) category.setRecipes(null);
            returnDto = categoryDtoMapper.toDto(category);
            return returnDto;

        } catch (DataIntegrityViolationException e) {
            throw new RepositoryException("Невозможно сохранить категорию!");
        }
    }

    @Override
    public CategoryDTO findById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        CategoryDTO categoryDTO;
        if (optionalCategory.isPresent()) {
            categoryDTO = categoryDtoMapper.toDto(optionalCategory.get());
        } else {
            throw new RepositoryException("Категории с таким id не существует: " + id);
        }
        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryDtoMapper.toListDto(categoryRepository.findAll())
                .stream()
                .sorted(Comparator.comparingLong(CategoryDTO::categoryId))
                .toList();
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        findById(id);
        recipeRepository.deleteAllInBatch(recipeRepository.findByCategoryIdOrderById(id));
        categoryRepository.deleteById(id);
        return true;
    }
}
