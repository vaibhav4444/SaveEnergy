package com.poc.saveenergy.myapplication.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.mxn.soul.flowingdrawer_core.FlowingView;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;
import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.fragments.MyMenuFragment;

/**
 * Created by vaibhav.singhal on 4/21/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LeftDrawerLayout mLeftDrawerLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private RecyclerView mRecyclerView;
    private View mInflatedView;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_container_coordinatorLayout);

        setupToobar();
        setupRecyclerView();
        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.drawerlayout);
        FlowingView mFlowingView = (FlowingView) findViewById(R.id.sv);
        FragmentManager fm = getSupportFragmentManager();
        MyMenuFragment mMenuFragment = (MyMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new MyMenuFragment()).commit();
            mMenuFragment.setDrawerLayout(mLeftDrawerLayout);
        }
        mLeftDrawerLayout.setFluidView(mFlowingView);
        mLeftDrawerLayout.setMenuFragment(mMenuFragment);
        //getWindow().setBackgroundDrawableResource(R.color.white);
        FrameLayout contentLayout = (FrameLayout) findViewById(R.id.content_detail);
        mInflatedView = getLayoutInflater().inflate(getLayoutId(), null);
        contentLayout.addView(mInflatedView);

    }

    protected void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.drawerList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    protected void setupToobar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftDrawerLayout.toggle();
            }
        });

        toolbar.setTitle(getResources().getString(R.string.app_name));

    }

    protected View getInflatedView() {
        return mInflatedView;
    }

    protected abstract int getLayoutId();

    public CoordinatorLayout getmCoordinatorLayout(){
        return mCoordinatorLayout;
    }
}
