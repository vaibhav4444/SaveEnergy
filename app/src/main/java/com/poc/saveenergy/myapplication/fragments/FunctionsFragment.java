package com.poc.saveenergy.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.service.BluetoothChatService;
import com.poc.saveenergy.myapplication.service.BluetoothScanService;

/**
 * Created by Vaib on 24-04-2016.
 */
public class FunctionsFragment extends BaseFragment {
    public static final String LOG_TAG = FunctionsFragment.class.getName();
    private Button btnSendData;
    private BluetoothChatService mChatService = null;
    public FunctionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), BluetoothScanService.class);
        getActivity().startService(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        Intent intent = new Intent(getActivity(), BluetoothScanService.class);
        getActivity().stopService(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatService = new BluetoothChatService(getActivity(), mHandler);
    }

    @Override
    protected int intializaLayoutId() {
        return R.layout.frag_function_layout;
    }

    @Override
    protected void initViews(final View mFragmentView) {
        btnSendData = (Button) mFragmentView.findViewById(R.id.btnSend);
            btnSendData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mChatService.write("1".getBytes());
                }
            });
    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            /*switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                        startScanService();
                    }
                    break;
            } */
        }
    };

}
