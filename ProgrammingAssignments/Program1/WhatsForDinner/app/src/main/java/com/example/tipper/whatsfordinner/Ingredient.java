package com.example.tipper.whatsfordinner;

/**
 * Created by tipper on 9/23/17.
 */

public class Ingredient
{
    public String ingredientName;
    public String ingredientUnit;
    public int ingredientUnitId;
    public int ingredientCount;

    public Ingredient()
    {

    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientUnit() {
        return ingredientUnit;
    }

    public void setIngredientUnit(String ingredientUnit) {
        this.ingredientUnit = ingredientUnit;
    }

    public int getIngredientCount() {
        return ingredientCount;
    }

    public void setIngredientCount(int ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    public int getIngredientUnitId() {
        return ingredientUnitId;
    }

    public void setIngredientUnitId(int ingredientUnitId) {
        this.ingredientUnitId = ingredientUnitId;
    }
}
