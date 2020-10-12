package com.example.mvvmapijava.utils;

import android.util.Log;

import com.example.mvvmapijava.model.Recipe;

import java.util.List;

public class Testing {

    public static void printRecipes(List<Recipe> list, String tag) {
        for(Recipe recipe : list) {
            Log.d(tag, "RECIPE TITLE IS " + recipe.getTitle());
        }
    }
}
