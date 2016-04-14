package com.poc.saveenergy.myapplication.application;

import android.app.Application;

/**
 * Created by Vaib on 13-04-2016.
 */
public class SaveEnergy extends Application {
    private Prefs prefs;
    private static SaveEnergy saveEnergy;
    @Override
    public void onCreate() {
        super.onCreate();
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
