package com.poc.saveenergy.myapplication.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by vaibhav.singhal on 4/14/2016.
 */
public class UtilityFunctions {
    private static BluetoothAdapter mBluetoothAdapter;
    /*********************************Bluetooth functions ****************************************************/
    public static void enableBT(Context context){
        initialiseBluetoothAdapterInstance();
        if (!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
        }
        enableDeviceBluetoothDiscovery(context);
    }
    public static void disableBT(){
        initialiseBluetoothAdapterInstance();
        if (mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();
        }
    }
    public static String getWiFiName(Context context){
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID().toLowerCase();
    }
    private static void enableDeviceBluetoothDiscovery(Context context){
        initialiseBluetoothAdapterInstance();
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(discoverableIntent);
        }
    }
    private static BluetoothAdapter initialiseBluetoothAdapterInstance(){
        if(mBluetoothAdapter == null){
            mBluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
        }
        return mBluetoothAdapter;
    }
    /********************************************************************************************************/
    /**
     * Hide Keyboard on View Called From
     *
     * @param mContext
     * @param view     View on which to hide Soft Keyboard
     */
    public static void hideKeyboard(Context mContext, View view) {
        if (mContext != null && view != null) {
            final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
