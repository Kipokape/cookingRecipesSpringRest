package com.example.cookingrecipesrest.servlet.mapper;


import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.servlet.dto.IngredientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IngredientDtoMapper {

    IngredientDtoMapper INSTANCE = Mappers.getMapper(IngredientDtoMapper.class);

    @Mapping(target = "id", source = "ingredientId")
    Ingredient map(IngredientDTO ingredientDTO);

    @Mapping(target = "ingredientId", source = "id")
    IngredientDTO map(Ingredient ingredient);
}
