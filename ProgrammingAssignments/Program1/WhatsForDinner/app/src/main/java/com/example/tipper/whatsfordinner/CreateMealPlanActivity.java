package com.example.tipper.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

public class CreateMealPlanActivity extends AppCompatActivity {

    public static final String DATE_PREF = "DATE_PREF" ;
    public static final String selectedDate = "selectedDateKey";
    private String date;
    private Spinner spinnerBreakfast;
    private Spinner spinnerLunch;
    private Spinner spinnerDinner;
    private ArrayAdapter<String> adapter;
    private DatabaseHandler db;
    private ArrayList<Meal> mealList;
    private ArrayList<String> mealNameList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        db = new DatabaseHandler(this);

        //SET SELECTED DATE
        TextView selectedDate = (TextView) findViewById(R.id.selectedDate);
        date = getSelectedDate();
        selectedDate.setText(date);

        spinnerBreakfast = (Spinner) findViewById(R.id.spinnerBreakfast);
        spinnerLunch = (Spinner) findViewById(R.id.spinnerLunch);
        spinnerDinner = (Spinner) findViewById(R.id.spinnerDinner);

        mealList = new ArrayList<Meal>();
        mealList = db.getAllMeals();

        mealNameList.add("Eating Out");
        for (Meal meal : mealList)
        {
            int mealCounter = meal.getMealCount();
            for(int i = 0; i < mealCounter ; i++) {
                mealNameList.add(meal.getMealName());
            }
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, mealNameList);

        spinnerBreakfast.setAdapter(adapter);
        spinnerLunch.setAdapter(adapter);
        spinnerDinner.setAdapter(adapter);

        spinnerBreakfast.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinnerBreakfast.getSelectedItem().toString();
                updateSpinner(i, selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerLunch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinnerLunch.getSelectedItem().toString();
                updateSpinner(i, selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerDinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinnerDinner.getSelectedItem().toString();
                updateSpinner(i, selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button submitMealPlan = (Button) findViewById(R.id.submitMeals);
        submitMealPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String breakfast = spinnerBreakfast.getSelectedItem().toString();
                String lunch = spinnerLunch.getSelectedItem().toString();
                String dinner = spinnerDinner.getSelectedItem().toString();

                MealPlan mealPlan = new MealPlan(date, breakfast, lunch, dinner);
                db.addMealPlan(mealPlan);

               Intent intent = new Intent(view.getContext(), CalendarMealActivity.class);
                startActivity(intent);

            }
        });



    }


    public void updateSpinner(int i, String selectedItem)
    {
        if (!selectedItem.equals("Eating Out")) {
            Meal selectedMeal = new Meal();
            selectedMeal = db.getMeal(selectedItem); //save previous Meal if user changes meal

            int newCount = selectedMeal.getMealCount() - 1;
            selectedMeal.setMealCount(newCount);

            int updateMeal = db.updateMeal(selectedMeal);
            if (updateMeal == 1) {
                Log.v("Error", "None");
            } else {
                Log.v("Error", "Update Meal unsuccessfully");
            }

            mealNameList.remove(i);
            adapter.notifyDataSetChanged();
        }
    }
    private String getSelectedDate()
    {
        SharedPreferences settings;
        String date;
        SharedPreferences.Editor editor;

        settings = getApplicationContext().getSharedPreferences(DATE_PREF, Context.MODE_PRIVATE);
        date = settings.getString(selectedDate, null);
        editor = settings.edit();
        editor.clear();
        editor.commit();

        return date;
    }
}
