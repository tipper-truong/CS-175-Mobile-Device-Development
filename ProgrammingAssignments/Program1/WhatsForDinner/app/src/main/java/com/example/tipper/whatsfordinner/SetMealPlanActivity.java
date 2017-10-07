package com.example.tipper.whatsfordinner;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SetMealPlanActivity extends AppCompatActivity {

    public static final String SET_MEAL_PLAN_DATE = "SET_DATE_PREF";
    public static final String setMealPlanDate = "setMealPlanDateKey";
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_meal_plan);

        db = new DatabaseHandler(this);
        String date = getMealPlanDate();
        MealPlan mealPlan = db.getMealPlan(date);

        TextView setMealDate = (TextView) findViewById(R.id.setMealDate);
        setMealDate.setText(mealPlan.getDate());

        TextView breakfastInfo = (TextView) findViewById(R.id.breakfastInfo);
        breakfastInfo.setText(mealPlan.getBreakfast());

        TextView lunchInfo = (TextView) findViewById(R.id.lunchInfo);
        lunchInfo.setText(mealPlan.getLunch());

        TextView dinnerInfo = (TextView) findViewById(R.id.dinnerInfo);
        dinnerInfo.setText(mealPlan.getDinner());

    }

    private String getMealPlanDate()
    {
        SharedPreferences settings;
        String date;
        SharedPreferences.Editor editor;

        settings = getApplicationContext().getSharedPreferences(SET_MEAL_PLAN_DATE, Context.MODE_PRIVATE);
        date = settings.getString(setMealPlanDate, null);
        editor = settings.edit();
        editor.clear();
        editor.commit();

        return date;
    }
}
