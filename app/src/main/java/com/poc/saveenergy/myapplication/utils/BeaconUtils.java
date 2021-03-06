package com.poc.saveenergy.myapplication.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.poc.saveenergy.myapplication.activity.MainActivity;
import com.poc.saveenergy.myapplication.fragments.BluetoothOperationClass;
import com.poc.saveenergy.myapplication.service.BTFinalService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by vaibhav.singhal on 5/18/2016.
 */
public class BeaconUtils {
    public static final String LOG_TAG = BeaconUtils.class.getName();
    private BTFinalService mContext;
    private static BeaconManager beaconManager;
    //private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", "B9407F30-F5F8-466E-AFF9-25556B57FE6D", 59044, 21681);
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", "b9407f30-f5f8-466e-aff9-25556b57fe6d", 41427, 55359);
    //list will contain major id of beacons found
    private List<Integer> majors = new ArrayList<Integer>();
    //private BluetoothOperationClass mBluetoothOperationClass;
    private BluetoothAdapter btAdapter;
    private BTFinalService mBTFinalService;
    private boolean isBluetoothConnecting = false;
    private static int mCount;
    private int mCountZero = 0;


    public  BeaconUtils(BTFinalService context, BTFinalService service){
        mContext = context;
        mBTFinalService = service;
        //mBluetoothOperationClass = bluetoothOperationClass;
        if(btAdapter == null){
            btAdapter =  BluetoothAdapter.getDefaultAdapter();
        }
        // Configure BeaconManager.
        beaconManager = new BeaconManager(mBTFinalService);
        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);
        setupBeaconListener();
        connectToService();
    }
    private void setupBeaconListener(){
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                Beacon bcn =  beacons.get(0);
               // postNotification(bcn.getMajor()+"Smart Office:entered region");
                Log.d("SWEETU","entered region");
//                postNotification("Entered region");
                if(BTFinalService.isBluetoothConnected == false && btAdapter.isEnabled() && !isBluetoothConnecting){
                    isBluetoothConnecting = true;
                    //mBTFinalService.connectBT();
                    isBluetoothConnecting = false;
                }
            }

            @Override
            public void onExitedRegion(Region region) {
                //postNotification("Smart Office:exited region"+region.getMajor());
                Log.d(LOG_TAG,"Exited region");
                majors.remove(region.getMajor());
                /*try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } */
                Log.i("tagE**","tagE** on exit before disconnect"+mCountZero);
                if(BTFinalService.isBluetoothConnected == true && mCountZero > 7){
                    Log.i("tagE**", "tagE** on exit disconnect called" + mCountZero);
                    Toast.makeText(mContext, "Trying to disconnect", Toast.LENGTH_LONG).show();
                    //mBTFinalService.closeBtConnection();
                }
            }
        });
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                // Note that results are not delivered on UI thread.
                // mBTFinalService.runOnUiThread(new Runnable() {
                // @Override
                // public void run() {
                // Note that beacons reported here are already sorted by estimated
                // distance between device and beacon.
                //getActionBar().setSubtitle("Found beacons: " + beacons.size());
                int count = beacons.size();
                mCount = beacons.size();
                if (count == 1) {
                    mCountZero = 0;
                    Log.i("tagE**", "tagE**From if count == 1");
                }

                if (count == 1 && BTFinalService.isBluetoothConnected == false && !isBluetoothConnecting) {
                    Toast.makeText(mContext, "Trying to connect", Toast.LENGTH_LONG).show();
                    isBluetoothConnecting = true;
                    mBTFinalService.connectBT();
                    isBluetoothConnecting = false;
                }
                if (count == 0) {
                    mCountZero++;
                    if(mCountZero == 8){
                        mBTFinalService.closeBtConnection();
                    }
                    Log.i("tagE**", "tagE** zero:" + mCount);
                }

                for (Beacon beacon : beacons) {
                    int major = beacon.getMajor();
                    Log.i("tagE**", "tagE** rssi:" + beacon.getRssi());
                            /*if(major == 59044){
                                if(BTFinalService.isBluetoothConnected == false && !isBluetoothConnecting){
                                    isBluetoothConnecting = true;
                                    mBTFinalService.connectBT();
                                    isBluetoothConnecting = false;
                                }
                            } */

                    boolean ifContain = majors.contains(major);
                    if (!majors.contains(major)) {
                        majors.add(major);
                        boolean temp = majors.contains(major);
                        //postNotification("beacon added to list");
                        Log.d(LOG_TAG, "Вы зашли на кухню");
                        Region region1 = new Region("regionId", beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());

                        setupExitListener(region1);
                    }
                }
                //}
                //});
            }
        });
    }
    public void setupExitListener(Region region) {
        final Region region1 = region;
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startMonitoring(region1);
                } catch (RemoteException e) {
                    Log.d(LOG_TAG, "Error while starting monitoring");
                }
            }
        });
    }

    /**
     * Will start Scanning for becone
     */
    public  void connectToService() {
        //  getActionBar().setSubtitle("Scanning...");
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
                } catch (RemoteException e) {
                    Toast.makeText(mBTFinalService, "Cannot start ranging, something terrible happened",
                            Toast.LENGTH_LONG).show();
                    Log.e(LOG_TAG, "Cannot start ranging", e);
                }
            }
        });
    }

    /**
     * Stop beacon search
     */
    public  void stopBeaconSearch(){
        if(beaconManager == null){
            return;
        }
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
        } catch (RemoteException e) {
            Log.d(LOG_TAG, "Error while stopping ranging", e);
        }

    }
}
