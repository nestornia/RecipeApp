package com.example.mvvmapijava;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

abstract public class BaseActivity extends AppCompatActivity {

    public ProgressBar mProgressBar;

    @Override
    public void setContentView(int layoutResID) {
        ConstraintLayout constraintlayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = constraintlayout.findViewById(R.id.activity_content);
        mProgressBar = constraintlayout.findViewById(R.id.progress_bar);

        getLayoutInflater().inflate(layoutResID, frameLayout, true);

        super.setContentView(constraintlayout);
    }

    public void setProgressBar(boolean visibility){
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}
