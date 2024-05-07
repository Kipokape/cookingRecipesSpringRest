package com.example.cookingrecipesspringrest.dto.mapper;


import com.example.cookingrecipesspringrest.dto.IngredientDTO;
import com.example.cookingrecipesspringrest.model.Ingredient;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Named("IngredientDtoMapper")
@Mapper(componentModel = "spring", uses = {RecipeDtoMapper.class})
public interface IngredientDtoMapper {

    IngredientDtoMapper INSTANCE = Mappers.getMapper(IngredientDtoMapper.class);

    @Named("toEntity")
    //@Mapping(target = "id", source = "ingredientId")
    @Mapping(target = "recipes", qualifiedByName = {"RecipeDtoMapper", "toEntity"})
    @InheritInverseConfiguration(name = "toDto")
    Ingredient toEntity(IngredientDTO ingredientDTO);

    @Named("toListEntity")
    @IterableMapping(qualifiedByName = "toEntity")
    List<Ingredient> toListEntity(List<IngredientDTO> dtoList);

    @Named("toDto")
    @Mapping(target = "ingredientId", source = "id")
    @Mapping(target = "recipes", qualifiedByName = {"RecipeDtoMapper", "toDtoWithoutIngredients"})
    IngredientDTO toDto(Ingredient ingredient);

    @Named("toListDto")
    @IterableMapping(qualifiedByName = "toDto")
    List<IngredientDTO> toListDto(List<Ingredient> ingredientList);

    @Named("toDtoWithoutRecipes")
    @Mapping(target = "ingredientId", source = "id")
    @Mapping(target = "recipes", ignore = true)
    IngredientDTO toDtoWithoutRecipes(Ingredient ingredient);

    @Named("toRecipesWithoutRecipesDto")
    @InheritInverseConfiguration(name = "toDtoWithoutRecipes")
    Ingredient toRecipesWithoutRecipesDto(IngredientDTO dto);
}
