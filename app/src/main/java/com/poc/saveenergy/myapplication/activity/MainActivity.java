package com.poc.saveenergy.myapplication.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.adapter.ViewPagerAdapter;
import com.poc.saveenergy.myapplication.constants.Constants;
import com.poc.saveenergy.myapplication.fragments.ConfigFragments;
import com.poc.saveenergy.myapplication.fragments.OnlineFragment;
import com.poc.saveenergy.myapplication.service.BTFinalService;
import com.poc.saveenergy.myapplication.service.BTTestService;
import com.poc.saveenergy.myapplication.utils.BeaconUtils;
import com.poc.saveenergy.myapplication.utils.Logger;
import com.poc.saveenergy.myapplication.utils.UtilityFunctions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/*
links:http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/
 */

public class MainActivity extends BaseActivity {

    public static final String LOG_TAG = MainActivity.class.getName();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your bluetooth devices MAC address
    private static String address = "00:06:66:76:A0:AD";
    public static boolean isBluetoothConnected = false;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private BeaconUtils mBeaconUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        startBluetoothOperation();
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //Assigns the ViewPager to TabLayout.
        tabLayout.setupWithViewPager(viewPager);
        UtilityFunctions.enableBT(this);
        //mBeaconUtils = new BeaconUtils(this);
        startService(new Intent(this, BTFinalService.class));


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Defines the number of tabs by setting appropriate fragment and tab name.
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ConfigFragments(), Constants.FRAGMENT_CONFIG);
       // adapter.addFragment(new FunctionsFragment(), Constants.FRAGMENT_FUNCTION);
        adapter.addFragment(new OnlineFragment(), Constants.FRAGMENT_ONLINE);
        viewPager.setAdapter(adapter);
    }
    private void startBluetoothOperation(){
        //BluetoothOperationClass bluetoothOperationClass = new BluetoothOperationClass(this);
        //bluetoothOperationClass.connectBT();

    }
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
                connectBT();
                return;
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
        // no need to do this thing as will be handled by beacon
        /*while (isBluetoothConnected == false) {
            connectBT();
        } */

        BTTestService.listDevice.clear();

    }
    public void closeBtConnection(){
        if(btSocket == null){
            Toast.makeText(this, "bt socket is null", Toast.LENGTH_LONG).show();
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
