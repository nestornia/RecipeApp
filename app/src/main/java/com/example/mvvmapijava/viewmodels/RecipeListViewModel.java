package com.example.mvvmapijava.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmapijava.model.Recipe;
import com.example.mvvmapijava.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuery;


    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mIsPerformingQuery = false;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }
    public LiveData<Boolean> isQueryExhausted() { return mRecipeRepository.isQueryExhausted(); }

    public void searchRecipesApi(String query, int pageNumber){
        mIsViewingRecipes = true;
        mIsPerformingQuery = true;
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }

    public boolean ismIsViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes) {
        this.mIsViewingRecipes = isViewingRecipes;
    }

    public boolean onBackPressed() {
        if (mIsPerformingQuery){
            mRecipeRepository.cancelRequest();
            mIsPerformingQuery = false;
        }
        if (mIsViewingRecipes) {
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }

    public boolean isPerformingQuery() {
        return mIsPerformingQuery;
    }

    public void setIsPerformingQuery(boolean isPerformingQuery) {
        this.mIsPerformingQuery = isPerformingQuery;
    }

    public void searchNextPage(){
        if(!mIsPerformingQuery
                && mIsViewingRecipes
                && !isQueryExhausted().getValue()){
            mRecipeRepository.searchNextPage();
        }
    }
}
