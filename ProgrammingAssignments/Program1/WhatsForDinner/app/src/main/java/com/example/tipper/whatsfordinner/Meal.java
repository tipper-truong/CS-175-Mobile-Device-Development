package com.example.tipper.whatsfordinner;

/**
 * Created by tipper on 10/5/17.
 */

public class Meal {

    private String mealName;
    private int mealCount;

    public Meal(String mealName, int mealCount)
    {
        this.mealName = mealName;
        this.mealCount = mealCount;
    }


    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public int getMealCount() {
        return mealCount;
    }

    public void setMealCount(int mealCount) {
        this.mealCount = mealCount;
    }
}
