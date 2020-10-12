package com.example.mvvmapijava.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmapijava.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // UI widgets
    TextView title, publisher, socialScore;
    AppCompatImageView image;
    OnRecipeListener mOnRecipeListener;


    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {
        super(itemView);

        this.mOnRecipeListener = onRecipeListener;

        title = itemView.findViewById(R.id.recipe_title);
        publisher = itemView.findViewById(R.id.recipe_publisher);
        socialScore = itemView.findViewById(R.id.recipe_social_score);
        image = itemView.findViewById(R.id.recipe_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mOnRecipeListener.onRecipeClick(getAdapterPosition());
    }
}
