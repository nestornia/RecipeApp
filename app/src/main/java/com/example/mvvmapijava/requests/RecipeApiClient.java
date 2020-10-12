package com.example.mvvmapijava.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvmapijava.AppExecutors;
import com.example.mvvmapijava.model.Recipe;
import com.example.mvvmapijava.requests.responses.RecipeResponse;
import com.example.mvvmapijava.requests.responses.RecipeSearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.mvvmapijava.utils.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private MutableLiveData<Recipe> mRecipe;
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;
    private RetrieveRecipeRunnable mRetrieveRecipeRunnable;
    private MutableLiveData<Boolean> mRecipeRequestTimeout = new MutableLiveData<>();

    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {

        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<>();
    }


    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }
    public LiveData<Recipe> getRecipe() {
        return mRecipe;
    }
    public LiveData<Boolean> isRecipeRequestTimedOut () { return mRecipeRequestTimeout;}

    public void searchRecipesApi(String query, int pageNumber) {
        if (mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable = null;
        }
        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query, pageNumber);
        final Future handleTimeout= AppExecutors.getInstance().networkIO().submit(mRetrieveRecipesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // let the user know its timed out
                Log.d(TAG, "run: TIMEOUT GET LIST OF RECIPES");
                handleTimeout.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void searchRecipeById(String rId) {
        if(mRetrieveRecipeRunnable != null){
            mRetrieveRecipeRunnable = null;
        }
        mRetrieveRecipeRunnable = new RetrieveRecipeRunnable(rId);
        final Future handleTimeout = AppExecutors.getInstance().networkIO().submit(mRetrieveRecipeRunnable);
        mRecipeRequestTimeout.setValue(false);
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "run: TIMEOUT GET RECIPE BY ID");
                mRecipeRequestTimeout.postValue(true);
                handleTimeout.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveRecipesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200){
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());
                    if(pageNumber == 1){
                        mRecipes.postValue(list);
                    }
                    else {
                        List<Recipe> currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(list);
                        mRecipes.postValue(currentRecipes);
                    }
                }
                else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mRecipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }

        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber){
            return RetrofitInstance.getRecipeApi().searchRecipe(
                    query,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: canceling the request");
            cancelRequest = true;
        }
    }

    private class RetrieveRecipeRunnable implements Runnable {

        private String rId;
        private boolean cancelRequest;

        public RetrieveRecipeRunnable(String recipeId) {
            this.rId = recipeId;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipe(rId).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    Recipe recipe = ((RecipeResponse) response.body()).getRecipe();
                    mRecipe.postValue(recipe);
                }
                else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mRecipe.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipe.postValue(null);
            }

        }

        private Call<RecipeResponse> getRecipe(String rId){
            return RetrofitInstance.getRecipeApi().getRecipe(rId);
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: canceling the request");
            cancelRequest = true;
        }
    }
    
    public void cancelRequest(){
        if (mRetrieveRecipesRunnable != null) {
            mRetrieveRecipesRunnable.cancelRequest();
        }
        if (mRetrieveRecipeRunnable != null) {
            mRetrieveRecipeRunnable.cancelRequest();
        }
    }
}
