package com.poc.saveenergy.myapplication.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.adapter.OnlineListAdapter;
import com.poc.saveenergy.myapplication.model.OnlineListModel;

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

/**
 * Created by Vaib on 23-04-2016.
 */
public class OnlineFragment extends Fragment {
    public static final String LOG_TAG = OnlineFragment.class.getName();
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private List<OnlineListModel> list_userStatus;
    private RecyclerView mUserStatusRecyclerView;
    public OnlineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list_userStatus = new ArrayList<OnlineListModel>();
        OnlineListModel model1 = new OnlineListModel();
        model1.setName("Tej");
        OnlineListModel model2 = new OnlineListModel();
        model2.setName("Vaibhav");
        OnlineListModel model3 = new OnlineListModel();
        model3.setName("Naval");
        list_userStatus.add(model1);
        list_userStatus.add(model2);
        list_userStatus.add(model3);



    }
    private class Task extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new String[0];
        }

        @Override protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_online, container, false);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe);
        //mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        //mWaveSwipeRefreshLayout.setWaveColor(Color.argb(100, 255, 0, 0));
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new Task().execute();
            }
        });
        prepareRecyclerView(view);

        return view;
    }
    private void prepareRecyclerView(View view){
        OnlineListAdapter  onlineListAdapter = new OnlineListAdapter(list_userStatus);
        mUserStatusRecyclerView = (RecyclerView) view.findViewById(R.id.onlineList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mUserStatusRecyclerView.setLayoutManager(mLayoutManager);
        mUserStatusRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mUserStatusRecyclerView.setAdapter(onlineListAdapter);
    }
}
