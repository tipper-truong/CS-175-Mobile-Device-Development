package com.example.tipper.whatsfordinner;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tipper.whatsfordinner.recipe.RecipeContent;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private RecipeContent.RecipeItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = RecipeContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.recipeName);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        RecipeContent.setContext(rootView.getContext());

        // Initialize Relative Layout for Recipe Detail XML
        if (mItem != null) {
            TextView recipeName = (TextView) rootView.findViewById(R.id.recipe_name_detail);
            TextView recipeIngredients = (TextView) rootView.findViewById(R.id.recipe_ingredients_detail);
            ImageView recipeImage = (ImageView) rootView.findViewById(R.id.recipe_image_detail);
            TextView recipeDescription = (TextView) rootView.findViewById(R.id.recipe_description__detail);
            recipeName.setText(mItem.recipeName); //set recipe name
            for (Ingredient i : mItem.recipeIngredients) {
                recipeIngredients.append(i.getIngredientName()  + " " +
                                         i.getIngredientCount() + " " +
                                         i.getIngredientUnit()  + " " + " \n"); // set recipe ingredients
            }

            String imagePath = mItem.recipeImagePath; //set recipe image
            if (URLUtil.isValidUrl(imagePath)) {
                // It's a URL
                try {
                    URL url = new URL(imagePath);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    Picasso.with(rootView.getContext()).load(imagePath).into(recipeImage);
                    urlConnection.disconnect(); //avoid any response leakage
                } catch(java.io.IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Else: it's a drawable resource ID or from GalleryView
                GalleryImageAdapter galleryImageAdapter= new GalleryImageAdapter(rootView.getContext());

                if (!imagePath.equals("2130837601")) { //default image id
                    recipeImage.setImageResource(galleryImageAdapter.mImageIds[Integer.parseInt(imagePath)]);
                } else  {
                    //else set default image
                    recipeImage.setImageResource(Integer.valueOf(imagePath));
                }
            }
            recipeDescription.setText(mItem.recipeDetails);

        }

        return rootView;
    }
}
