package com.poc.saveenergy.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.constants.Constants;

import java.nio.charset.MalformedInputException;

/**
 * Created by vaibhav.singhal on 5/2/2016.
 */
public class SplashActivity extends Activity {
    private ImageView mImgLogo;
    private Handler mHandler;

    private Runnable myRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.e("tagE", "tagE");

        mImgLogo = (ImageView) findViewById(R.id.imgLogo);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        mImgLogo.startAnimation(rotateAnimation);
        mHandler = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
            }
        };

    }
    @Override
    protected void onResume() {

        if (mHandler != null && myRunnable != null) {
            mHandler.postDelayed(myRunnable, Constants.SPLASH_TIME_OUT);
        }
        super.onResume();
    }
    @Override
    protected void onPause() {
        if (mHandler != null && myRunnable != null) {
            mHandler.removeCallbacks(myRunnable);
        }
        super.onPause();
    }

}
