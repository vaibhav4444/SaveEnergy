package com.poc.saveenergy.myapplication.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.poc.saveenergy.myapplication.application.SaveEnergy;
import com.poc.saveenergy.myapplication.constants.Constants;
import com.poc.saveenergy.myapplication.utils.UtilityFunctions;

/**
 * Created by Vaib on 13-04-2016.
 */
public class WiFiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            // Do your work.

            // e.g. To check the Network Name or other info:

            String ssid = UtilityFunctions.getWiFiName(context);
            if(ssid != null && ssid.contains(SaveEnergy.getInstance().getPrefs().get(Constants.PREF_KEY_WIFI_NAME).toLowerCase())){
                Toast.makeText(SaveEnergy.getInstance(), "Connected", Toast.LENGTH_LONG).show();
                UtilityFunctions.enableBT();
                Intent discoverableIntent = new
                        Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                context.startActivity(discoverableIntent);
            }

        }
        else{
            final Context context1 = context;
            new Handler().postAtTime(new Runnable() {
                @Override
                public void run() {
                    String ssid = UtilityFunctions.getWiFiName(context1);
                    if(ssid != null && ssid.contains("known")){
                        UtilityFunctions.disableBT();

                    }
                }
            }, 19000);
            Log.i("this","not connected");

        }
    }
}
