package com.example.cookingrecipesrest.service.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.db.ConnectionManagerImpl;
import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.repository.CategoryRepository;
import com.example.cookingrecipesrest.repository.impl.CategoryRepositoryImpl;
import com.example.cookingrecipesrest.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    private final ConnectionManager connectionManager;


    public CategoryServiceImpl() {
        connectionManager = new ConnectionManagerImpl();
        categoryRepository = new CategoryRepositoryImpl(connectionManager);
    }

    public CategoryServiceImpl(CategoryRepository categoryRepository, ConnectionManager connectionManager) {
        this.categoryRepository = categoryRepository;
        this.connectionManager = connectionManager;
    }

    @Override
    public Category save(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка сохранения категории. " + e.getMessage());
        }
    }

    @Override
    public Category findById(Long id) {
        try {
            return categoryRepository.findById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка чтения данных. " + e.getMessage());
        }
    }

    @Override
    public List<Category> findAll() {
        try {
            return categoryRepository.findALL();
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка чтения данных. " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Category category) {
        try {
            return categoryRepository.deleteById(category.getId());
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка удаления. " + e.getMessage());
        }
    }
}
