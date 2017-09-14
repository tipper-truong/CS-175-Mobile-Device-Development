package com.example.tipper.whatsfordinner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

//Packages used for debugging
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NewDishActivity extends AppCompatActivity {

    private Context newDishContext;
    private Activity newDishActivity;
    private RelativeLayout newDishLayout;
    private PopupWindow newDishPopupWindow;
    private PopupWindow uploadImageURLPopupWindow;
    private ImageView addImage;
    public static final String PREFS_NAME = "IMAGES_PREF";
    public static final String PREFS_KEY = "imageKey";
    private Recipe recipe;
    ArrayList<String> ingredients;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView actv;

    private DatabaseHandler db;
    ArrayList<AutoCompleteTextView> ingredientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);

        recipe = new Recipe();
        db = new DatabaseHandler(this);

        /* For creating/upgrading tables because doing Database db = new DatabaseHandler(); doesn't create new tables or upgrade it for some reason */
        //SQLiteDatabase database = new DatabaseHandler(getApplicationContext()).getWritableDatabase();
        //database.close();

        ingredients = new ArrayList<String>();
        ingredients.add("Apple");
        ingredients.add("Banana");
        ingredients.add("Cherry");
        ingredients.add("Date");
        ingredients.add("Grape");
        ingredients.add("Kiwi");
        ingredients.add("Mango");
        ingredients.add("Pear");

        for(int i = 0; i < ingredients.size(); i++) {
            db.addIngredient(newDishContext, ingredients.get(i));
        }

        /********** IF THE USER SELECTED AN IMAGE LOCALLY **********/
        SharedPreferences settings;
        String imageID;
        SharedPreferences.Editor editor;
        GalleryImageAdapter galleryImageAdapter= new GalleryImageAdapter(this);

        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        imageID = settings.getString(PREFS_KEY, null);
        if (imageID != null) {
            ImageView localImage = (ImageView)findViewById(R.id.setFoodImage);
            localImage.setImageResource(galleryImageAdapter.mImageIds[Integer.parseInt(imageID)]);
            recipe.setImagePath(imageID); //store in Recipe
        } //else set default image
        editor = settings.edit();
        editor.clear();
        editor.commit();


        /********** ENTER AND ADD INGREDIENTS **********/
        ingredientsList = new ArrayList<AutoCompleteTextView>();

        final LinearLayout ingredients_LinearLayout = (LinearLayout) findViewById(R.id.ingredients_linearLayout);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);

        //ADD INGREDIENTS
        TextView addIngredients = (TextView) findViewById(R.id.addIngredients_textView);
        addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ingredients = db.getAllIngredients();
                //Creating the instance of ArrayAdapter containing list of fruit names
                adapter = new ArrayAdapter<String>
                        (newDishContext, android.R.layout.select_dialog_item, ingredients);
                //Getting the instance of AutoCompleteTextView
                actv = new AutoCompleteTextView(newDishContext);
                actv.requestFocus();
                actv.setThreshold(1);//will start working from first character
                actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                actv.setTextColor(Color.RED);
                actv.setHint("Enter an ingredient");
                actv.setLayoutParams(params);
                ingredients_LinearLayout.addView(actv);
                ingredientsList.add(actv);
            }
        });

        //SUBMIT INGREDIENTS
        TextView submitRecipe  = (TextView) findViewById(R.id.submit_textView);
        submitRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> ingredientsStrList = new ArrayList<String>();
                for (int i = 0; i < ingredientsList.size(); i++) {
                    if(!ingredientsList.get(i).getText().toString().isEmpty()) {
                        ingredientsStrList.add(ingredientsList.get(i).getText().toString());
                    } else {
                        Toast toast = Toast.makeText(newDishContext, "Empty list of ingredients, please enter ingredients", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }
                }
                EditText recipeName = (EditText) (findViewById(R.id.recipe_editText));
                EditText recipeDescription = (EditText) findViewById(R.id.recipe_textArea);

                if(!recipeName.getText().toString().isEmpty() && !recipeDescription.getText().toString().isEmpty() && !ingredientsStrList.isEmpty()) {
                    recipe.setName(recipeName.getText().toString()); //store in Recipe
                    recipe.setDescription(recipeDescription.getText().toString());
                    recipe.setIngredients(ingredientsStrList); //store in Recipe

                    /*

                    Add recipe to SQLite Database, Table: Recipes
                    where the recipes will be added to the SQLite Database
                    Set Default Image if user did not select image

                    */

                    boolean recipeAdded = true;
                    if(recipe.getImagePath() == null) {
                        recipe.setImagePath(String.valueOf(R.drawable.meal_icon));
                        recipeAdded = db.addRecipe(newDishContext, recipe);

                        if(recipeAdded) { //if recipe added properly and no errors have been thrown, show user success message
                            //Refresh
                            Intent refresh = new Intent(getApplicationContext(), NewDishActivity.class);
                            startActivity(refresh);
                            finish();
                            //Display Success
                            Toast toast = Toast.makeText(newDishContext, "Added recipe successfully", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        }
                    } else {
                        recipeAdded = db.addRecipe(newDishContext, recipe);

                        if(recipeAdded) {  //if recipe added properly and no errors have been thrown, show user success message
                            //Refresh
                            Intent refresh = new Intent(getApplicationContext(), NewDishActivity.class);
                            startActivity(refresh);
                            finish();
                            //Display Success
                            Toast toast = Toast.makeText(newDishContext, "Added recipe successfully", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        }

                    }

                    //Adding new ingredients for Ingredient AutoCompleteTextView
                    for(int i = 0; i < recipe.getIngredients().size(); i++) {
                        db.addIngredient(newDishContext, recipe.getIngredients().get(i));
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    Toast toast = Toast.makeText(newDishContext, "Empty recipe name, description and/or ingredients, please enter the recipe's name/description/ingredients", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }

            }
        });

        /********** UPLOAD IMAGE VIA URL OR LOCALLY POP UP WINDOW **********/
        uploadImagePopupWindow();

        db.close();

    }

    private void uploadImagePopupWindow()
    {
        newDishContext = getApplicationContext();
        newDishActivity = NewDishActivity.this;

        newDishLayout = (RelativeLayout) findViewById(R.id.rl_newdish_layout);
        addImage = (ImageView) findViewById(R.id.uploadImageView);

        //POPUP WINDOW CONFIGS: Asks the user to upload the image locally or via URL
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) newDishContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.image_upload_popup_layout,null);

                /*
                    public PopupWindow (View contentView, int width, int height)
                        Create a new non focusable popup window which can display the contentView.
                        The dimension of the window must be passed to this constructor.

                        The popup does not provide any background. This should be handled by
                        the content view.

                    Parameters
                        contentView : the popup's content
                        width : the popup's width
                        height : the popup's height
                */
                // Initialize a new instance of popup window
                newDishPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        true
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    newDishPopupWindow.setElevation(5.0f);
                }

                //UPLOAD IMAGE VIA URL
                Button uploadImageURL = (Button) customView.findViewById(R.id.upload_image_url_button);
                uploadImageURL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        newDishPopupWindow.dismiss();

                        //Display popup window to enter image url manually
                        imageUrlPopUpWindow();
                    }
                });

                Button uploadImageLocally = (Button) customView.findViewById(R.id.upload_image_local_button);
                uploadImageLocally.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                        startActivity(intent);
                    }
                });

                // Get a reference for the custom view close button
                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        newDishPopupWindow.dismiss();
                    }
                });

                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
                // Finally, show the popup window at the center location of root relative layout
                newDishPopupWindow.showAtLocation(newDishLayout, Gravity.CENTER,0,0);
            }
        });
    }
    private void imageUrlPopUpWindow()
    {
        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) newDishContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        final View imageUrlView = inflater.inflate(R.layout.image_upload_url_popup_layout,null);

        uploadImageURLPopupWindow = new PopupWindow(
                imageUrlView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            uploadImageURLPopupWindow.setElevation(5.0f);
        }

        //SUBMITTING IMAGE URL
        Button submitButton = (Button) imageUrlView.findViewById(R.id.submitURL_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView uploadedImage = (ImageView) newDishActivity.findViewById(R.id.setFoodImage);
                EditText imageUrl = (EditText) imageUrlView.findViewById(R.id.imageURL_editText);

                if (URLUtil.isValidUrl(imageUrl.getText().toString())) {
                    String validImageUrl = imageUrl.getText().toString().replaceAll("\\s+","");
                    try {
                        recipe.setImagePath(validImageUrl); //store in Recipe
                        URL url = new URL(validImageUrl);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        Picasso.with(newDishContext).load(validImageUrl).into(uploadedImage);
                        urlConnection.disconnect(); //avoid any response leakage
                        uploadImageURLPopupWindow.dismiss();
                    } catch(java.io.IOException e) {
                        Toast toast = Toast.makeText(newDishContext, "Invalid URL, please try again", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(newDishContext, "Invalid URL, please try again", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }
            }
        });

        // Get a reference for the custom view close button
        ImageButton closeButton = (ImageButton) imageUrlView.findViewById(R.id.ib_close);

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                uploadImageURLPopupWindow.dismiss();
            }
        });

        uploadImageURLPopupWindow.showAtLocation(newDishLayout, Gravity.CENTER,0,0);

    }

}
