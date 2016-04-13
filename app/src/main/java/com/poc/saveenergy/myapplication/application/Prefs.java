package com.poc.saveenergy.myapplication.application;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vaib on 13-04-2016.
 */
public class Prefs {
    private Context context;
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public Prefs(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }
    public void get(String key){
        sharedPreferences.getString(key, null);
    }
    public void put(String key,String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
