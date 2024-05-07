package com.example.cookingrecipesspringrest.model;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "ingredient")
@BatchSize(size = 10)
public class Ingredient {

    @Id
    @Column(name = "id_ingredient")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name_ingredient", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "ingredients", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @Fetch(FetchMode.SUBSELECT)
    private List<Recipe> recipes;

    public Ingredient(long id, String name, List<Recipe> recipes) {
        this.id = id;
        this.name = name;
        this.recipes = recipes;
    }

    public Ingredient(String name, List<Recipe> recipes) {
        this.name = name;
        this.recipes = recipes;
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

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", recipes=" + recipes.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
