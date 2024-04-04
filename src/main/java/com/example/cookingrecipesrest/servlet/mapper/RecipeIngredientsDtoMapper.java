package com.example.cookingrecipesrest.servlet.mapper;

import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.servlet.dto.RecipeIngredientsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecipeIngredientsDtoMapper {

    RecipeIngredientsDtoMapper INSTANCE = Mappers.getMapper(RecipeIngredientsDtoMapper.class);

    @Mapping(target = "id", source = "recipeIngredientsId")
    RecipeIngredients map(RecipeIngredientsDTO recipeIngredientsDTO);

    @Mapping(target = "recipeIngredientsId", source = "id")
    RecipeIngredientsDTO map(RecipeIngredients recipeIngredients);

}
