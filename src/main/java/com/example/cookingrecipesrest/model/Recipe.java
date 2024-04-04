package com.example.cookingrecipesrest.model;

import java.util.List;
import java.util.Objects;

public class Recipe {

    private long id;

    private long idCategory;
    private String name;

    private List<RecipeIngredients> recipeIngredients;

    public Recipe(long id, long idCategory, String name, List<RecipeIngredients> recipeIngredients) {
        this.id = id;
        this.idCategory = idCategory;
        this.name = name;
        this.recipeIngredients = recipeIngredients;
    }

    public Recipe() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(long idCategory) {
        this.idCategory = idCategory;
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
        return "Recipe{" +
                "id=" + id +
                ", idCategory=" + idCategory +
                ", name='" + name + '\'' +
                ", recipeIngredients=" + recipeIngredients +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id && idCategory == recipe.idCategory && name.equals(recipe.name) && Objects.equals(recipeIngredients, recipe.recipeIngredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idCategory, name, recipeIngredients);
    }
}
