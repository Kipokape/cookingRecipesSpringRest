package com.example.cookingrecipesrest.servlet.mapper;

import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.servlet.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryDtoMapper {

    CategoryDtoMapper INSTANCE = Mappers.getMapper(CategoryDtoMapper.class);

    @Mapping(target = "id", source = "categoryId")
    Category map(CategoryDTO categoryDTO);

    @Mapping(target = "categoryId", source = "id")
    CategoryDTO map(Category category);
}
