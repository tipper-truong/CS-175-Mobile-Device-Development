package com.example.tipper.whatsfordinner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.*;

public class GroceriesActivity extends AppCompatActivity {

    private DatabaseHandler db;
    private Context groceriesContext;
    private SwipeMenuListView groceriesListView;
    private ArrayAdapter<String> groceriesAdapter;
    private TreeMap<String, Ingredient> groceriesTreeMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);

        db = new DatabaseHandler(this);

        groceriesContext = this;

        groceriesListView = (SwipeMenuListView) findViewById(R.id.grocerieslistView);

        ArrayList<Recipe> recipeList = db.getAllRecipes();
        ArrayList<String> ingredientList = new ArrayList<String>();
        ArrayList<Ingredient> recipeIngredientList;

        // Getting all the Ingredients from Recipe Table and storing in ArrayList<String> ingredientList
        for(Recipe r : recipeList) {
            recipeIngredientList = r.getIngredients();
            for (Ingredient i : recipeIngredientList) {
                String ingredient = i.getIngredientName() + " " + "(" + i.getIngredientCount() + " " + i.getIngredientUnit() + ")";
                ingredientList.add(ingredient);
                groceriesTreeMap.put(r.getName(), i);
            }
        }

        groceriesAdapter = new ArrayAdapter<String> (this,  android.R.layout.simple_list_item_1, ingredientList);
        groceriesListView.setAdapter(groceriesAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "+" item
                SwipeMenuItem incrementItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                incrementItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                incrementItem.setWidth(dp2px(90));
                // set item title
                incrementItem.setTitle("+");
                // set item title fontsize
                incrementItem.setTitleSize(30);
                // set item title font color
                incrementItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(incrementItem);

                // create "-" item
                SwipeMenuItem decrementItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                decrementItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                decrementItem.setWidth(dp2px(90));
                // set item title
                decrementItem.setTitle("-");
                // set item title fontsize
                decrementItem.setTitleSize(30);
                // set item title font color
                decrementItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(decrementItem);
            }
        };


        groceriesListView.setMenuCreator(creator);

        groceriesListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Object item = groceriesListView.getItemAtPosition(position);
                Toast toast;
                switch (index) {
                    case 0:
                        // increment
                        toast = Toast.makeText(groceriesContext, "+", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                        break;
                    case 1:
                        // decrement
                        toast = Toast.makeText(groceriesContext, "-", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                        break;
                }
                return false;
            }
        });

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
