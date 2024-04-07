package com.example.cookingrecipesrest.model;

import java.util.Objects;

public class RecipeIngredients {

    private long id;

    private long recipeId;

    private long ingredientId;

    private int weight;

    public RecipeIngredients(long id, long recipeId, long ingredientId, int weight) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.weight = weight;
    }

    public RecipeIngredients(long recipeId, long ingredientId, int weight) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
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

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
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
                ", recipeId=" + recipeId +
                ", ingredientId=" + ingredientId +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredients that = (RecipeIngredients) o;
        return id == that.id && recipeId == that.recipeId && ingredientId == that.ingredientId && weight == that.weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipeId, ingredientId, weight);
    }
}
