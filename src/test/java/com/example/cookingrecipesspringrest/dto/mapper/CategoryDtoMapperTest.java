package com.example.cookingrecipesspringrest.dto.mapper;

import com.example.cookingrecipesspringrest.config.SpringConfig;
import com.example.cookingrecipesspringrest.dto.CategoryDTO;
import com.example.cookingrecipesspringrest.model.Category;
import com.example.cookingrecipesspringrest.model.Recipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.NestedTestConfiguration;
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
class CategoryDtoMapperTest {

    private final Category testCategory = new Category(1L, "первая", null);

    private final Recipe testRecipeOne = new Recipe(1L, testCategory, "рецепт 1", new ArrayList<>());

    private final Recipe testRecipeTwo = new Recipe(2L, testCategory, "рецепт 2", new ArrayList<>());

    @BeforeEach
    void setUp() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(testRecipeOne);
        recipes.add(testRecipeTwo);
        testCategory.setRecipes(recipes);
    }

    @Autowired
    CategoryDtoMapper categoryDtoMapper = new CategoryDtoMapperImpl();

    @Test
    void toDto_Category_ReturnCategoryDTO() {
        CategoryDTO dto = categoryDtoMapper.toDto(testCategory);
        Assertions.assertEquals(testCategory.getId(), dto.categoryId());
        Assertions.assertEquals(testCategory.getName(), dto.name());
        Assertions.assertEquals(testCategory.getRecipes().size(), dto.recipes().size());
    }

    @Test
    void toListDTO_ListCategory_ReturnListDto() {
        List<Category> listEntity = new ArrayList<>();
        listEntity.add(testCategory);
        listEntity.add(testCategory);

        List<CategoryDTO> listDto = categoryDtoMapper.toListDto(listEntity);

        Assertions.assertEquals(listEntity.get(1).getName(), listDto.get(1).name());
        Assertions.assertEquals(listEntity.size(), listDto.size());
        Assertions.assertEquals(listEntity.get(1).getRecipes().size(), listDto.get(1).recipes().size());
    }

    @Test
    void toEntity_CategoryDTO_ReturnCategory() {
        CategoryDTO dto = categoryDtoMapper.toDto(testCategory);

        Category entity = categoryDtoMapper.toEntity(dto);

        Assertions.assertEquals(entity.getId(), dto.categoryId());
        Assertions.assertEquals(entity.getName(), dto.name());
        Assertions.assertEquals(entity.getRecipes().size(), dto.recipes().size());
    }

    @Test
    void toListEntity_ListCategoryDTO_ReturnListCategory() {
        CategoryDTO dto = categoryDtoMapper.toDto(testCategory);
        List<CategoryDTO> listDto = new ArrayList<>();
        listDto.add(dto);
        listDto.add(dto);

        List<Category> listEntity = categoryDtoMapper.toListEntity(listDto);

        Assertions.assertEquals(listEntity.get(1).getName(), listDto.get(1).name());
        Assertions.assertEquals(listEntity.size(), listDto.size());
        Assertions.assertEquals(listEntity.get(1).getRecipes().size(), listDto.get(1).recipes().size());
    }

}