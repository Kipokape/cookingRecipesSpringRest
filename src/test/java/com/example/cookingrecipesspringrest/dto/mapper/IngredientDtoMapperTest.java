package com.example.cookingrecipesspringrest.dto.mapper;

import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.dto.IngredientDTO;
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
class IngredientDtoMapperTest {


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
    IngredientDtoMapper ingredientDtoMapper;

    @Test
    void toDto_Ingredient_ReturnDto() {
        IngredientDTO dto = ingredientDtoMapper.toDto(testIngredientOne);

        Assertions.assertEquals(testIngredientOne.getId(), dto.ingredientId());
        Assertions.assertEquals(testIngredientOne.getName(), dto.name());
        Assertions.assertEquals(testIngredientOne.getRecipes().size(), dto.recipes().size());
    }

    @Test
    void toListDTO_ListIngredient_ReturnListDto() {
        List<Ingredient> listEntity = new ArrayList<>();
        listEntity.add(testIngredientOne);
        listEntity.add(testIngredientTwo);

        List<IngredientDTO> listDto = ingredientDtoMapper.toListDto(listEntity);

        Assertions.assertEquals(listEntity.get(1).getName(), listDto.get(1).name());
        Assertions.assertEquals(listEntity.size(), listDto.size());
        Assertions.assertEquals(listEntity.get(1).getName(), listDto.get(1).name());
    }

    @Test
    void toEntity_IngredientDto_ReturnIngredient() {
        IngredientDTO dto = ingredientDtoMapper.toDto(testIngredientOne);

        Ingredient ingredient = ingredientDtoMapper.toEntity(dto);

        Assertions.assertEquals(ingredient.getId(), dto.ingredientId());
        Assertions.assertEquals(ingredient.getName(), dto.name());
        Assertions.assertEquals(ingredient.getRecipes().size(), dto.recipes().size());
    }

    @Test
    void toListEntity_ListIngredientDTO_ReturnListIngredient() {
        IngredientDTO dtoOne = ingredientDtoMapper.toDto(testIngredientOne);
        IngredientDTO dtoTwo = ingredientDtoMapper.toDto(testIngredientOne);
        List<IngredientDTO> listDto = new ArrayList<>();
        listDto.add(dtoOne);
        listDto.add(dtoTwo);

        List<Ingredient> listEntity = ingredientDtoMapper.toListEntity(listDto);

        Assertions.assertEquals(listEntity.get(1).getName(), listDto.get(1).name());
        Assertions.assertEquals(listEntity.size(), listDto.size());
        Assertions.assertEquals(listEntity.get(1).getRecipes().size(), listDto.get(1).recipes().size());
    }

}