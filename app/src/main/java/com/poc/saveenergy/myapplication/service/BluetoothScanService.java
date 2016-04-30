package com.poc.saveenergy.myapplication.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Vaib on 17-04-2016.
 */
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.constants.Constants;

import java.util.ArrayList;

/**
 * Created by Vaib on 17-04-2016.
 */
public class BluetoothScanService extends Service {
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothChatService mBluetoothChatService;
    private String mote_mac = "80:6C";
    private String arduino_mac = "00:06:66:76:A0:AD";
    /**
     * Newly discovered devices
     */
    private ArrayList<String> mNewDevicesArrayList;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //mBluetoothChatService = new BluetoothChatService();
        if(mBluetoothAdapter == null){
            mBluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
        }
        registerReceiverForIntentFilter();
        mNewDevicesArrayList = new ArrayList<String>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNewDevicesArrayList.clear();
        mBluetoothChatService = new BluetoothChatService(this, mHandler);
        mBluetoothAdapter.startDiscovery();
        //if (mBluetoothChatService.getState() == BluetoothChatService.STATE_NONE) {
        // Start the Bluetooth chat services
        // mBluetoothChatService.start();
        // }
        return super.onStartCommand(intent, flags, startId);
    }
    private void registerReceiverForIntentFilter(){
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //ParcelUuid [] uudi =device.getUuids();
                //device.fetchUuidsWithSdp();
                // If it's already paired, skip it, because it's been listed already
                if(device.getAddress().contains(mote_mac)){
                    mBluetoothAdapter.cancelDiscovery();
                    mBluetoothChatService.connect(device,false);

                }
                //if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                mNewDevicesArrayList.add(device.getName() + "\n" + device.getAddress());
                //}
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (mNewDevicesArrayList.size() == 0) {
                    mBluetoothAdapter.startDiscovery();
                    //String noDevices = getResources().getText(R.string.none_found).toString();
                }
            }

        }
    };
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            Toast.makeText(BluetoothScanService.this, "coonected",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            Toast.makeText(BluetoothScanService.this, "connecting",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            Toast.makeText(BluetoothScanService.this, "listen",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;

                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    String mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);

                    Toast.makeText(BluetoothScanService.this, "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                    break;
                case Constants.MESSAGE_TOAST:

                    Toast.makeText(BluetoothScanService.this, msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                    mBluetoothAdapter.startDiscovery();
                    //startScanService();

                    break;
            }
        }
    };
}