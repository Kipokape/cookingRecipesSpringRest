package com.example.cookingrecipesrest.repository.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.db.ConnectionManagerTestImpl;
import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.repository.CategoryRepository;
import com.example.cookingrecipesrest.repository.IngredientRepository;
import com.example.cookingrecipesrest.repository.RecipeRepository;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IngredientRepositoryImplTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    private IngredientRepository repository;
    private CategoryRepository categoryRepository;
    private RecipeRepository recipeRepository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        ConnectionManager connectionProvider = new ConnectionManagerTestImpl(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        categoryRepository = new CategoryRepositoryImpl(connectionProvider);
        recipeRepository = new RecipeRepositoryImpl(connectionProvider, null);
        repository = new IngredientRepositoryImpl(connectionProvider, recipeRepository);
        categoryRepository.save(new Category(0, "Первые блюда", null));
        categoryRepository.save(new Category(0, "Вторые блюда", null));
        recipeRepository.save(new Recipe(0, 1, "Щи", null));
        recipeRepository.save(new Recipe(0, 1, "Борщ", null));
    }

    @AfterEach
    void tearDown() {
        repository.truncateTable();
        categoryRepository.truncateTable();
        recipeRepository.truncateTable();
    }


    @Test
    void findById_ExistingId_ReturnIngredient() {
        Ingredient expected;
        Ingredient actual;
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(1, 1, "Щи", null));
        recipes.add(new Recipe(2, 1, "Борщ", null));

        expected = repository.save(new Ingredient(0, "Картошка", recipes));
        actual = repository.findById(expected.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById_NoneExistingId_ThrowsRuntimeException() {
        Assertions.assertThrows(RuntimeException.class, () -> repository.findById(11L));
    }

    @Test
    void findAll_NotEmptyTable_ReturnIngredients() {
        List<Ingredient> ingredients;
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(2, 1, "Борщ", null));
        recipes.add(new Recipe(1, 1, "Щи", null));

        List<Recipe> recipesSec = new ArrayList<>();
        recipesSec.add(new Recipe(2, 1, "Борщ", null));
        recipesSec.add(new Recipe(1, 1, "Щи", null));

        repository.save(new Ingredient(0, "Капуста", recipes));
        repository.save(new Ingredient(0, "Картошка", recipesSec));
        ingredients = repository.findALL();

        Assertions.assertEquals(2, ingredients.size());
    }

    @Test
    void findAll_EmptyTable_ReturnEmptyList() {
        List<Ingredient> ingredients;
        ingredients = repository.findALL();
        Assertions.assertEquals(0, ingredients.size());
    }

    @Test
    void getIngredientsByRecipe_ExistRecipe_ReturnIngredients() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(1, 1, "Щи", null));
        recipes.add(new Recipe(2, 1, "Борщ", null));


        repository.save(new Ingredient(0, "Капуста", recipes));
        repository.save(new Ingredient(0,"Картошка", recipes));
        Assertions.assertEquals(2, repository.getIngredientsByRecipe(1L).size());
    }

    @Test
    void getIngredientsByRecipe_NotExistRecipe_ReturnNone() {
        Assertions.assertEquals(0, repository.getIngredientsByRecipe(11L).size());
    }


    @Test
    void deleteById_ExistingId_ReturnTrue() {
        Ingredient expected;
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(2, 1, "Борщ", null));
        recipes.add(new Recipe(1, 1, "Щи", null));
        expected = repository.save(new Ingredient(0, "Картошка", recipes));
        Assertions.assertTrue(repository.deleteById(expected.getId()));
    }

    @Test
    void deleteById_NoneExistingId_ReturnFalse() {
        Assertions.assertFalse(repository.deleteById(11L));
    }


    @Test
    void save_NotExistIngredient_ReturnNewIngredient() {
        Ingredient expected = new Ingredient(1, "Картошка", null);

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(2, 1, "Борщ", null));
        recipes.add(new Recipe(1, 1, "Щи", null));
        expected.setRecipes(recipes);

        Ingredient actual = new Ingredient();
        actual.setName(expected.getName());
        actual.setRecipes(recipes);
        repository.save(actual);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_ExistIngredient_ReturnIngredient() {
        Ingredient expected = new Ingredient(0, "Капуста", null);

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(2, 1, "Борщ", null));
        recipes.add(new Recipe(1, 1, "Щи", null));
        expected.setRecipes(recipes);
        repository.save(expected);

        Ingredient actual = new Ingredient();
        actual.setId(1);
        actual.setName(expected.getName());
        actual.setRecipes(recipes);
        repository.save(actual);

        Assertions.assertEquals(expected, actual);
    }


}