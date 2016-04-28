package com.poc.saveenergy.myapplication.application;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
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
}
