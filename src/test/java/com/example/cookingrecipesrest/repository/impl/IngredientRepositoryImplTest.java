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
        recipeRepository = new RecipeRepositoryImpl(connectionProvider);
        repository = new IngredientRepositoryImpl(connectionProvider);
        categoryRepository.save(new Category(0, "Первые блюда", null));
        categoryRepository.save(new Category(0, "Вторые блюда", null));
        recipeRepository.save(new Recipe(0,1, "Щи", null));
        recipeRepository.save(new Recipe(0,1, "Борщ", null));
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
        List<RecipeIngredients> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(new RecipeIngredients(0, 1, 1, 100));
        recipeIngredients.add(new RecipeIngredients(0, 2, 1, 200));

        expected = repository.save(new Ingredient(0, "Картошка", recipeIngredients));
        actual = repository.findById(expected.getId());
        System.out.println(expected);
        System.out.println(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById_NoneExistingId_ThrowsRuntimeException() {
        Assertions.assertThrows(RuntimeException.class, () -> repository.findById(11L));
    }

    @Test
    void findAll_NotEmptyTable_ReturnIngredients() {
        List<Ingredient> ingredients;
        List<RecipeIngredients> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(new RecipeIngredients(0, 1, 1, 100));
        recipeIngredients.add(new RecipeIngredients(0, 2, 1, 200));

        List<RecipeIngredients> recipeIngredientsSec = new ArrayList<>();
        recipeIngredientsSec.add(new RecipeIngredients(0, 1, 2, 400));
        recipeIngredientsSec.add(new RecipeIngredients(0, 2, 2, 300));

        repository.save(new Ingredient(0,"Капуста", recipeIngredients));
        repository.save(new Ingredient(0,"Картошка", recipeIngredientsSec));
        ingredients = repository.findALL();

        System.out.println(ingredients);
        Assertions.assertEquals(2, ingredients.size());
    }

    @Test
    void findAll_EmptyTable_ReturnEmptyList() {
        List<Ingredient> ingredients;
        ingredients = repository.findALL();
        Assertions.assertEquals(0, ingredients.size());
    }

    @Test
    void deleteById_ExistingId_ReturnTrue() {
        Ingredient expected;
        List<RecipeIngredients> recipeIngredients= new ArrayList<>();
        recipeIngredients.add(new RecipeIngredients(0, 1, 1, 333));
        recipeIngredients.add(new RecipeIngredients(0, 2, 1, 444));
        expected = repository.save(new Ingredient(0, "Картошка", recipeIngredients));
        Assertions.assertTrue(repository.deleteById(expected.getId()));
    }

    @Test
    void deleteById_NoneExistingId_ReturnFalse() {
        Assertions.assertFalse(repository.deleteById(11L));
    }



    @Test
    void save_NotExistRecipe_ReturnNewIngredient() {
        Ingredient expected = new Ingredient(1, "Картошка", null);

        List<RecipeIngredients> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(new RecipeIngredients(0, 1, 1, 100));
        recipeIngredients.add(new RecipeIngredients(0, 2, 1, 200));
        expected.setRecipeIngredients(recipeIngredients);

        Ingredient actual = new Ingredient();
        actual.setName(expected.getName());
        actual.setRecipeIngredients(recipeIngredients);
        repository.save(actual);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_ExistCategory_ReturnCategory() {
        Ingredient expected = new Ingredient(0, "Капуста", null);

        List<RecipeIngredients> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(new RecipeIngredients(0, 1, 1, 100));
        recipeIngredients.add(new RecipeIngredients(0, 2, 1, 200));
        expected.setRecipeIngredients(recipeIngredients);
        repository.save(expected);

        Ingredient actual = new Ingredient();
        actual.setId(1);
        actual.setName(expected.getName());
        actual.setRecipeIngredients(recipeIngredients);
        repository.save(actual);

        Assertions.assertEquals(expected, actual);
    }






}