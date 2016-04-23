package com.poc.saveenergy.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.poc.saveenergy.myapplication.R;

/**
 * Created by vaibhav.singhal on 4/21/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);
        //getWindow().setBackgroundDrawableResource(R.color.white);
        FrameLayout contentLayout = (FrameLayout) findViewById(R.id.content_detail);
        View contentView = getLayoutInflater().inflate(getLayoutId(), null);
        contentLayout.addView(contentView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
    }
    protected abstract int getLayoutId();
}
