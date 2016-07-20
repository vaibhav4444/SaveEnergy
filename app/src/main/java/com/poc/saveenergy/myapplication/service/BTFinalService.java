package com.poc.saveenergy.myapplication.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.poc.saveenergy.myapplication.application.SaveEnergy;
import com.poc.saveenergy.myapplication.fragments.BluetoothOperationClass;
import com.poc.saveenergy.myapplication.utils.BeaconUtils;
import com.poc.saveenergy.myapplication.utils.Logger;
import com.poc.saveenergy.myapplication.utils.TumakuBLE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by vaibhav.singhal on 5/23/2016.
 */
public class BTFinalService extends Service implements BluetoothAdapter.LeScanCallback {
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your bluetooth devices MAC address
    private static String address = "00:06:66:76:A0:AD";
    public   static  boolean  isBluetoothConnected = false;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private BeaconUtils mBeaconUtils;
    private BluetoothSocketWrapper bluetoothSocket;
    private TumakuBLE mTumakuBLE;
    private boolean isBluetoothConnecting = false;
    private int isConnectCount = 0, isDisconnectCount = 0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        super.onCreate();
        //connectBT();
        //mBeaconUtils = new BeaconUtils(this,this);
        ((SaveEnergy)getApplication()).resetTumakuBLE();
        mTumakuBLE=((SaveEnergy)getApplication()).getTumakuBLEInstance(this);


    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TumakuBLE.setup();
        mTumakuBLE.startLeScan();
        return super.onStartCommand(intent, flags, startId);
    }
    public void connectBT(){

        if(btAdapter == null) {
            btAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            if(device != null) {
                btSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket = new NativeBluetoothSocket(btSocket);
            }

            else{
                isBluetoothConnected = false;
                isBluetoothConnecting = false;
                //connectBT();
                return;
            }

        } catch (IOException e) {
            isBluetoothConnected = false;
            isBluetoothConnecting = false;
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
            //connectBT();
            return;
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d("TAG", "...Connecting to Remote...");
        try {

            //btSocket.connect();
            bluetoothSocket.connect();
            isBluetoothConnected = true;
            isBluetoothConnecting = false;
            Log.d("TAG", "...Connection established and data link opened...");
        } catch (IOException e) {
            /*isBluetoothConnected = false;
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                //connectBT();
                return;
            } */
            //try the fallback
            try {
                bluetoothSocket = new FallbackBluetoothSocket(bluetoothSocket.getUnderlyingSocket());
                Thread.sleep(500);
                bluetoothSocket.connect();
                isBluetoothConnected = true;
                isBluetoothConnecting = false;
               // break;
            } catch (FallbackException e1) {
                Log.w("BT", "Could not initialize FallbackBluetoothSocket classes.", e);
            } catch (InterruptedException e1) {
                Log.w("BT", e1.getMessage(), e1);
            } catch (IOException e1) {
                Log.w("BT", "Fallback failed. Cancelling.", e1);
            }
        }

        // Create a data stream so we can talk to server.
        Log.d("TAG", "...Creating Socket...");

        try {
             outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
        if(isBluetoothConnected) {
             ReadInputThread readInputThread = new ReadInputThread(btSocket);
            readInputThread.start();
        }
    }
    private void errorExit(String title, String message){
        Toast msg = Toast.makeText(this,
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

                    Toast.makeText(BTFinalService.this, "Connected", Toast.LENGTH_LONG).show();

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
        try {
            //Toast.makeText(BTFinalService.this, "connection lost() called", Toast.LENGTH_LONG).show();
            if(btSocket != null)
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            //connectBT();
            return;
        }
        // no need to do this thing as will be handled by beacon
        /*while (isBluetoothConnected == false) {
            connectBT();
        } */

        BTTestService.listDevice.clear();

    }
    public void closeBtConnection(){
         Log.e("tagE", "tagE" +" within closeBT");
        if(btSocket == null){
            Toast.makeText(this, "bt socket is null", Toast.LENGTH_LONG).show();
            return;
       }
        Toast.makeText(this, "closeBtConnection() called", Toast.LENGTH_LONG).show();
        try {
            btSocket.close();
            isBluetoothConnected = false;
        } catch (IOException e2) {
           e2.printStackTrace();
            errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBeaconUtils != null){
            mBeaconUtils.stopBeaconSearch();
        }
    }

    //new code
    public static interface BluetoothSocketWrapper {

        InputStream getInputStream() throws IOException;

        OutputStream getOutputStream() throws IOException;

        String getRemoteDeviceName();

        void connect() throws IOException;

        String getRemoteDeviceAddress();

        void close() throws IOException;

        BluetoothSocket getUnderlyingSocket();

    }

    public static class NativeBluetoothSocket implements BluetoothSocketWrapper {

        private BluetoothSocket socket;

        public NativeBluetoothSocket(BluetoothSocket tmp) {
            this.socket = tmp;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return socket.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return socket.getOutputStream();
        }

        @Override
        public String getRemoteDeviceName() {
            return socket.getRemoteDevice().getName();
        }

        @Override
        public void connect() throws IOException {
            socket.connect();
        }

        @Override
        public String getRemoteDeviceAddress() {
            return socket.getRemoteDevice().getAddress();
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }

        @Override
        public BluetoothSocket getUnderlyingSocket() {
            return socket;
        }

    }
    public static class FallbackException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        public FallbackException(Exception e) {
            super(e);
        }

    }
    public class FallbackBluetoothSocket extends NativeBluetoothSocket {

        private BluetoothSocket fallbackSocket;

        public FallbackBluetoothSocket(BluetoothSocket tmp) throws FallbackException {
            super(tmp);
            try
            {
                Class<?> clazz = tmp.getRemoteDevice().getClass();
                Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};
                Method m = clazz.getMethod("createInsecureRfcommSocket", paramTypes);
                Object[] params = new Object[] {Integer.valueOf(1)};
                fallbackSocket = (BluetoothSocket) m.invoke(tmp.getRemoteDevice(), params);
            }
            catch (Exception e)
            {
                throw new FallbackException(e);
            }
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return fallbackSocket.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return fallbackSocket.getOutputStream();
        }


        @Override
        public void connect() throws IOException {
            fallbackSocket.connect();
        }


        @Override
        public void close() throws IOException {
            fallbackSocket.close();
        }

    }
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.e("tagE","tagE" +Math.abs(rssi));
        Log.e("tagE", "tagE" +rssi);

        //Toast.makeText(this, "c: "+isConnectCount+" dc: "+isDisconnectCount+" rssi: "+rssi, Toast.LENGTH_LONG).show();
        int newRssi = Math.abs(rssi);
        Log.e("tagE", "tagE" +" new rssi "+newRssi);
        if(newRssi < 80){
            Log.e("tagE", "tagE" +" count connect"+isConnectCount);
            isDisconnectCount = 0;
            isConnectCount = isConnectCount + 1;
            Log.e("tagE", "tagE" +" after increment count connect"+isConnectCount);
        }
        else if(newRssi >= 80){
             Log.e("tagE", "tagE" +" count disconnect"+isDisconnectCount);
            isConnectCount = 0;
            isDisconnectCount= isDisconnectCount + 1;
            Log.e("tagE", "tagE" +" after increment count disconnect"+isDisconnectCount);
        }
        if (isConnectCount > 3 &&BTFinalService.isBluetoothConnected == false && !isBluetoothConnecting) {
             Log.e("tagE", "tagE" +"trying to connect");
            Toast.makeText(this, "Trying to connect", Toast.LENGTH_LONG).show();
            isBluetoothConnecting = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                     connectBT();
                }
            }).start();

            //isBluetoothConnecting = false;
        }
        else if(isDisconnectCount >4){
             Log.e("tagE", "tagE" +"trying to dis-connect");
            Toast.makeText(this, "Trying to disconnect", Toast.LENGTH_LONG).show();
            closeBtConnection();
        }

        if(isDisconnectCount >40){

            isConnectCount = 0;
        }
        if(isDisconnectCount>40){
            isDisconnectCount = 0;
        }
    }



}
