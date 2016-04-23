package com.poc.saveenergy.myapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poc.saveenergy.myapplication.R;

/**
 * Created by Vaib on 23-04-2016.
 */
public class ConfigFragments extends Fragment {
    public static final String LOG_TAG = ConfigFragments.class.getName();
    public ConfigFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.config_frag_layout, container, false);
    }
}
