package com.example.cookingrecipesspringrest.dto.mapper;

import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.dto.RecipeDTO;
import com.example.cookingrecipesspringrest.model.Category;
import com.example.cookingrecipesspringrest.model.Ingredient;
import com.example.cookingrecipesspringrest.model.Recipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringJUnitConfig
@SpringJUnitWebConfig
@ContextConfiguration(classes = SpringConfig.class)
@TestPropertySource("classpath:test-db.properties")
class RecipeDtoMapperTest {

    private final Category testCategory = new Category(1L, "первая", null);

    private final Recipe testRecipeOne = new Recipe(1L, testCategory, "рецепт 1", null);

    private final Recipe testRecipeTwo = new Recipe(2L, testCategory, "рецепт 2", null);

    private final Ingredient testIngredientOne = new Ingredient(1L, "ингредиент 1", null);

    private final Ingredient testIngredientTwo = new Ingredient(2L, "ингредиент 2", null);

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
    RecipeDtoMapper recipeDtoMapper;

    @Test
    void toDto_Recipe_ReturnDto() {
        RecipeDTO dto = recipeDtoMapper.toDto(testRecipeOne);

        Assertions.assertEquals(testRecipeOne.getId(), dto.recipeId());
        Assertions.assertEquals(testRecipeOne.getName(), dto.name());
        Assertions.assertEquals(testRecipeOne.getIngredients().size(), dto.ingredients().size());
    }

    @Test
    void toListDTO_ListRecipe_ReturnListDto() {
        List<Recipe> listEntity = new ArrayList<>();
        listEntity.add(testRecipeOne);
        listEntity.add(testRecipeTwo);

        List<RecipeDTO> listDto = recipeDtoMapper.toListDto(listEntity);

        Assertions.assertEquals(listEntity.get(1).getName(), listDto.get(1).name());
        Assertions.assertEquals(listEntity.size(), listDto.size());
        Assertions.assertEquals(listEntity.get(1).getName(), listDto.get(1).name());
    }

    @Test
    void toEntity_RecipeDto_ReturnRecipe() {
        RecipeDTO dto = recipeDtoMapper.toDto(testRecipeOne);

        Recipe entity = recipeDtoMapper.toEntity(dto);

        Assertions.assertEquals(entity.getId(), dto.recipeId());
        Assertions.assertEquals(entity.getName(), dto.name());
        Assertions.assertEquals(entity.getIngredients().size(), dto.ingredients().size());
    }

    @Test
    void toListEntity_ListRecipeDTO_ReturnListRecipe() {
        RecipeDTO dtoOne = recipeDtoMapper.toDto(testRecipeOne);
        RecipeDTO dtoTwo = recipeDtoMapper.toDto(testRecipeTwo);
        List<RecipeDTO> listDto = new ArrayList<>();
        listDto.add(dtoOne);
        listDto.add(dtoTwo);

        List<Recipe> listEntity = recipeDtoMapper.toListEntity(listDto);

        Assertions.assertEquals(listEntity.get(1).getName(), listDto.get(1).name());
        Assertions.assertEquals(listEntity.size(), listDto.size());
        Assertions.assertEquals(listEntity.get(1).getIngredients().size(), listDto.get(1).ingredients().size());
    }
}