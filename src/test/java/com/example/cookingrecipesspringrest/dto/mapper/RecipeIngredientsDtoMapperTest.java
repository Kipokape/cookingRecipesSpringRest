package com.example.cookingrecipesspringrest.dto.mapper;

import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.dto.RecipeIngredientsDTO;
import com.example.cookingrecipesspringrest.model.Category;
import com.example.cookingrecipesspringrest.model.Ingredient;
import com.example.cookingrecipesspringrest.model.Recipe;
import com.example.cookingrecipesspringrest.model.RecipeIngredients;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringJUnitConfig
@SpringJUnitWebConfig
@ContextConfiguration(classes = SpringConfig.class)
class RecipeIngredientsDtoMapperTest {


    private final Category testCategory = new Category(1L, "первая", null);

    private final Recipe testRecipeOne = new Recipe(1L, testCategory, "рецепт 1", null);

    private final Recipe testRecipeTwo = new Recipe(2L, testCategory, "рецепт 2", null);

    private final Ingredient testIngredientOne = new Ingredient(1L, "ингредиент 1", null);

    private final Ingredient testIngredientTwo = new Ingredient(2L, "ингредиент 2", null);

    private final RecipeIngredients testRecipeIngredientsOne = new RecipeIngredients(1L, testRecipeOne, testIngredientOne, 100);

    private final RecipeIngredients testRecipeIngredientsTwo = new RecipeIngredients(2L, testRecipeTwo, testIngredientTwo, 200);

    private final RecipeIngredients testRecipeIngredientsThree = new RecipeIngredients(3L, testRecipeOne, testIngredientTwo, 300);

    private final RecipeIngredients testRecipeIngredientsFour = new RecipeIngredients(4L, testRecipeTwo, testIngredientOne, 400);

    @BeforeEach
    public void setUp() throws Exception {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(testRecipeOne);
        recipes.add(testRecipeTwo);
        testCategory.setRecipes(recipes);
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(testIngredientOne);
        ingredients.add(testIngredientTwo);
        testIngredientOne.setRecipes(recipes);
        testIngredientTwo.setRecipes(recipes);
        testRecipeOne.setIngredients(ingredients);
        testRecipeTwo.setIngredients(ingredients);
    }

    @Autowired
    RecipeIngredientsDtoMapper recipeIngredientsDtoMapper;

    @Test
    void toDto_RecipeIngredients_ReturnDto() {
        RecipeIngredientsDTO dto = recipeIngredientsDtoMapper.toDto(testRecipeIngredientsOne);

        Assertions.assertEquals(testRecipeIngredientsOne.getId(), dto.recipeIngredientsId());
        Assertions.assertEquals(testRecipeIngredientsOne.getIngredient().getName(), dto.ingredient().name());
        Assertions.assertEquals(testRecipeIngredientsOne.getRecipe().getName(), dto.recipe().name());
    }

    @Test
    void toListDTO_ListRecipeIngredients_ReturnListDto() {
        List<RecipeIngredients> listEntity = new ArrayList<>();
        listEntity.add(testRecipeIngredientsOne);
        listEntity.add(testRecipeIngredientsTwo);
        listEntity.add(testRecipeIngredientsThree);
        listEntity.add(testRecipeIngredientsFour);

        List<RecipeIngredientsDTO> listDto = recipeIngredientsDtoMapper.toListDto(listEntity);

        Assertions.assertEquals(listEntity.get(1).getId(), listDto.get(1).recipeIngredientsId());
        Assertions.assertEquals(listEntity.size(), listDto.size());
        Assertions.assertEquals(listEntity.get(1).getRecipe().getName(), listDto.get(1).recipe().name());
    }

    @Test
    void toEntity_RecipeIngredientsDto_ReturnRecipeIngredients() {
        RecipeIngredientsDTO dto = recipeIngredientsDtoMapper.toDto(testRecipeIngredientsOne);

        RecipeIngredients entity = recipeIngredientsDtoMapper.toEntity(dto);

        Assertions.assertEquals(entity.getId(), dto.recipeIngredientsId());
        Assertions.assertEquals(entity.getRecipe().getName(), dto.recipe().name());
        Assertions.assertEquals(entity.getIngredient().getName(), dto.ingredient().name());
    }

    @Test
    void toListEntity_ListRecipeIngredientsDTO_ReturnListRecipeIngredients() {
        RecipeIngredientsDTO dtoOne = recipeIngredientsDtoMapper.toDto(testRecipeIngredientsOne);
        RecipeIngredientsDTO dtoTwo = recipeIngredientsDtoMapper.toDto(testRecipeIngredientsTwo);
        RecipeIngredientsDTO dtoThree = recipeIngredientsDtoMapper.toDto(testRecipeIngredientsThree);
        RecipeIngredientsDTO dtoFour = recipeIngredientsDtoMapper.toDto(testRecipeIngredientsFour);
        List<RecipeIngredientsDTO> listDto = new ArrayList<>();
        listDto.add(dtoOne);
        listDto.add(dtoTwo);
        listDto.add(dtoThree);
        listDto.add(dtoFour);

        List<RecipeIngredients> listEntity = recipeIngredientsDtoMapper.toListEntity(listDto);

        Assertions.assertEquals(listEntity.get(1).getId(), listDto.get(1).recipeIngredientsId());
        Assertions.assertEquals(listEntity.size(), listDto.size());
        Assertions.assertEquals(listEntity.get(1).getIngredient().getName(), listDto.get(1).ingredient().name());
        Assertions.assertEquals(listEntity.get(1).getRecipe().getName(), listDto.get(1).recipe().name());
    }


}