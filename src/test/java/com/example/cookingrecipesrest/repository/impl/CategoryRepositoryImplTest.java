package com.example.cookingrecipesrest.repository.impl;

import com.example.cookingrecipesrest.db.ConnectionManager;
import com.example.cookingrecipesrest.db.ConnectionManagerTestImpl;
import com.example.cookingrecipesrest.model.Category;
import com.example.cookingrecipesrest.model.Recipe;
import com.example.cookingrecipesrest.repository.CategoryRepository;
import com.example.cookingrecipesrest.repository.IngredientRepository;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;

class CategoryRepositoryImplTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    private CategoryRepository repository;

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
        repository = new CategoryRepositoryImpl(connectionProvider);
        ingredientRepository = new IngredientRepositoryImpl(connectionProvider);

    }

    @AfterEach
    void tearDown() {
        repository.truncateTable();
    }

    @Test
    void findById_ExistingId_ReturnCategory() {
        Category expected;
        Category actual;
        List<Recipe> recipes= new ArrayList<>();
        recipes.add(new Recipe(0, 1, "Борщ", null));
        recipes.add(new Recipe(0, 1, "Щи", null));

        expected = repository.save(new Category(0, "Первые блюда", recipes));
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
    void deleteById_ExistingId_ReturnTrue() {
        Category expected;
        List<Recipe> recipes= new ArrayList<>();
        recipes.add(new Recipe(0, 1, "Борщ", null));
        recipes.add(new Recipe(0, 1, "Щи", null));
        expected = repository.save(new Category(0, "Первые блюда", recipes));
        Assertions.assertTrue(repository.deleteById(expected.getId()));
    }

    @Test
    void deleteById_NoneExistingId_ReturnFalse() {
        Assertions.assertFalse(repository.deleteById(11L));
    }

    @Test
    void findAll_NotEmptyTable_ReturnCategories() {
        List<Category> categories;
        List<Recipe> recipes= new ArrayList<>();
        recipes.add(new Recipe(0, 1, "Борщ", null));
        recipes.add(new Recipe(0, 1, "Щи", null));
        List<Recipe> recipesSec= new ArrayList<>();
        recipesSec.add(new Recipe(0, 2, "Омлет", null));
        recipesSec.add(new Recipe(0, 2, "Гуляш", null));
        repository.save(new Category(0, "Первые блюда", recipes));
        repository.save(new Category(0, "Вторые блюда", recipesSec));
        categories = repository.findALL();

        System.out.println(categories);

        Assertions.assertEquals(2, categories.size());
    }

    @Test
    void findAll_EmptyTable_ReturnEmptyList() {
        List<Category> categories;
        categories = repository.findALL();
        Assertions.assertEquals(0, categories.size());
    }

    @Test
    void save_NotExistCategory_ReturnNewCategory() {
        Category expected = new Category(1, "Первые блюда", null);
        List<Recipe> recipes= new ArrayList<>();
        recipes.add(new Recipe(0, 1, "Борщ", null));
        recipes.add(new Recipe(0, 1, "Щи", null));
        expected.setRecipes(recipes);
        Category actual = new Category();
        actual.setRecipes(recipes);
        actual.setName(expected.getName());

        repository.save(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_ExistCategory_ReturnCategory() {
        Category expected = new Category(0, "Первые блюда", null);
        List<Recipe> recipes= new ArrayList<>();
        recipes.add(new Recipe(0, 1, "Борщ", null));
        recipes.add(new Recipe(0, 1, "Щи", null));
        expected.setRecipes(recipes);
        Category actual = new Category();
        actual.setId(1);
        actual.setRecipes(recipes);
        actual.setName("Первые блюда2");

        repository.save(expected);
        expected.setName("Первые блюда2");
        recipes.add(new Recipe(0, 1, "Уха", null));

        repository.save(actual);

        Assertions.assertEquals(expected, actual);

    }

    @Test
    void save_NotExistCategoryUpdate_ThrowsRuntimeException() {
        Category expected = new Category(1, "Первые блюда2", null);
        Assertions.assertThrows(RuntimeException.class, () -> repository.save(expected));
    }


}