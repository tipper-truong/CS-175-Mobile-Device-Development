package com.example.tipper.whatsfordinner.recipe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.tipper.whatsfordinner.DatabaseHandler;
import com.example.tipper.whatsfordinner.Ingredient;
import com.example.tipper.whatsfordinner.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class RecipeContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<RecipeItem> ITEMS = new ArrayList<RecipeItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, RecipeItem> ITEM_MAP = new HashMap<String, RecipeItem>();
    private static SQLiteDatabase db;


    /*static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createRecipeItem(i));
        }
    }*/

    public static void setContext(Context c)
    {
        ITEMS.clear(); //reset data structure
        ITEM_MAP.clear(); // reset data structure
        if(db == null) { db = new DatabaseHandler(c).getWritableDatabase(); }
        if(db.isOpen()) {
            DatabaseHandler database = new DatabaseHandler(c);
            ArrayList<Recipe> recipeList = database.getAllRecipes();

            for (Recipe recipe : recipeList) {
                //RecipeItem item = new RecipeItem(String.valueOf(recipe.getID()), recipe.getName(), recipe.getDescription());
                RecipeItem item = new RecipeItem(recipe.getName(), recipe.getIngredients(), recipe.getImagePath(), recipe.getDescription());
                addItem(item);
            }
            database.close();
            db.close();
        } else {
            db = new DatabaseHandler(c).getWritableDatabase();
            DatabaseHandler database = new DatabaseHandler(c);
            ArrayList<Recipe> recipeList = database.getAllRecipes();

            for (Recipe recipe : recipeList) {
                //RecipeItem item = new RecipeItem(String.valueOf(recipe.getID()), recipe.getName(), recipe.getDescription());
                RecipeItem item = new RecipeItem(recipe.getName(), recipe.getIngredients(), recipe.getImagePath(), recipe.getDescription());
                addItem(item);
            }
            database.close();
            db.close();
        }
    }


    private static void addItem(RecipeItem item) {
        if(!ITEMS.contains(item) && !ITEM_MAP.containsKey(item.recipeName)) {
            ITEMS.add(item);
            ITEM_MAP.put(item.recipeName, item);
        }
    }

    /*private static RecipeItem createRecipeItem(int position) {
        //return new RecipeItem(String.valueOf(position), "Item " + position, makeDetails(position));
        return null;
        //return new RecipeItem(recipe.getName(), recipe.getIngredients(), recipe.getImagePath(), recipe.getDescription());
    }*/

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class RecipeItem {
        //public final String id;
        //public final String content;
        //public final String details;
        public final String recipeName;
        public final ArrayList<Ingredient> recipeIngredients;
        public final String recipeImagePath;
        public final String recipeDetails;

        public RecipeItem(String recipeName, ArrayList<Ingredient> recipeIngredients, String recipeImagePath, String recipeDetails) {
            this.recipeName = recipeName;
            this.recipeImagePath = recipeImagePath;
            this.recipeIngredients = recipeIngredients;
            this.recipeDetails = recipeDetails;
            //this.id = id;
            //this.content = content;
            //this.details = details;
        }

        @Override
        public String toString() {
            return recipeName;
        }
    }
}
