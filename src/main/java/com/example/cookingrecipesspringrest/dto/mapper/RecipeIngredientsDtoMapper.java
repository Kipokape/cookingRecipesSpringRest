package com.example.cookingrecipesspringrest.dto.mapper;

import com.example.cookingrecipesspringrest.dto.RecipeIngredientsDTO;
import com.example.cookingrecipesspringrest.model.RecipeIngredients;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RecipeDtoMapper.class, IngredientDtoMapper.class})
public interface RecipeIngredientsDtoMapper {

    RecipeIngredientsDtoMapper INSTANCE = Mappers.getMapper(RecipeIngredientsDtoMapper.class);

    @Named("toEntity")
    @Mapping(target = "ingredient", qualifiedByName = {"IngredientDtoMapper", "toEntity"})
    @Mapping(target = "recipe", qualifiedByName = {"RecipeDtoMapper", "toEntity"})
    @InheritInverseConfiguration(name = "toDto")
    RecipeIngredients toEntity(RecipeIngredientsDTO dto);

    @Named("toEntityList")
    @IterableMapping(qualifiedByName = "toEntity")
    List<RecipeIngredients> toListEntity(List<RecipeIngredientsDTO> dtoList);

    @Named("toDto")
    @Mapping(target = "recipeIngredientsId", source = "id")
    @Mapping(target = "ingredient", qualifiedByName = {"IngredientDtoMapper", "toDtoWithoutRecipes"})
    @Mapping(target = "recipe", qualifiedByName = {"RecipeDtoMapper", "toDtoWithoutIngredients"})
    RecipeIngredientsDTO toDto(RecipeIngredients recipeIngredients);

    @Named("toDtoList")
    @IterableMapping(qualifiedByName = "toDto")
    List<RecipeIngredientsDTO> toListDto(List<RecipeIngredients> recipeList);


}
