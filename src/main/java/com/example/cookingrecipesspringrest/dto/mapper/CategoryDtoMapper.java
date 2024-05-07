package com.example.cookingrecipesspringrest.dto.mapper;

import com.example.cookingrecipesspringrest.dto.CategoryDTO;
import com.example.cookingrecipesspringrest.model.Category;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Named("CategoryDtoMapper")
@Mapper(componentModel = "spring", uses = RecipeDtoMapper.class)
public interface CategoryDtoMapper {

    CategoryDtoMapper INSTANCE = Mappers.getMapper(CategoryDtoMapper.class);

    @Named("toEntity")
    @Mapping(target = "recipes", qualifiedByName = {"RecipeDtoMapper", "toEntity"})
    @InheritInverseConfiguration(name = "toDto")
    Category toEntity(CategoryDTO dto);

    @Named("toListEntity")
    @IterableMapping(qualifiedByName = "toEntity")
    List<Category> toListEntity(List<CategoryDTO> dtoList);

    @Named("toDto")
    @Mapping(target = "categoryId", source = "id")
    @Mapping(target = "recipes", qualifiedByName = {"RecipeDtoMapper", "toDto"})
    CategoryDTO toDto(Category category);

    @Named("toListDto")
    @IterableMapping(qualifiedByName = "toDto")
    List<CategoryDTO> toListDto(List<Category> categoryList);
}
