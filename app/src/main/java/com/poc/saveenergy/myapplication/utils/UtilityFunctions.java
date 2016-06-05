package com.poc.saveenergy.myapplication.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.activity.MainActivity;

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
    public static void createNotification(Context context){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.app_icon);
        mBuilder.setSmallIcon(R.mipmap.app_icon);
        mBuilder.setLargeIcon(icon);
        mBuilder.setContentTitle(context.getResources().getString(R.string.app_name)+": "+"Health Alert");
        mBuilder.setContentText("Time to take short walk.");
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

    }
}
