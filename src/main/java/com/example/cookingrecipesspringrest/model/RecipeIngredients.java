package com.example.cookingrecipesspringrest.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.lang.NonNull;

import java.util.Objects;


@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredients {

    @Id
    @Column(name = "id_recipe_ingredients")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_recipe", nullable = false)
    @Fetch(FetchMode.JOIN)
    @NonNull
    private Recipe recipe;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ingredient", nullable = false)
    @Fetch(FetchMode.JOIN)
    @NonNull
    private Ingredient ingredient;

    @Column(nullable = false)
    private int weight;

    public RecipeIngredients(long id, Recipe recipe, Ingredient ingredient, int weight) {
        this.id = id;
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.weight = weight;
    }

    public RecipeIngredients(Recipe recipe, Ingredient ingredient, int weight) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.weight = weight;
    }

    public RecipeIngredients() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "RecipeIngredients{" +
                "id=" + id +
                ", recipe=" + recipe.getName() +
                ", ingredient=" + ingredient.getName() +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredients that = (RecipeIngredients) o;
        return id == that.id && weight == that.weight && recipe.equals(that.recipe) && ingredient.equals(that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipe, ingredient, weight);
    }
}
