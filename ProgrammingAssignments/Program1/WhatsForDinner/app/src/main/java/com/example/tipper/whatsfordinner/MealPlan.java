package com.example.tipper.whatsfordinner;

/**
 * Created by tipper on 10/6/17.
 */

public class MealPlan {

    private String date;
    private String breakfast;
    private String lunch;
    private String dinner;

    public MealPlan(String date, String breakfast, String lunch, String dinner)
    {
        this.date = date;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    public String getDate() {
        return date;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public String getDinner() {
        return dinner;
    }
}
