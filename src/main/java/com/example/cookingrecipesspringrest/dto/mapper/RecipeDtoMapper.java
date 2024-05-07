package com.example.cookingrecipesspringrest.dto.mapper;

import com.example.cookingrecipesspringrest.dto.RecipeDTO;
import com.example.cookingrecipesspringrest.model.Recipe;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Named("RecipeDtoMapper")
@Mapper(componentModel = "spring", uses = {IngredientDtoMapper.class, CategoryDtoMapper.class})
public interface RecipeDtoMapper {

    RecipeDtoMapper INSTANCE = Mappers.getMapper(RecipeDtoMapper.class);

    @Named("toEntity")
    @Mapping(target = "ingredients", qualifiedByName = {"IngredientDtoMapper", "toEntity"})
    @InheritInverseConfiguration(name = "toDto")
    Recipe toEntity(RecipeDTO recipeDTO);

    @Named("toEntityList")
    @IterableMapping(qualifiedByName = "toEntity")
    List<Recipe> toListEntity(List<RecipeDTO> dtoList);

    @Named("toDto")
    @Mapping(target = "recipeId", source = "id")
    @Mapping(target = "category.recipes", ignore = true)
    @Mapping(target = "category.categoryId", source = "category.id")
    @Mapping(target = "ingredients", qualifiedByName = {"IngredientDtoMapper", "toDtoWithoutRecipes"})
    RecipeDTO toDto(Recipe recipe);

    @Named("toDtoList")
    @IterableMapping(qualifiedByName = "toDto")
    List<RecipeDTO> toListDto(List<Recipe> recipeList);

    @Named("toDtoWithoutIngredients")
    @Mapping(target = "recipeId", source = "id")
    @Mapping(target = "category.categoryId", source = "category.id")
    @Mapping(target = "category.recipes", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    RecipeDTO toDtoWithoutIngredients(Recipe recipe);

}
