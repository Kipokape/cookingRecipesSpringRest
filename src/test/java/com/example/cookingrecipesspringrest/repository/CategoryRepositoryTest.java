package com.example.cookingrecipesspringrest.repository;


import com.example.cookingrecipesspringrest.config.DatabaseConfig;
import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.model.Category;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
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
class CategoryRepositoryTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test")
            .withPassword("test")
            .withUsername("admin");

    @Autowired
    private CategoryRepository repository;
    private Category testOne = new Category(1, "первая", new ArrayList<>());
    private Category testTwo = new Category(2, "вторая", new ArrayList<>());
    private Category testThree = new Category(3, "третья", new ArrayList<>());

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
    void findById_ExistingId_ReturnCategory() {
        Optional<Category> actual = repository.findById(1L);
        actual.ifPresent(category -> Assertions.assertEquals(testOne, category));
    }

    @Test
    void findById_NoneExistingId_ReturnEmpty() {
        Optional<Category> actual = repository.findById(0L);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void deleteById_ExistingId_ReturnEmpty() {
        repository.deleteById(1L);
        Assertions.assertFalse(repository.findById(1L).isPresent());
    }

    @Test
    void deleteById_NoneExistingId_ReturnEmpty() {
        repository.deleteById(1111L);
        Assertions.assertFalse(repository.findById(1111L).isPresent());
    }

    @Test
    void deleteByEntity_ExistingEntity_ReturnEmpty() {
        repository.delete(testOne);
        Assertions.assertFalse(repository.findOne(Example.of(testOne)).isPresent());
    }

    @Test
    void findAll_NotEmptyTable_ReturnCategories() {
        List<Category> actual = repository.findAll();
        Assertions.assertEquals(3, actual.size());
    }

    @Test
    void findAll_EmptyTable_ReturnEmptyList() {
        repository.deleteAll();
        List<Category> actual = repository.findAll();
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    void save_NotExistCategory_ReturnNewCategory() {
        Category expected = new Category("Четвертая", new ArrayList<>());
        Category actual = repository.save(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_ExistCategory_ReturnCategory() {
        Category expected = new Category(testThree.getId(), "Третья2", new ArrayList<>());
        Category actual = repository.save(expected);
        Assertions.assertEquals(expected, actual);

    }

    @Test
    void save_NotExistCategoryInvalidEntity_ThrowsDataIntegrityViolationException() {
        Category expected = new Category();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(expected));
    }

}