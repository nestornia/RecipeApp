package com.example.mvvmapijava;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mvvmapijava.model.Recipe;
import com.example.mvvmapijava.viewmodels.RecipeViewModel;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";

    //UI
    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeRank;
    private LinearLayout mRecipeIngredientsContainer;
    private ScrollView mScrollView;

    private RecipeViewModel mRecipeViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeRank = findViewById(R.id.recipe_social_score);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        setProgressBar(true);
        subscribeObservers();
        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, "getIncomingIntent: " + recipe.getTitle());
            mRecipeViewModel.searchRecipeById(recipe.getRecipeId());
        }
    }

    private void subscribeObservers() {
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe != null) {
                    if (recipe.getRecipeId().equals(mRecipeViewModel.getRecipeId())) {
                        setRecipeProperties(recipe);
                        mRecipeViewModel.setmDidRetrieveRecipe(true);
                    }
                }
            }
        });

        mRecipeViewModel.isRecipeRequestTimedOut().observe(this, new Observer<Boolean>() {

            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean && !mRecipeViewModel.ismDidRetrieveRecipe()) {
                    // display error screen request timed out
                    Log.d(TAG, "onChanged: timed out ...");
                    displayErrorScreen("Error retrieving data. Check network connection.");
                }
            }
        });
    }

    private void setRecipeProperties(Recipe recipe) {
        if (recipe != null) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipe.getImageUrl())
                    .into(mRecipeImage);

            mRecipeTitle.setText(recipe.getTitle());
            mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocialRank())));

            mRecipeIngredientsContainer.removeAllViews();
            for (String ingredient : recipe.getIngredients()) {
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                mRecipeIngredientsContainer.addView(textView);
            }
        }
        showParent();
        setProgressBar(false);
    }

    private void displayErrorScreen(String errorMessage) {
        mRecipeTitle.setText("Error retreiving screen");
        mRecipeRank.setText("");
        TextView textView = new TextView(this);
        if (!errorMessage.equals("")){
            textView.setText(errorMessage);
        }
        else {
            textView.setText("Error");
        }
        textView.setTextSize(15);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        mRecipeIngredientsContainer.addView(textView);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.ic_launcher_background)
                .into(mRecipeImage);

        showParent();
        setProgressBar(false);
    }

    private void showParent() {
        mScrollView.setVisibility(View.VISIBLE);
    }

}
