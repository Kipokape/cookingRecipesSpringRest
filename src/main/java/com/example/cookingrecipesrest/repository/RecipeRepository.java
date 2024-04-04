
package com.example.cookingrecipesrest.repository;

import com.example.cookingrecipesrest.model.Recipe;

import java.util.List;

public interface RecipeRepository extends SimpleRepository<Recipe, Long>{

    List<Recipe> getRecipesByCategory(Long idCategory);
}
