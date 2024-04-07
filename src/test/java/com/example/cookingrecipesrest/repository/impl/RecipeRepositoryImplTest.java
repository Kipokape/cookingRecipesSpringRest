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

class RecipeRepositoryImplTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    private RecipeRepository repository;
    private CategoryRepository categoryRepository;

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
        ingredientRepository = new IngredientRepositoryImpl(connectionProvider, null);
        repository = new RecipeRepositoryImpl(connectionProvider, ingredientRepository);
        categoryRepository.save(new Category(0, "Первые блюда", null));
        categoryRepository.save(new Category(0, "Вторые блюда", null));
        ingredientRepository.save(new Ingredient(0, "Капуста", null));
        ingredientRepository.save(new Ingredient(0, "Картошка", null));
    }

    @AfterEach
    void tearDown() {
        repository.truncateTable();
        categoryRepository.truncateTable();
        ingredientRepository.truncateTable();
    }

    @Test
    void findById_ExistingId_ReturnRecipe() {
        Recipe expected;
        Recipe actual;
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "Капуста", null));
        ingredients.add(new Ingredient(2, "Картошка", null));

        expected = repository.save(new Recipe(0, 1,"Первые блюда", ingredients));
        actual = repository.findById(expected.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById_NoneExistingId_ThrowsRuntimeException() {
        Assertions.assertThrows(RuntimeException.class, () -> repository.findById(11L));
    }

    @Test
    void getRecipesByCategory_ExistCategory_ReturnRecipes() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "Капуста", null));
        ingredients.add(new Ingredient(2, "Картошка", null));
        repository.save(new Recipe(0,1,"Борщ", ingredients));
        repository.save(new Recipe(0,1,"Щи", null));
        Assertions.assertEquals(2, repository.getRecipesByCategory(1L).size());
    }

    @Test
    void getRecipesByCategory_NotExistCategory_ReturnNone() {
        Assertions.assertEquals(0, repository.getRecipesByCategory(11L).size());
    }

    @Test
    void getRecipesByIngredient_ExistIngredient_ReturnRecipes() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "Капуста", null));
        ingredients.add(new Ingredient(2, "Картошка", null));
        repository.save(new Recipe(0,1,"Борщ", ingredients));
        repository.save(new Recipe(0,1,"Щи", null));
        Assertions.assertEquals(1, repository.getRecipesByIngredient(1L).size());
    }

    @Test
    void getRecipesByIngredient_NotExistIngredient_ReturnNone() {
        Assertions.assertEquals(0, repository.getRecipesByIngredient(11L).size());
    }

    @Test
    void findAll_NotEmptyTable_ReturnRecipes() {
        List<Recipe> recipes;
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "Капуста", null));
        ingredients.add(new Ingredient(2, "Картошка", null));

        List<Ingredient> ingredientsSec = new ArrayList<>();
        ingredientsSec.add(new Ingredient(1, "Капуста", null));
        ingredientsSec.add(new Ingredient(2, "Картошка", null));

        repository.save(new Recipe(0, 1, "Щи", ingredients));
        repository.save(new Recipe(0, 2, "Борщ", ingredientsSec));
        recipes = repository.findALL();

        Assertions.assertEquals(2, recipes.size());
    }

    @Test
    void findAll_EmptyTable_ReturnEmptyList() {
        List<Recipe> recipes;
        recipes = repository.findALL();
        Assertions.assertEquals(0, recipes.size());
    }

    @Test
    void deleteById_ExistingId_ReturnTrue() {
        Recipe expected;
        List<Ingredient> ingredients= new ArrayList<>();
        ingredients.add(new Ingredient(1, "Капуста", null));
        ingredients.add(new Ingredient(2, "Картошка", null));
        expected = repository.save(new Recipe(0, 1, "Омлет", ingredients));
        Assertions.assertTrue(repository.deleteById(expected.getId()));
    }

    @Test
    void deleteById_NoneExistingId_ReturnFalse() {
        Assertions.assertFalse(repository.deleteById(11L));
    }



    @Test
    void save_NotExistRecipe_ReturnNewRecipe() {
        Recipe expected = new Recipe(1, 1, "Борщ", null);

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "Капуста", null));
        ingredients.add(new Ingredient(2, "Картошка", null));
        expected.setIngredients(ingredients);

        Recipe actual = new Recipe();
        actual.setName(expected.getName());
        actual.setIdCategory(expected.getIdCategory());
        actual.setIngredients(ingredients);
        repository.save(actual);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_ExistCategory_ReturnCategory() {
        Recipe expected = new Recipe(0, 1, "Борщ", null);

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "Капуста", null));
        ingredients.add(new Ingredient(2, "Картошка", null));
        expected.setIngredients(ingredients);
        repository.save(expected);

        Recipe actual = new Recipe();
        actual.setId(1);
        actual.setName(expected.getName());
        actual.setIdCategory(expected.getIdCategory());
        actual.setIngredients(ingredients);
        repository.save(actual);

        Assertions.assertEquals(expected, actual);
    }




}