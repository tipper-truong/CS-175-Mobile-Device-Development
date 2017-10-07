package com.example.tipper.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import java.util.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CalendarMealActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private Context calendarMealContext;
    private DatabaseHandler db;
    public static final String DATE_PREF = "DATE_PREF" ;
    public static final String selectedDate = "selectedDateKey";

    public static final String SET_MEAL_PLAN_DATE = "SET_DATE_PREF";
    public static final String setMealPlanDate = "setMealPlanDateKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_meal);

        db = new DatabaseHandler(this);
        calendarMealContext = this;
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarView.addDecorator(new TodaysDateDecorator());

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
                String selected_date = sdf.format(widget.getSelectedDate().getDate());

                try {
                    MealPlan mealPlan = db.getMealPlan(selected_date);
                    // Selected Date already has a meal plan
                    SharedPreferences settings;
                    SharedPreferences.Editor editor;
                    settings = calendarMealContext.getSharedPreferences(SET_MEAL_PLAN_DATE, Context.MODE_PRIVATE);
                    editor = settings.edit();

                    editor.putString(setMealPlanDate, selected_date);
                    editor.commit();

                    Intent intent = new Intent(calendarMealContext, SetMealPlanActivity.class);
                    startActivity(intent);

                } catch (CursorIndexOutOfBoundsException e ) {
                    // Selected Date doesn't have a meal plan
                    Log.v("Error", "No Meal Plan for " + selected_date);
                    SharedPreferences settings;
                    SharedPreferences.Editor editor;
                    settings = calendarMealContext.getSharedPreferences(DATE_PREF, Context.MODE_PRIVATE);
                    editor = settings.edit();

                    editor.putString(selectedDate, selected_date);
                    editor.commit();


                    Intent intent = new Intent(calendarMealContext, CreateMealPlanActivity.class);
                    startActivity(intent);

                }


            }
        });

        ArrayList<MealPlan> mealPlanList= new ArrayList<MealPlan>();
        mealPlanList = db.getAllMealPlans();

        for(MealPlan plan : mealPlanList) {
            if (plan.getDate() != null) {
                Date setMealPlanDate = getDate(plan.getDate());
                calendarView.addDecorator(new MealPlanDecorator(setMealPlanDate));
            }
        }

        db.close();

    }

    private Date getDate(String date)
    {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date mealPlanDate = new Date();
        try {
            mealPlanDate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mealPlanDate;
    }


}
