package com.example.tipper.whatsfordinner;

import java.util.*;

/**
 * Created by tipper on 10/5/17.
 */

public class Meal {

    private String mealName;
    private int mealCount;

    public Meal()
    {
    }


    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public int getMealCount() {return mealCount;}
    public void setMealCount(int mealCount) {
        this.mealCount = mealCount;
    }


}
