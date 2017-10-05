package com.example.tipper.whatsfordinner;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import android.text.InputType;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class EditDishActivity extends AppCompatActivity {

    public static final String EDIT_RECIPE_PREF = "EDIT_RECIPE_PREF" ;
    public static final String recipeKey = "recipeKey";
    private Recipe recipe;
    private ArrayList<Ingredient> ingredientList;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterSpinner;
    private AutoCompleteTextView actv;
    private Spinner spinnerUnit;
    private EditText editTextUnitNum;

    private DatabaseHandler db;
    ArrayList<AutoCompleteTextView> AutoCompleteIngredientsList; //ingredients name list
    ArrayList<Spinner> unitList; // ingredient unit list
    ArrayList<EditText> unitNumList; // ingredient unit number list


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);

        db = new DatabaseHandler(this);
        recipe = new Recipe();
        ingredientList = new ArrayList<Ingredient>();
        AutoCompleteIngredientsList = new ArrayList<AutoCompleteTextView>();
        unitList = new ArrayList<Spinner>();
        unitNumList = new ArrayList<EditText>();

        recipe = retrieveRecipe(db);
        ingredientList = recipe.getIngredients();
        final LinearLayout ingredients_LinearLayout = (LinearLayout) findViewById(R.id.ingredients_linearLayout);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);

        /********** SET RECIPE INFORMATION TO DISH LAYOUT **********/

        // SETTING RECIPE NAME
        EditText recipeNameEditText = (EditText) findViewById(R.id.recipe_editText);
        recipeNameEditText.setText(recipe.getName());
        // End SETTING RECIPE NAME

        ImageView recipeImageView = (ImageView) findViewById(R.id.setFoodImage);

        // SETTING RECIPE IMAGE
        if(URLUtil.isValidUrl(recipe.getImagePath())) {
            // Image from the Internet
            URL url = null;
            try {
                url = new URL(recipe.getImagePath());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Picasso.with(getApplicationContext()).load(recipe.getImagePath()).into(recipeImageView);
            urlConnection.disconnect(); //avoid any response leakage
        } else if (recipe.getImagePath().equals(String.valueOf(R.drawable.meal_icon))){
            recipeImageView.setImageResource(R.drawable.meal_icon);
        } else {
            // Local Image
            GalleryImageAdapter galleryImageAdapter= new GalleryImageAdapter(this);
            int imageID = Integer.parseInt(recipe.getImagePath());
            recipeImageView.setImageResource(galleryImageAdapter.mImageIds[imageID]);

        }  // End SETTING RECIPE IMAGE

        // SETTING RECIPE DESCRIPTION
        TextView recipeDescriptionTextView = (TextView) findViewById(R.id.recipe_textArea);
        recipeDescriptionTextView.setText(recipe.getDescription());
        // End SETTING RECIPE DESCRIPTION

        for (int i = 0; i < ingredientList.size(); i++)
        {

            ArrayList<String> ingredientNameStrList = new ArrayList<String>();
            ingredientNameStrList.add(ingredientList.get(i).getIngredientName());

            //Creating the instance of ArrayAdapter containing list of fruit names
            adapter = new ArrayAdapter<String>
                    (getApplicationContext(), android.R.layout.select_dialog_item, ingredientNameStrList);
            adapterSpinner = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.units));

            actv = new AutoCompleteTextView(getApplicationContext()); //Ingredient Name
            editTextUnitNum = new EditText(getApplicationContext()); // Ingredient Unit Number
            spinnerUnit = new Spinner(getApplicationContext()); // Ingredient Unit --> lb, pieces, etc.

            actv.requestFocus();
            actv.setThreshold(1);//will start working from first character
            actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

            spinnerUnit.setAdapter(adapterSpinner); //setting adapter data into EditSpinner (food units)
            spinnerUnit.setSelection(ingredientList.get(i).getIngredientUnitId());

            actv.setTextColor(Color.RED);
            actv.setHint("Enter an ingredient");
            actv.setLayoutParams(params);
            actv.setText(ingredientList.get(i).getIngredientName());

            editTextUnitNum.setHint("Enter unit number per measurement");
            editTextUnitNum.setInputType(InputType.TYPE_CLASS_NUMBER);
            editTextUnitNum.setText(String.valueOf(ingredientList.get(i).getIngredientCount()));

            ingredients_LinearLayout.addView(actv);
            ingredients_LinearLayout.addView(editTextUnitNum);
            ingredients_LinearLayout.addView(spinnerUnit);

            // ADD UNIT AND COUNT ON INGREDIENTS TABLE AND MANIPULATE DATA BASE ON THAT INFO
            AutoCompleteIngredientsList.add(actv);
            unitNumList.add(editTextUnitNum);
            unitList.add(spinnerUnit);

        }

        // UPDATE INGREDIENTS AND UPLOAD POPUP WINDOW

    }

    private Recipe retrieveRecipe(DatabaseHandler db)
    {
        SharedPreferences settings;
        String recipeName;
        SharedPreferences.Editor editor;
        GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(this);
        Recipe recipe = new Recipe();
        settings = getApplicationContext().getSharedPreferences(EDIT_RECIPE_PREF, Context.MODE_PRIVATE);
        recipeName = settings.getString(recipeKey, null);
        if (recipeName != null) {
            recipe = db.getRecipe(recipeName);

        } //else set default image
        editor = settings.edit();
        editor.clear();
        editor.commit();

        return recipe;
    }
}
