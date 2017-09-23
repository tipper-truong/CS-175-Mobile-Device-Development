package com.example.tipper.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


import com.example.tipper.whatsfordinner.recipe.RecipeContent;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        RecipeContent.setContext(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(RecipeContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<RecipeContent.RecipeItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<RecipeContent.RecipeItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            //holder.mIdView.setText(mValues.get(position).id);
            holder.mRecipeNameView.setText(mValues.get(position).recipeName); //recipe name

           /* ArrayList<String> recipeList = mValues.get(position).recipeIngredients; //recipe ingredients
            for (String r: recipeList) {
                holder.mRecipeIngredientsView.setText(r); //displaying all the recipe ingredients
            }

            String imagePath = mValues.get(position).recipeImagePath;
            Log.v("Image Path", imagePath);
            if(URLUtil.isValidUrl(imagePath)) { //recipe image path
                // It's a URL
                try {
                    URL url = new URL(imagePath);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    Picasso.with(getApplicationContext()).load(imagePath).into(holder.mRecipeImageView);
                    urlConnection.disconnect(); //avoid any response leakage
                } catch(java.io.IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Else: it's a drawable resource ID
                holder.mRecipeImageView.setImageResource(Integer.valueOf(imagePath));
            }
            holder.mRecipeDescriptionView.setText(mValues.get(position).recipeDetails); */


            if(mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, holder.mItem.recipeName);
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {

                        Bundle arguments = new Bundle();
                        arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, holder.mItem.recipeName);
                        RecipeDetailFragment fragment = new RecipeDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.recipe_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RecipeDetailActivity.class);
                        intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, holder.mItem.recipeName);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            //public final TextView mIdView;
            public final TextView mRecipeNameView;
            public final TextView mRecipeIngredientsView;
            public final ImageView mRecipeImageView;
            public final TextView mRecipeDescriptionView;
            public RecipeContent.RecipeItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                //mIdView = (TextView) view.findViewById(R.id.id);
                mRecipeNameView = (TextView) view.findViewById(R.id.recipe_name);
                mRecipeIngredientsView = (TextView) view.findViewById(R.id.recipe_ingredients_detail);
                mRecipeImageView = (ImageView) view.findViewById(R.id.recipe_image_detail);
                mRecipeDescriptionView = (TextView) view.findViewById(R.id.recipe_description__detail);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mRecipeNameView.getText() + "'";
            }
        }
    }
}