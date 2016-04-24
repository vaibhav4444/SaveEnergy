package com.poc.saveenergy.myapplication.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.adapter.ViewPagerAdapter;
import com.poc.saveenergy.myapplication.application.SaveEnergy;
import com.poc.saveenergy.myapplication.constants.Constants;
import com.poc.saveenergy.myapplication.fragments.ConfigFragments;
import com.poc.saveenergy.myapplication.fragments.FunctionsFragment;
import com.poc.saveenergy.myapplication.fragments.OnlineFragment;
import com.poc.saveenergy.myapplication.receiver.BluetoothDeviceReceiver;

/*
links:http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/
 */

public class MainActivity extends BaseActivity {
    private EditText editText_WiFiName;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editText_WiFiName = (EditText) findViewById(R.id.edtWifiName);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //Assigns the ViewPager to TabLayout.
        tabLayout.setupWithViewPager(viewPager);
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
    public void onClick(View v){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter intFilter = new IntentFilter();
        intFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(new BluetoothDeviceReceiver(), intFilter);
        mBluetoothAdapter.startDiscovery();
        if(TextUtils.isEmpty(editText_WiFiName.getText().toString())){
            editText_WiFiName.setError("Enter wifi name");
            return;
        }
        SaveEnergy.getInstance().getPrefs().put(Constants.PREF_KEY_WIFI_NAME, editText_WiFiName.getText().toString());
        Toast.makeText(this, "Saved:"+SaveEnergy.getInstance().getPrefs().get(Constants.PREF_KEY_WIFI_NAME), Toast.LENGTH_LONG).show();

    }

    /**
     * Defines the number of tabs by setting appropriate fragment and tab name.
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ConfigFragments(), Constants.FRAGMENT_CONFIG);
        adapter.addFragment(new FunctionsFragment(), Constants.FRAGMENT_FUNCTION);
        adapter.addFragment(new OnlineFragment(), Constants.FRAGMENT_ONLINE);
        viewPager.setAdapter(adapter);
    }
}