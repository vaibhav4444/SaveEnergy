package com.poc.saveenergy.myapplication.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);
        setupToobar();
        setupRecyclerView();
        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.drawerlayout);
        FlowingView mFlowingView = (FlowingView) findViewById(R.id.sv);


        FragmentManager fm = getSupportFragmentManager();
        MyMenuFragment mMenuFragment = (MyMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new MyMenuFragment()).commit();
        }
        mLeftDrawerLayout.setFluidView(mFlowingView);
        mLeftDrawerLayout.setMenuFragment(mMenuFragment);
        //getWindow().setBackgroundDrawableResource(R.color.white);
        FrameLayout contentLayout = (FrameLayout) findViewById(R.id.content_detail);
        View contentView = getLayoutInflater().inflate(getLayoutId(), null);
        contentLayout.addView(contentView);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
    }
    protected void setupRecyclerView(){
        mRecyclerView = (RecyclerView)findViewById(R.id.drawerList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }
    protected void setupToobar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftDrawerLayout.toggle();
            }
        });
        setSupportActionBar(toolbar);

    }
    protected abstract int getLayoutId();
}
