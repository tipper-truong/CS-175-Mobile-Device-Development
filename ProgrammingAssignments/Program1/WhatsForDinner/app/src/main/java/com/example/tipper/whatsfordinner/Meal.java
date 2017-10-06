package com.example.tipper.whatsfordinner;

import java.util.Date;
import java.util.*;

/**
 * Created by tipper on 10/5/17.
 */

public class Meal {

    private String mealName;
    private int mealCount;
    private ArrayList<String> mealDate;
    private ArrayList<String> mealCategory; //breakfast, lunch, dinner

    public Meal()
    {
        mealDate = new ArrayList<String>();
        mealCategory = new ArrayList<String>();
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

    public ArrayList<String> getMealDate() {
        return mealDate;
    }

    public void setMealDate(ArrayList<String> mealDate) {
        this.mealDate = mealDate;
    }

    public ArrayList<String> getMealCategory() {
        return mealCategory;
    }

    public void setMealCategory(ArrayList<String> mealCategory) {
        this.mealCategory = mealCategory;
    }
}
