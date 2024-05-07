package com.example.cookingrecipesspringrest.repository;

import com.example.cookingrecipesspringrest.model.RecipeIngredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredients, Long> {

    List<RecipeIngredients> findRecipeIngredientsByRecipeIdOrderById(Long recipeId);

    List<RecipeIngredients> findRecipeIngredientsByIngredientIdOrderById(Long ingredientId);

}
