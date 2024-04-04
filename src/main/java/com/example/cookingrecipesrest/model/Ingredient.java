package com.example.cookingrecipesrest.model;

import java.util.List;
import java.util.Objects;

public class Ingredient {

    private long id;


    private String name;

    private List<RecipeIngredients> recipeIngredients;

    public Ingredient(long id, String name, List<RecipeIngredients> recipeIngredients) {
        this.id = id;
        this.name = name;
        this.recipeIngredients = recipeIngredients;
    }

    public Ingredient() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RecipeIngredients> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredients> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", recipeIngredients=" + recipeIngredients +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id && name.equals(that.name) && Objects.equals(recipeIngredients, that.recipeIngredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, recipeIngredients);
    }
}
