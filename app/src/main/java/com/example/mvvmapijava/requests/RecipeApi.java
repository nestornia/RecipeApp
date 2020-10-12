package com.example.mvvmapijava.requests;

import com.example.mvvmapijava.requests.responses.RecipeResponse;
import com.example.mvvmapijava.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    // SEARCH
    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(
            @Query("q") String query,
            @Query("page") String page
    );

    //GET
    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("rId") String rId
    );
}
