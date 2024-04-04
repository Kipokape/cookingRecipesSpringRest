package com.example.cookingrecipesrest.repository.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.db.ConnectionManagerTestImpl;
import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.model.Ingredient;
import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.model.RecipeIngredients;
import com.example.cookingrecipesrest.repository.CategoryRepository;
import com.example.cookingrecipesrest.repository.IngredientRepository;
import com.example.cookingrecipesrest.repository.RecipeIngredientsRepository;
import com.example.cookingrecipesrest.repository.RecipeRepository;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;


class RecipeIngredientsRepositoryImplTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    private RecipeIngredientsRepository repository;
    private CategoryRepository categoryRepository;
    private RecipeRepository recipeRepository;

    private IngredientRepository ingredientRepository;

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
        repository = new RecipeIngredientsRepositoryImpl(connectionProvider);
        ingredientRepository = new IngredientRepositoryImpl(connectionProvider);
        categoryRepository.save(new Category(0, "Первые блюда", null));
        categoryRepository.save(new Category(0, "Вторые блюда", null));
        recipeRepository.save(new Recipe(0,1, "Щи", null));
        recipeRepository.save(new Recipe(0,1, "Борщ", null));
        ingredientRepository.save(new Ingredient(0,"Капуста", null));
        ingredientRepository.save(new Ingredient(0,"Картошка", null));
    }

    @AfterEach
    void tearDown() {
        repository.truncateTable();
        categoryRepository.truncateTable();
        recipeRepository.truncateTable();
        ingredientRepository.truncateTable();
    }


    @Test
    void findById_ExistingId_ReturnRecipeIngredients() {
        RecipeIngredients expected;
        RecipeIngredients actual;
        expected = repository.save(new RecipeIngredients(0, 1,1, 333));
        actual = repository.findById(expected.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById_NoneExistingId_ThrowsRuntimeException() {
        Assertions.assertThrows(RuntimeException.class, () -> repository.findById(11L));
    }

    @Test
    void findAll_NotEmptyTable_ReturnIngredients() {
        List<RecipeIngredients> recipeIngredients;

        repository.save(new RecipeIngredients(0, 1,1, 333));
        repository.save(new RecipeIngredients(0, 1,2, 200));
        recipeIngredients = repository.findALL();

        Assertions.assertEquals(2, recipeIngredients.size());
    }

    @Test
    void findAll_EmptyTable_ReturnEmptyList() {
        List<RecipeIngredients> recipeIngredients;
        recipeIngredients = repository.findALL();
        Assertions.assertEquals(0, recipeIngredients.size());
    }

    @Test
    void getRecipeIngredientsByRecipe_ExistRecipe_ReturnRecipeIngredients() {
        repository.save(new RecipeIngredients(0, 1,1, 333));
        repository.save(new RecipeIngredients(0, 1,2, 444));
        Assertions.assertEquals(2, repository.getRecipeIngredientsByRecipe(1L).size());
    }

    @Test
    void getRecipeIngredientsByRecipe_NotExistRecipe_ReturnRecipeIngredients() {
        Assertions.assertEquals(0, repository.getRecipeIngredientsByRecipe(11L).size());
    }

    @Test
    void getRecipeIngredientsByIngredient_ExistIngredient_ReturnRecipeIngredients() {
        repository.save(new RecipeIngredients(0, 1,1, 333));
        repository.save(new RecipeIngredients(0, 2,1, 444));
        Assertions.assertEquals(2, repository.getRecipeIngredientsByIngredient(1L).size());
    }

    @Test
    void getRecipeIngredientsByIngredient_NotExistIngredient_ReturnRecipeIngredients() {
        Assertions.assertEquals(0, repository.getRecipeIngredientsByIngredient(11L).size());
    }

    @Test
    void deleteById_ExistingId_ReturnTrue() {
        RecipeIngredients expected;
        expected = repository.save(new RecipeIngredients(0, 1,2, 200));
        Assertions.assertTrue(repository.deleteById(expected.getId()));
    }

    @Test
    void deleteById_NoneExistingId_ReturnFalse() {
        Assertions.assertFalse(repository.deleteById(11L));
    }

    @Test
    void save_NotExistRecipe_ReturnNewRecipeIngredients() {
        RecipeIngredients expected = new RecipeIngredients(1, 1,2, 200);

        RecipeIngredients actual = new RecipeIngredients();
        actual.setRecipeId(expected.getRecipeId());
        actual.setIngredientId(expected.getIngredientId());
        actual.setWeight(expected.getWeight());
        repository.save(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_ExistCategory_ReturnRecipeIngredients() {
        RecipeIngredients expected = new RecipeIngredients(0, 1,2, 200);

        repository.save(expected);
        RecipeIngredients actual = new RecipeIngredients();
        actual.setId(1);
        actual.setRecipeId(expected.getRecipeId());
        actual.setIngredientId(expected.getIngredientId());
        actual.setWeight(expected.getWeight());
        repository.save(actual);
        Assertions.assertEquals(expected, actual);
    }




}