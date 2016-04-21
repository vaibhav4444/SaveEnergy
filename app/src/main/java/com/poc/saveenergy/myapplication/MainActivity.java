package com.poc.saveenergy.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.poc.saveenergy.myapplication.application.SaveEnergy;
import com.poc.saveenergy.myapplication.constants.Constants;
import com.poc.saveenergy.myapplication.receiver.BluetoothDeviceReceiver;

public class MainActivity extends AppCompatActivity {
    private EditText editText_WiFiName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText_WiFiName = (EditText) findViewById(R.id.edtWifiName);
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
}
