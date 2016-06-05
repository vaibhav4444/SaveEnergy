package com.poc.saveenergy.myapplication.fragments;

import android.app.SearchManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
 * links:http://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview
 */
public class OnlineFragment extends Fragment {
    public static final String LOG_TAG = OnlineFragment.class.getName();
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private List<OnlineListModel> list_userStatus, list_userCopy;
    private RecyclerView mUserStatusRecyclerView;
    private OnlineListAdapter  onlineListAdapter ;

    public OnlineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list_userStatus = new ArrayList<OnlineListModel>();
        list_userCopy = new ArrayList<OnlineListModel>();
        OnlineListModel model1 = new OnlineListModel();
        model1.setName("Tej");
        model1.setIsOnline(true);
        model1.setImageResource(R.drawable.tej);
        OnlineListModel model2 = new OnlineListModel();
        model2.setName("Vaibhav");
        model2.setIsOnline(false);
        model2.setImageResource(R.drawable.vaib);
        OnlineListModel model3 = new OnlineListModel();
        model3.setName("Naval");
        model3.setIsOnline(true);
        model3.setImageResource(R.drawable.naval);
        OnlineListModel model4 = new OnlineListModel();
        model4.setName("Ajit");
        model4.setIsOnline(true);
        OnlineListModel model5 = new OnlineListModel();
        model5.setName("Mangal");
        model5.setIsOnline(false);
        OnlineListModel model6 = new OnlineListModel();
        model6.setName("Alok");
        model6.setIsOnline(false);
        OnlineListModel model7= new OnlineListModel();
        model7.setName("Tarun");
        list_userStatus.add(model1);
        list_userStatus.add(model2);
        list_userStatus.add(model3);
        list_userStatus.add(model4);
        list_userStatus.add(model5);
        list_userStatus.add(model6);
        list_userStatus.add(model7);
        list_userCopy.addAll(list_userStatus);



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
        setHasOptionsMenu(true);
        return view;
    }
    private void prepareRecyclerView(View view){
        onlineListAdapter = new OnlineListAdapter(list_userStatus);
        mUserStatusRecyclerView = (RecyclerView) view.findViewById(R.id.onlineList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mUserStatusRecyclerView.setLayoutManager(mLayoutManager);
        mUserStatusRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mUserStatusRecyclerView.setAdapter(onlineListAdapter);
    }
    //http://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_with_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getApplicationContext().getSystemService(getActivity().SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                if(TextUtils.isEmpty(newText)){
                    onlineListAdapter.animateTo(list_userCopy);
                    mUserStatusRecyclerView.scrollToPosition(0);
                    return true;
                }
                final List<OnlineListModel> filteredModelList = filter(list_userStatus, newText);
                onlineListAdapter.animateTo(filteredModelList);
                mUserStatusRecyclerView.scrollToPosition(0);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
    }
    private List<OnlineListModel> filter(List<OnlineListModel> models, String query) {
        query = query.toLowerCase();

        final List<OnlineListModel> filteredModelList = new ArrayList<>();
        for (OnlineListModel model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
