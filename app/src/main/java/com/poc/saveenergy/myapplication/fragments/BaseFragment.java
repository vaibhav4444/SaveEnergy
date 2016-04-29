package com.poc.saveenergy.myapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vaibhav.singhal on 4/29/2016.
 */
public abstract class BaseFragment extends Fragment {
    // Root view for this fragment
    private View mFragmentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(intializaLayoutId(), null);
        initViews(mFragmentView);
        return mFragmentView;
    }
    /**
     * @return returns layout id of the fragment.
     */
    protected abstract int intializaLayoutId();
    protected abstract void initViews(View mFragmentView);

}
