package com.poc.saveenergy.myapplication.constants;

/**
 * Created by Vaib on 13-04-2016.
 */
public class Constants {
    public static int SPLASH_TIME_OUT = 1000;
    public static final String PREF_KEY_WIFI_NAME = "wifiname";
    public static final String PREF_KEY_USERNAME = "username";
    public static final String PREF_KEY_PASSWORD = "password";
    public static final String FRAGMENT_CONFIG = "Config";
    public static final String FRAGMENT_ONLINE = "Status";
    public static final String FRAGMENT_FUNCTION = "Functions";

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    //MongoLab Constants
    private static final String MONGO_KEY = "veTqID_gkb74tG-yL4MGcS1p2RRBP1Pf";
    public static final String MOGO_URL = "https://api.mongolab.com/api/1/databases/geolocation/collections/onlinedb?apiKey=" + MONGO_KEY;


}
