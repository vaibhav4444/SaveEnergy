package com.poc.saveenergy.myapplication.utils;

import android.app.Activity;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.poc.saveenergy.myapplication.fragments.FunctionsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by vaibhav.singhal on 5/18/2016.
 */
public class BeaconUtils {
    public static final String LOG_TAG = BeaconUtils.class.getName();
    private Activity mContext;
    private BeaconManager beaconManager;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", "B9407F30-F5F8-466E-AFF9-25556B57FE6D", 59044, 21681);
    //list will contain major id of beacons found
    private List<Integer> majors = new ArrayList<Integer>();
    private FunctionsFragment mFunctionsFragment;
    private boolean isBeaconFound = false;

    public  BeaconUtils(Activity context, FunctionsFragment functionsFragment){
        mContext = context;
        mFunctionsFragment = functionsFragment;
        // Configure BeaconManager.
        beaconManager = new BeaconManager(context);
        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);
        setupBeaconListener();
    }
    private void setupBeaconListener(){
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                Beacon bcn =  beacons.get(0);
               // postNotification(bcn.getMajor()+"Smart Office:entered region");
                Log.d("SWEETU","entered region");
//                postNotification("Entered region");
            }

            @Override
            public void onExitedRegion(Region region) {
                //postNotification("Smart Office:exited region"+region.getMajor());
                Log.d(LOG_TAG,"Exited region");
                majors.remove(region.getMajor());
            }
        });
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                // Note that results are not delivered on UI thread.
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Note that beacons reported here are already sorted by estimated
                        // distance between device and beacon.
                        //getActionBar().setSubtitle("Found beacons: " + beacons.size());
                        int count = beacons.size();
                        if(count == 1 && FunctionsFragment.isConnected == false){
                            isBeaconFound = true;
                            mFunctionsFragment.connectBT();
                        }
                        else if(count == 0 && FunctionsFragment.isConnected == true){

                        }
                        for(Beacon beacon:beacons){
                            int major = beacon.getMajor();
                            boolean ifContain = majors.contains(major);
                            if(!majors.contains(major)){
                                majors.add(major);
                                boolean temp = majors.contains(major);
                                //postNotification("beacon added to list");
                                Log.d(LOG_TAG,"Вы зашли на кухню");
                                Region region = new Region("regionId", beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());

                                setupExitListener(region);
                            }
                        }
                    }
                });
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
                    Toast.makeText(mContext, "Cannot start ranging, something terrible happened",
                            Toast.LENGTH_LONG).show();
                    Log.e(LOG_TAG, "Cannot start ranging", e);
                }
            }
        });
    }

    /**
     * Stop beacon search
     */
    public void stopBeaconSearch(){
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
        } catch (RemoteException e) {
            Log.d(LOG_TAG, "Error while stopping ranging", e);
        }

    }
}
