package com.poc.saveenergy.myapplication.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.activity.MainActivity;
import com.poc.saveenergy.myapplication.application.Prefs;
import com.poc.saveenergy.myapplication.application.SaveEnergy;
import com.poc.saveenergy.myapplication.constants.Constants;
import com.poc.saveenergy.myapplication.utils.UtilityFunctions;

/**
 * Created by Vaib on 23-04-2016.
 */
public class ConfigFragments extends BaseFragment {
    public static final String LOG_TAG = ConfigFragments.class.getName();
    private EditText editText_WiFiName;
    private EditText editText_username;
    private EditText editText_password;
    private TextInputLayout txt_wifi, txt_username, txt_password;
    private Prefs prefs;

    private Button btn_save;
    private MainActivity mMainActivity;
    public ConfigFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs =  SaveEnergy.getInstance().getPrefs();
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof MainActivity){
            mMainActivity = (MainActivity) activity;
        }
    }

    @Override
    protected void initViews(View mFragmentView) {
        editText_WiFiName = (EditText) mFragmentView.findViewById(R.id.edtWifiName);
        editText_username = (EditText) mFragmentView.findViewById(R.id.edt_username);
        editText_password = (EditText) mFragmentView.findViewById(R.id.edt_password);
        txt_wifi = (TextInputLayout)mFragmentView.findViewById(R.id.txt_wifi);
        txt_username = (TextInputLayout)mFragmentView.findViewById(R.id.txt_username);
        txt_password = (TextInputLayout)mFragmentView.findViewById(R.id.txt_password);
        btn_save = (Button) mFragmentView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetails();
            }
        });
        fillEditText();
    }

    /**
     * REad detailsfrom pref & set in edit text
     */
    private void fillEditText(){
        if(!TextUtils.isEmpty(prefs.get(Constants.PREF_KEY_USERNAME))){
            editText_username.setText(prefs.get(Constants.PREF_KEY_USERNAME));
        }
        if(!TextUtils.isEmpty(prefs.get(Constants.PREF_KEY_WIFI_NAME))){
            editText_WiFiName.setText(prefs.get(Constants.PREF_KEY_WIFI_NAME));
        }
        if(!TextUtils.isEmpty(prefs.get(Constants.PREF_KEY_PASSWORD))){
            editText_password.setText(prefs.get(Constants.PREF_KEY_PASSWORD));
        }
    }
    /**
     * save details in preference
     */
    private void saveDetails(){
       if(isError()){
           return;
       }


        UtilityFunctions.hideKeyboard(getActivity(), btn_save);
        prefs.put(Constants.PREF_KEY_WIFI_NAME, editText_WiFiName.getText().toString());
        prefs.put(Constants.PREF_KEY_USERNAME, editText_username.getText().toString());
        prefs.put(Constants.PREF_KEY_PASSWORD, editText_password.getText().toString());
        Snackbar snackbar = Snackbar
                .make(mMainActivity.getmCoordinatorLayout(), mMainActivity.getResources().getString(R.string.wifiSaved)+SaveEnergy.getInstance().getPrefs().get(Constants.PREF_KEY_WIFI_NAME), Snackbar.LENGTH_LONG);
        snackbar.show();

    }
    private boolean isError(){
        boolean isError = false;
        if(TextUtils.isEmpty(editText_WiFiName.getText().toString())){
            editText_WiFiName.setError(getResources().getString(R.string.error_wifiname));
            isError = true;
        }
        if(TextUtils.isEmpty(editText_username.getText().toString())){
            editText_WiFiName.setError(getResources().getString(R.string.error_username));
            isError = true;
        }
        if(TextUtils.isEmpty(editText_password.getText().toString())){
            editText_WiFiName.setError(getResources().getString(R.string.error_password));
            isError = true;
        }
        return isError;
    }
}
