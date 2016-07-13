package com.poc.saveenergy.myapplication.application;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.poc.saveenergy.myapplication.utils.TumakuBLE;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Vaib on 13-04-2016.
 */
public class SaveEnergy extends Application {
    private Prefs prefs;
    private static SaveEnergy saveEnergy;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        saveEnergy = this;
    }
    public static SaveEnergy getInstance(){
        return saveEnergy;
    }
    public Prefs getPrefs(){
        if(prefs ==  null){
            prefs = new Prefs(this);
        }
        return  prefs;
    }
    static TumakuBLE mTumakuBLE;

    public void resetTumakuBLE(){
        TumakuBLE.resetTumakuBLE();
    }

    public TumakuBLE getTumakuBLEInstance(Context context){
        mTumakuBLE= TumakuBLE.getInstance(context);
        return mTumakuBLE;
    }
}
