package com.example.cookingrecipesspringrest.repository;

import com.example.cookingrecipesspringrest.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
