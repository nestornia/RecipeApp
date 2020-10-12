package com.example.mvvmapijava.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.mvvmapijava.model.Recipe;
import com.example.mvvmapijava.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private static RecipeApiClient mRecipeApiClient;
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();

    public static RecipeRepository getInstance() {
        if (instance == null){
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();
        initMediators();
    }

    private void initMediators() {
        LiveData<List<Recipe>> recipeListApiSource = mRecipeApiClient.getRecipes();
        mRecipes.addSource(recipeListApiSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null) {
                    mRecipes.setValue(recipes);
                    doneQuery(recipes);
                }
                else {
                    // search the database cache
                    doneQuery(null);
                }
            }
        });
    }

    public void doneQuery(List<Recipe> list){
        if (list != null) {
            if (list.size() % 30 != 0) {
                mIsQueryExhausted.setValue(true);
            }
        }
        else {
            mIsQueryExhausted.setValue(true);
        }
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }
    public LiveData<Recipe> getRecipe() { return mRecipeApiClient.getRecipe(); }
    public LiveData<Boolean> isRecipeRequestTimedOut() { return mRecipeApiClient.isRecipeRequestTimedOut(); }


    public LiveData<Boolean> isQueryExhausted() {
        return mIsQueryExhausted;
    }

    public void searchRecipesApi(String query, int pageNumber){
        if (pageNumber == 0){
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
        mRecipeApiClient.searchRecipesApi(query, pageNumber);
    }

    public void searchRecipeById(String rId){
        mRecipeApiClient.searchRecipeById(rId);
    }

    public void searchNextPage(){
        searchRecipesApi(mQuery, mPageNumber + 1);
    }

    public void cancelRequest(){
        mRecipeApiClient.cancelRequest();
    }

}
