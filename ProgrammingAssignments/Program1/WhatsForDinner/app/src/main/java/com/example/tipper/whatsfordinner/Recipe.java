package com.example.tipper.whatsfordinner;
import java.util.*;
/**
 * Created by tipper on 9/6/17.
 */

public class Recipe
{
    private int id;
    private String name;
    private ArrayList<String> ingredients;
    private String imagePath;
    private String description;

    public Recipe()
    {
        ingredients = new ArrayList<String>();
    }

    public void setID(int id) { this.id = id; }

    public int getID() { return id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }


}
