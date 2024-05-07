package com.example.cookingrecipesspringrest.repository;

import com.example.cookingrecipesspringrest.config.DatabaseConfig;
import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.model.Category;
import com.example.cookingrecipesspringrest.model.Ingredient;
import com.example.cookingrecipesspringrest.model.Recipe;
import com.example.cookingrecipesspringrest.model.RecipeIngredients;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringJUnitConfig
@SpringJUnitWebConfig
@ContextConfiguration(classes = {SpringConfig.class, DatabaseConfig.class})
@Testcontainers
class RecipeIngredientsRepositoryTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test")
            .withPassword("test")
            .withUsername("admin");

    @Autowired
    private RecipeIngredientsRepository repository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory = new Category("первая", new ArrayList<>());

    private Recipe testRecipe = new Recipe(testCategory, "рецепт", new ArrayList<>());

    private Ingredient testIngredientOne = new Ingredient("ингредиент 1", new ArrayList<>());

    private Ingredient testIngredientTwo = new Ingredient("ингредиент 2", new ArrayList<>());

    private Ingredient testIngredientThree = new Ingredient("ингредиент 3", new ArrayList<>());

    private RecipeIngredients testOne = new RecipeIngredients(testRecipe, testIngredientOne, 100);
    private RecipeIngredients testTwo = new RecipeIngredients(testRecipe, testIngredientTwo, 200);
    private RecipeIngredients testThree = new RecipeIngredients(testRecipe, testIngredientThree, 300);

    @BeforeClass
    public static void beforeClass() {
        postgres.start();
    }

    @AfterClass
    public static void afterClass() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("db.url", postgres::getJdbcUrl);
        registry.add("db.password", postgres::getPassword);
        registry.add("db.username", postgres::getUsername);
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
        categoryRepository.deleteAll();
        testCategory = categoryRepository.save(testCategory);
        testRecipe = recipeRepository.save(testRecipe);
        testIngredientOne = ingredientRepository.save(testIngredientOne);
        testIngredientTwo = ingredientRepository.save(testIngredientTwo);
        testIngredientThree = ingredientRepository.save(testIngredientThree);
        testOne = repository.save(testOne);
        testTwo = repository.save(testTwo);
        testThree = repository.save(testThree);
    }

    @Test
    void findById_ExistingId_ReturnRecipeIngredients() {
        Optional<RecipeIngredients> actual = repository.findById(1L);
        actual.ifPresent(recipe -> Assertions.assertEquals(testOne, recipe));
    }

    @Test
    void findById_NoneExistingId_ReturnEmpty() {
        Optional<RecipeIngredients> actual = repository.findById(1444L);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findAll_NotEmptyTable_ReturnRecipeIngredients() {
        List<RecipeIngredients> actual = repository.findAll();
        Assertions.assertEquals(3, actual.size());
    }

    @Test
    void findRecipeIngredientsByRecipeIdOrderById_ExistingRecipe_ReturnRecipeIngredients() {
        List<RecipeIngredients> actual = repository.findRecipeIngredientsByRecipeIdOrderById(testRecipe.getId());
        Assertions.assertEquals(3, actual.size());
    }

    @Test
    void findRecipeIngredientsByRecipeIdOrderById_NotExistingRecipe_ReturnEmptyList() {
        List<RecipeIngredients> actual = repository.findRecipeIngredientsByRecipeIdOrderById(1000L);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void findRecipeIngredientsByIngredientIdOrderById_ExistingIngredient_ReturnRecipeIngredients() {
        List<RecipeIngredients> actual = repository.findRecipeIngredientsByIngredientIdOrderById(testIngredientOne.getId());
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    void findRecipeIngredientsByIngredientIdOrderById_NotExistingIngredient_ReturnEmptyList() {
        List<RecipeIngredients> actual = repository.findRecipeIngredientsByIngredientIdOrderById(1000L);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void findAll_EmptyTable_ReturnEmptyList() {
        repository.deleteAll();
        List<RecipeIngredients> actual = repository.findAll();
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void deleteById_ExistingId_ReturnEmpty() {
        repository.deleteById(1L);
        Assertions.assertFalse(repository.findById(1L).isPresent());
    }

    @Test
    void deleteById_NoneExistingId_ReturnEmpty() {
        repository.deleteById(1000L);
        Assertions.assertFalse(repository.findById(1000L).isPresent());
    }

    @Test
    void save_NotExistRecipeIngredients_ReturnNewRecipeIngredients() {
        RecipeIngredients expected = new RecipeIngredients(testRecipe, testIngredientThree, 400);
        RecipeIngredients actual = repository.save(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_ExistRecipeIngredients_ReturnRecipeIngredients() {
        RecipeIngredients expected = new RecipeIngredients(testThree.getId(), testRecipe, testIngredientThree, 444);
        RecipeIngredients actual = repository.save(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_NotExistRecipeIngredientsInvalidEntity_ThrowsDataIntegrityViolationException() {
        RecipeIngredients expected = new RecipeIngredients();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(expected));
    }


}