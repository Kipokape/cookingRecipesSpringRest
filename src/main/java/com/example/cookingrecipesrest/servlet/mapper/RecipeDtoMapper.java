package com.example.cookingrecipesrest.servlet.mapper;

import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.servlet.dto.RecipeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecipeDtoMapper {

    RecipeDtoMapper INSTANCE = Mappers.getMapper(RecipeDtoMapper.class);

    @Mapping(target = "id", source = "recipeId")
    Recipe map(RecipeDTO recipeDTO);

    @Mapping(target = "recipeId", source = "id")
    RecipeDTO map(Recipe recipe);
}
