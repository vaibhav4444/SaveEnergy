package com.poc.saveenergy.myapplication.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.poc.saveenergy.myapplication.service.BTTestService;

/**
 * Created by Vaib on 01-05-2016.
 */
public class BluetoothStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                    == BluetoothAdapter.STATE_ON) {
                    Intent intent1 = new Intent(context, BTTestService.class);
                context.startService(intent1);
            }
            else if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                    == BluetoothAdapter.STATE_OFF){
                Intent intent1 = new Intent(context, BTTestService.class);
                context.stopService(intent1);
            }
        }
    }
}
