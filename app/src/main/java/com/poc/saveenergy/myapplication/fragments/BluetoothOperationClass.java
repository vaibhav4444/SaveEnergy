package com.poc.saveenergy.myapplication.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.interfaces.AsyncResponse;
import com.poc.saveenergy.myapplication.service.BTTestService;
import com.poc.saveenergy.myapplication.service.BluetoothChatService;
import com.poc.saveenergy.myapplication.utils.BeaconUtils;
import com.poc.saveenergy.myapplication.utils.Logger;
import com.poc.saveenergy.myapplication.utils.MongoLabUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Vaib on 24-04-2016.
 */
public class BluetoothOperationClass{
    public static final String LOG_TAG = BluetoothOperationClass.class.getName();
    //private Button btnSendOne, btnSendZero, btn_startService, btn_stopService;
    private BluetoothChatService mChatService = null;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private BTTestService btTestService;
    private static BluetoothDevice device;
    public static boolean isBluetoothConnected = false;
    private Activity mActivity;
    private BeaconUtils mBeaconUtils;

    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your bluetooth devices MAC address
    private static String address = "00:06:66:76:A0:AD";
    public BluetoothOperationClass(Activity activity) {
        mActivity = activity;
        // = new BeaconUtils(mActivity, this);
    }
    public BluetoothOperationClass() {

    }

    /*@Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatService = new BluetoothChatService(getActivity(), mHandler);
        btTestService = new BTTestService();
    }

    @Override
    protected int intializaLayoutId() {
        return R.layout.frag_function_layout;
    }

    @Override
    protected void initViews(final View mFragmentView) {
        btnSendOne = (Button) mFragmentView.findViewById(R.id.btn_one);
        btnSendOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MongoLabUtil().getData(new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        Log.i("", "output:"+output);
                    }
                });
            }
        });
        btnSendZero = (Button) mFragmentView.findViewById(R.id.btn_zero);
        btnSendZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btTestService.sendData("X".getBytes());
                //sendData("X".getBytes());
            }
        });
        btn_startService = (Button) mFragmentView.findViewById(R.id.startService);
        btn_startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BTTestService.class);
                getActivity().startService(intent);
            }
        });
        btn_stopService = (Button) mFragmentView.findViewById(R.id.stopService);
        btn_stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BTTestService.class);
                getActivity().stopService(intent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }

        try     {
            if(btSocket != null)
                btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    } */

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //FragmentActivity activity = getActivity();

        }
    };
    public void connectBT(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            if(device != null)
                btSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            else{
                isBluetoothConnected = false;
                connectBT();
                return;
            }

        } catch (IOException e) {
            isBluetoothConnected = false;
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
            connectBT();
            return;
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d("TAG", "...Connecting to Remote...");
        try {
            btSocket.connect();
            isBluetoothConnected = true;
            Log.d("TAG", "...Connection established and data link opened...");
        } catch (IOException e) {
            isBluetoothConnected = false;
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d("TAG", "...Creating Socket...");

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
        ReadInputThread readInputThread = new ReadInputThread(btSocket);
        readInputThread.start();
    }
    private void errorExit(String title, String message){
        Toast msg = Toast.makeText(mActivity,
                title + " - " + message, Toast.LENGTH_SHORT);
        msg.show();

    }
    private class ReadInputThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        public ReadInputThread(BluetoothSocket socket){
            InputStream tmpIn = null;
            OutputStream tmpOut = null;


            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();

            } catch (IOException e) {
                Logger.error("TAG", "temp sockets not created", e);
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            Logger.debug("TAG", "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);



                } catch (IOException e) {
                    Logger.error("TAG", "disconnected", e);
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
        isBluetoothConnected = false;
        while (isBluetoothConnected == false) {
            connectBT();
        }

        BTTestService.listDevice.clear();

    }
    public void closeBtConnection(){
        if(btSocket == null){
            Log.e(LOG_TAG, "bt socket is null");
            //return;
        }
        try {
            btSocket.close();
            isBluetoothConnected = false;
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
        }
    }



}