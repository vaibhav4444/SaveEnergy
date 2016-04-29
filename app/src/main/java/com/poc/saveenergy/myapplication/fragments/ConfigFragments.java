package com.poc.saveenergy.myapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.application.SaveEnergy;
import com.poc.saveenergy.myapplication.constants.Constants;
import com.poc.saveenergy.myapplication.utils.UtilityFunctions;

/**
 * Created by Vaib on 23-04-2016.
 */
public class ConfigFragments extends BaseFragment {
    public static final String LOG_TAG = ConfigFragments.class.getName();
    private EditText editText_WiFiName;
    private Button btn_save;
    public ConfigFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Will provide layout for fragment
     * @return
     */
    @Override
    protected int intializaLayoutId() {
        return R.layout.config_frag_layout;
    }

    @Override
    protected void initViews(View mFragmentView) {
        editText_WiFiName = (EditText) mFragmentView.findViewById(R.id.edtWifiName);
        btn_save = (Button) mFragmentView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWifiDetails();
            }
        });
    }
    private void saveWifiDetails(){
        if(TextUtils.isEmpty(editText_WiFiName.getText().toString())){
            editText_WiFiName.setError("Enter wifi name");
            return;
        }
        UtilityFunctions.hideKeyboard(getActivity(), btn_save);
        SaveEnergy.getInstance().getPrefs().put(Constants.PREF_KEY_WIFI_NAME, editText_WiFiName.getText().toString());
        Toast.makeText(getActivity(), "Saved:"+SaveEnergy.getInstance().getPrefs().get(Constants.PREF_KEY_WIFI_NAME), Toast.LENGTH_LONG).show();

    }
}
