package com.example.cookingrecipesspringrest.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @Column(name = "id_recipe")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_category")
    @NonNull
    @Fetch(FetchMode.JOIN)
    private Category category;

    @Column(name = "name_recipe", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "recipe_ingredients",
            joinColumns = @JoinColumn(name = "id_recipe"),
            inverseJoinColumns = @JoinColumn(name = "id_ingredient")
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<Ingredient> ingredients;

    public Recipe(long id, Category category, String name, List<Ingredient> ingredients) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.ingredients = ingredients;
    }

    public Recipe(@NonNull Category category, String name, List<Ingredient> ingredients) {
        this.category = category;
        this.name = name;
        this.ingredients = ingredients;
    }

    public Recipe() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", category=" + category.getName() +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id && category.equals(recipe.category) && name.equals(recipe.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, name);
    }
}
