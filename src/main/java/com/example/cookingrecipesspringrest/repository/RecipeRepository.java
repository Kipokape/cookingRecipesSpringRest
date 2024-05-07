
package com.example.cookingrecipesspringrest.repository;


import com.example.cookingrecipesspringrest.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByCategoryIdOrderById(Long categoryId);
}
