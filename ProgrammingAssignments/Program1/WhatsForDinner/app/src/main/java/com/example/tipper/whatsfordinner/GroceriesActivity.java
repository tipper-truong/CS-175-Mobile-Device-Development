package com.example.tipper.whatsfordinner;

import android.content.Context;
import android.content.Intent;
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
    private TreeMap<String, Integer> groceriesTreeMap;
    private ArrayList<String> ingredientList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);

        db = new DatabaseHandler(this);

        groceriesContext = this;

        groceriesListView = (SwipeMenuListView) findViewById(R.id.grocerieslistView);


        ArrayList<Recipe> recipeList = db.getAllRecipes();
        groceriesTreeMap = new TreeMap<String, Integer>();
        ArrayList<Ingredient> recipeIngredientList;

        // Getting all the Ingredients from Recipe Table and storing in ArrayList<String> ingredientList
        for(Recipe r : recipeList) {
            recipeIngredientList = r.getIngredients();
            for(int i = 0; i < recipeIngredientList.size(); i++) {
                String ingredient = recipeIngredientList.get(i).getIngredientName() + " " + "(" + recipeIngredientList.get(i).getIngredientCount() + " " + recipeIngredientList.get(i).getIngredientUnit() + ")";
                ingredientList.add(ingredient);
                groceriesTreeMap.put(ingredient, r.getID());

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
                Object groceryItem = groceriesListView.getItemAtPosition(position);
                Toast toast;
                Recipe recipe = new Recipe();

                Log.v("Select Item", groceryItem.toString());
                int recipeID = groceriesTreeMap.get(groceryItem.toString());
                recipe = db.getRecipe(recipeID);

                switch (index) {
                    case 0:

                        // increment unit counter
                        manipulateUnitCounter(recipe, groceryItem.toString(), 1);
                        break;

                    case 1:

                        // decrement unit counter
                        manipulateUnitCounter(recipe, groceryItem.toString(), -1);
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

    private void manipulateUnitCounter(Recipe recipe, String groceryItem, int countChecker)
    {
        ArrayList<Ingredient> ingredients = recipe.getIngredients();
        String grocery = "";
        int count = 0;
        for(int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            grocery = ingredient.getIngredientName() + " " + "(" + ingredient.getIngredientCount() + " " + ingredient.getIngredientUnit() + ")";
            int recipeID = 0;
            if (groceriesTreeMap.containsKey(grocery)) {
                recipeID = groceriesTreeMap.get(grocery); // save recipeID for re-inserting in TreeMap value
                groceriesTreeMap.remove(grocery); // re-insert new Ingredient Key with new counter, remove it first
            }

            if(grocery.equals(groceryItem)) { //
                count = ingredient.getIngredientCount();
                if (countChecker == 1) {
                    count += 1; // increment count
                } else if (countChecker == -1){
                    count -= 1; // decrement count

                }

                if (count == 0) {
                    // delete ingredients from Recipe ArrayList<Ingredient> and update to ArrayAdapter
                    ingredientList.remove(i);
                    ingredients.remove(i); // remove ingredient from arraylist
                    grocery = ingredient.getIngredientName() + " removed from Groceries List";
                    groceriesAdapter.notifyDataSetChanged();

                } else {
                    ingredient.setIngredientCount(count); // set new count in the ingredient
                    ingredients.set(i, ingredient); // update ingredients ArrayList of new count
                    grocery = ingredient.getIngredientName() + " " + "(" + ingredient.getIngredientCount() + " " + ingredient.getIngredientUnit() + ")"; //reset with new count
                    groceriesTreeMap.put(grocery, recipeID);
                }

                break;
            }

        }

        // ingredientList update for ArrayAdapter SwipeMenuListView
        for(int i = 0; i < ingredientList.size(); i++) {
            if(ingredientList.get(i).equals(groceryItem)) {
                //Update ingredientList with new count
                ingredientList.set(i, grocery);
                groceriesAdapter.notifyDataSetChanged();
            }
        }

        recipe.setIngredients(ingredients);
        int result = db.updateRecipe(recipe);

        Toast toast;
        if(result == 1) { // Successful update
            toast = Toast.makeText(groceriesContext, grocery, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        } else {
            toast = Toast.makeText(groceriesContext, "Update for: " + grocery + " unsuccessful", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
    }
}
