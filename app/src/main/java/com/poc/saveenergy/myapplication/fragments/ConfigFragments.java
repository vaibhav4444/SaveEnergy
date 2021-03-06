package com.poc.saveenergy.myapplication.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

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
    private CheckBox chkShowHidePassword;
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
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
       builder1.setIcon(R.mipmap.app_icon);
        builder1.setTitle("Health Alert");
        builder1.setMessage("It's been longer than 30 minutes, you should probably take short walk.");

        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        //alert11.show();

        editText_WiFiName = (EditText) mFragmentView.findViewById(R.id.edtWifiName);
        editText_username = (EditText) mFragmentView.findViewById(R.id.edt_username);
        editText_password = (EditText) mFragmentView.findViewById(R.id.edt_password);

       // txt_wifi = (TextInputLayout)mFragmentView.findViewById(R.id.txt_wifi);
        //txt_username = (TextInputLayout)mFragmentView.findViewById(R.id.txt_username);
        //txt_password = (TextInputLayout)mFragmentView.findViewById(R.id.txt_password);
        btn_save = (Button) mFragmentView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetails();
            }
        });
        chkShowHidePassword = (CheckBox) mFragmentView.findViewById(R.id.checkBoxShow);
        chkShowHidePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    editText_password.setTransformationMethod(new PasswordTransformationMethod());;
                } else {
                    //show password as plain text
                    editText_password.setTransformationMethod(null);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        fillEditText();
        UtilityFunctions.hideKeyboard(getActivity(), editText_WiFiName);
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
        UtilityFunctions.createNotification(getActivity());
       if(isError()){
           return;
       }


        UtilityFunctions.hideKeyboard(getActivity(), btn_save);
        prefs.put(Constants.PREF_KEY_WIFI_NAME, editText_WiFiName.getText().toString());
        prefs.put(Constants.PREF_KEY_USERNAME, editText_username.getText().toString());
        prefs.put(Constants.PREF_KEY_PASSWORD, editText_password.getText().toString());
       /* Snackbar snackbar = Snackbar
                .make(mMainActivity.getmCoordinatorLayout(), R.string.de, Snackbar.LENGTH_LONG);
        snackbar.show(); */
        Toast.makeText(getActivity(), R.string.detailSaved, Toast.LENGTH_LONG).show();

    }
    private boolean isError(){
        boolean isError = false;
        if(TextUtils.isEmpty(editText_WiFiName.getText().toString())){
            editText_WiFiName.setError(getResources().getString(R.string.error_wifiname));
            isError = true;
        }
        if(TextUtils.isEmpty(editText_username.getText().toString())){
            editText_username.setError(getResources().getString(R.string.error_username));
            isError = true;
        }
        if(TextUtils.isEmpty(editText_password.getText().toString())){
            editText_password.setError(getResources().getString(R.string.error_password));
            isError = true;
        }
        return isError;
    }

    @Override
    public void onPause() {
        super.onPause();
        editText_WiFiName.setError(null);
        editText_username.setError(null);
        editText_password.setError(null);
        editText_WiFiName.setText("");
        editText_username.setText("");
        editText_password.setText("");
    }
}
