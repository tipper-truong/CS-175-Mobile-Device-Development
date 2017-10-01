package com.example.tipper.whatsfordinner;
import java.util.*;
/**
 * Created by tipper on 9/6/17.
 */

public class Recipe
{
    private int id;
    private String name;
    private ArrayList<Ingredient> ingredient;
    private String imagePath;
    private String description;

    public Recipe()
    {
        ingredient = new ArrayList<Ingredient>();
    }

    public void setID(int id) { this.id = id; }

    public int getID() { return id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredient;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredient = ingredients;
    }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }


}
