package com.example.tipper.whatsfordinner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by tipper on 9/8/17.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "RecipeDB";

    // Recipe table name
    private static final String TABLE_RECIPES = "Recipe";

    // Recipe Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_DESCRIPTION = "description";

    // Ingredient table name
    private static final String TABLE_INGREDIENTS = "Ingredient";

    // Ingredient Table Column names
    private static final String KEY_INGREDIENTS_NAME = "ingredients";

    //Meal table name
    private static final String TABLE_MEALS = "Meal";

    //Meal Table Column names
    private static final String KEY_MEAL_NAME = "meal";
    private static final String KEY_MEAL_COUNT = "meal_count";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("Creating Table", "True");
        String CREATE_INGREDIENT_TABLE = "CREATE TABLE " + TABLE_INGREDIENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_INGREDIENTS_NAME + " TEXT,"
                +  "UNIQUE (" + KEY_INGREDIENTS_NAME + ") ON CONFLICT ROLLBACK)"; //helps avoid duplicates

        String CREATE_MEAL_TABLE = "CREATE TABLE " + TABLE_MEALS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_MEAL_NAME + " TEXT,"
                + KEY_MEAL_COUNT + " INTEGER )"; //helps avoid duplicates

        String CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                        + KEY_ID + " INTEGER PRIMARY KEY,"
                        + KEY_NAME + " TEXT,"
                        + KEY_INGREDIENTS + " TEXT, "
                        + KEY_IMAGE_PATH + " TEXT, "
                        + KEY_DESCRIPTION + " TEXT, "
                        +  "UNIQUE (" + KEY_NAME  + ") ON CONFLICT ROLLBACK )"; //helps avoid duplicates

        db.execSQL(CREATE_INGREDIENT_TABLE);
        db.execSQL(CREATE_MEAL_TABLE);
        db.execSQL(CREATE_RECIPE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("Upgrading Table", "True");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);

        // Create tables again
        onCreate(db);
    }

    /*********** OPERATIONS FOR INGREDIENTS TABLE ***********/
    public void addIngredient(Context context, String ingredient)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INGREDIENTS_NAME, ingredient);

        try {
            db.insertOrThrow(TABLE_INGREDIENTS, null, values);
        } catch (SQLiteConstraintException e) {
            Log.v("Error", ingredient + " already exist in database");
        }

        db.close();
    }

    public ArrayList<String> getAllIngredients()
    {
        ArrayList<String> ingredientList = new ArrayList<String>();

        //SELECT ALL Query
        String selectQuery = "SELECT * FROM " + TABLE_INGREDIENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all the rows and adding to the list
        if (cursor.moveToFirst()) {
            do {
               ingredientList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return ingredientList;
    }

    /*********** OPERATIONS FOR RECIPE TABLE ***********/
    public boolean addRecipe(Context context, Recipe recipe)
    {
        boolean recipeAdded = true;
        SQLiteDatabase db = this.getWritableDatabase();
        //For the ArrayList of Recipes
        Gson gson = new Gson();
        String arrayListStr = gson.toJson(recipe.getIngredients());

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, recipe.getName());
        values.put(KEY_INGREDIENTS, arrayListStr);
        values.put(KEY_IMAGE_PATH, recipe.getImagePath());
        values.put(KEY_DESCRIPTION, recipe.getDescription());

        try {
            db.insertOrThrow(TABLE_RECIPES, null, values);
        } catch (SQLiteConstraintException e) { //If the value already exists, alert the user to enter a different recipe
            recipeAdded = false;
            Toast toast = Toast.makeText(context, "This recipe already exists, please try a different one", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }

        db.close();
        return recipeAdded;
    }

    public Recipe getRecipe(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, new String[] { KEY_ID, KEY_NAME, KEY_INGREDIENTS, KEY_IMAGE_PATH, KEY_DESCRIPTION}, KEY_NAME + "=?",
                new String[] {name}, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Recipe recipe = new Recipe();
        recipe.setID(cursor.getInt(0));
        recipe.setName(cursor.getString(1));
        Gson gson = new Gson();
        ArrayList<String> ingredientList = gson.fromJson(cursor.getString(2), new TypeToken<ArrayList<String>>(){}.getType());
        recipe.setIngredients(ingredientList);
        recipe.setImagePath(cursor.getString(3));
        recipe.setDescription(cursor.getString(4));

        return recipe;
    }


    public ArrayList<Recipe> getAllRecipes()
    {
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();

        //SELECT ALL Query
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all the rows and adding to the list
        if (cursor.moveToFirst()) {
            do {
                Gson gson = new Gson();
                Recipe recipe = new Recipe();
                recipe.setID(Integer.parseInt(cursor.getString(0)));
                recipe.setName(cursor.getString(1));
                //Retrieve the token of ArrayList of ingredients
                ArrayList<String> ingredientList = gson.fromJson(cursor.getString(2), new TypeToken<ArrayList<String>>(){}.getType());
                recipe.setIngredients(ingredientList);
                recipe.setImagePath(cursor.getString(3));
                recipe.setDescription(cursor.getString(4));

                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }

        return recipeList;
    }
}
