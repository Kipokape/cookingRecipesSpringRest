package com.example.cookingrecipesspringrest.repository;

import com.example.cookingrecipesspringrest.config.DatabaseConfig;
import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.model.Ingredient;
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
class IngredientRepositoryTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test")
            .withPassword("test")
            .withUsername("admin");

    @Autowired
    private IngredientRepository repository;


    private Ingredient testOne = new Ingredient("ингредиент 1", new ArrayList<>());
    private Ingredient testTwo = new Ingredient("ингредиент 2", new ArrayList<>());
    private Ingredient testThree = new Ingredient("ингредиент 3", new ArrayList<>());

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
        testOne = repository.save(testOne);
        testTwo = repository.save(testTwo);
        testThree = repository.save(testThree);
    }

    @Test
    void findById_ExistingId_ReturnIngredient() {
        Optional<Ingredient> actual = repository.findById(1L);
        actual.ifPresent(recipe -> Assertions.assertEquals(testOne, recipe));
    }

    @Test
    void findById_NoneExistingId_ReturnEmpty() {
        Optional<Ingredient> actual = repository.findById(1444L);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findAll_NotEmptyTable_ReturnIngredients() {
        List<Ingredient> actual = repository.findAll();
        Assertions.assertEquals(3, actual.size());
    }

    @Test
    void findAll_EmptyTable_ReturnEmptyList() {
        repository.deleteAll();
        List<Ingredient> actual = repository.findAll();
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
    void save_NotExistIngredient_ReturnNewIngredient() {
        Ingredient expected = new Ingredient("test", new ArrayList<>());
        Ingredient actual = repository.save(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_ExistIngredient_ReturnIngredient() {
        Ingredient expected = new Ingredient(testThree.getId(), "Третий2", new ArrayList<>());
        Ingredient actual = repository.save(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_NotExistIngredientInvalidEntity_ThrowsDataIntegrityViolationException() {
        Ingredient expected = new Ingredient();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(expected));
    }


}