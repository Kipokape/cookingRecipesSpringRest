package com.example.cookingrecipesrest.servlet.mapper;

import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.servlet.dto.CategoryDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDtoMapperTest {


    @Test
    void map_Category_ReturnDto() {
        Category category = new Category(1L,"тест", null);

        CategoryDTO dto = CategoryDtoMapper.INSTANCE.map(category);

        Assertions.assertEquals(category.getId(), dto.categoryId());
        Assertions.assertEquals(category.getName(), dto.name());
        Assertions.assertEquals(category.getRecipes(), dto.recipes());
    }

    @Test
    void map_CategoryDto_ReturnCategory() {
        CategoryDTO dto = new CategoryDTO(1l, "тест", null);

        Category category = CategoryDtoMapper.INSTANCE.map(dto);

        Assertions.assertEquals(category.getId(), dto.categoryId());
        Assertions.assertEquals(category.getName(), dto.name());
        Assertions.assertEquals(category.getRecipes(), dto.recipes());
    }
}