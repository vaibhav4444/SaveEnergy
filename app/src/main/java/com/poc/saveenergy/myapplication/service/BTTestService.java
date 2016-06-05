package com.poc.saveenergy.myapplication.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.poc.saveenergy.myapplication.application.SaveEnergy;
import com.poc.saveenergy.myapplication.fragments.BluetoothOperationClass;
import com.poc.saveenergy.myapplication.utils.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Vaib on 01-05-2016.
 */
public class BTTestService extends Service {
    private String TAG = BTTestService.class.getName();
    private BluetoothAdapter btAdapter;
    private BluetoothChatService mBluetoothChatService;
    private String mote_mac = "80:6C";
    private String arduino_mac= "00:06:66:76:A0:AD";
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private ReadInputThread readInputThread;
    private BluetoothOperationClass bluetoothOperationClass = null;
    public static ArrayList<Integer> listDevice = new ArrayList<Integer>();

    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your bluetooth devices MAC address
    private static String address = "00:06:66:76:A0:AD";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //mBluetoothChatService = new BluetoothChatService();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(btAdapter == null){
            btAdapter =  BluetoothAdapter.getDefaultAdapter();
        }
        if(bluetoothOperationClass == null){
            bluetoothOperationClass = new BluetoothOperationClass();
        }
       registerReceiverForIntentFilter();
        listDevice.clear();
        btAdapter.startDiscovery();
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
                if(device.getAddress().contains(arduino_mac)){
                    btAdapter.cancelDiscovery();
                    listDevice.add(1);
                    //connectBT();
                    bluetoothOperationClass.connectBT();

                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if(listDevice.size() == 0){
                    btAdapter.startDiscovery();
                }
            }

        }
    };
    private void connectBT(){

        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            btSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d("TAG", "...Connecting to Remote...");
        try {
            btSocket.connect();
            Log.d("TAG", "...Connection established and data link opened...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }
        if(readInputThread != null){
            readInputThread = null;
        }
        readInputThread= new ReadInputThread(btSocket);
        //readInputThread.run();
        // Create a data stream so we can talk to server.
        Log.d("TAG", "...Creating Socket...");

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
        Log.i(TAG,"stream");
    }
    private void errorExit(String title, String message){
        Toast msg = Toast.makeText(this,
                title + " - " + message, Toast.LENGTH_SHORT);
        msg.show();
        //finish();
    }
    public void sendData(byte[] buffer) {

        if(outStream == null){
            if(btSocket != null){
                try {
                    outStream = btSocket.getOutputStream();
                } catch (IOException e) {
                    errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
                }
            }
            Toast.makeText(SaveEnergy.getInstance(),"outstream null", Toast.LENGTH_LONG).show();
            //return;
        }
        Log.d("tag", "b:" + buffer.toString());
        try {
            outStream.write(buffer);

            // Share the sent message back to the UI Activity
            //mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer)
            //  .sendToTarget();
        } catch (IOException e) {
            Log.e("TAG", "Exception during write", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }

        try     {
            if(btSocket != null) {
                btSocket.close();
            }
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }
    private class ReadInputThread extends Thread {
        private final InputStream mmInStream;
        public ReadInputThread(BluetoothSocket socket){
            InputStream tmpIn = null;


            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Logger.error(TAG, "temp sockets not created", e);
            }
            mmInStream = tmpIn;
        }
        public void run() {
            Logger.debug(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);



                } catch (IOException e) {
                    Logger.error(TAG, "disconnected", e);
                    listDevice.clear();
                    connectionLost();
                    // Start the service over to restart listening mode
                    // BluetoothChatService.this.start();
                    break;
                }
            }
        }

    }
    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
       btAdapter.startDiscovery();
    }
}
