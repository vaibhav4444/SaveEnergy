package com.poc.saveenergy.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.poc.saveenergy.myapplication.R;

/**
 * Created by vaibhav.singhal on 4/21/2016.
 */
public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);
        //getWindow().setBackgroundDrawableResource(R.color.white);
        FrameLayout contentLayout = (FrameLayout) findViewById(R.id.content_detail);
        View contentView = getLayoutInflater().inflate(getLayoutId(), null);
        contentLayout.addView(contentView);
    }
    protected abstract int getLayoutId();
}
