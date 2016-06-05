package com.poc.saveenergy.myapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;
import com.mxn.soul.flowingdrawer_core.MenuFragment;
import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.application.Prefs;
import com.poc.saveenergy.myapplication.application.SaveEnergy;
import com.poc.saveenergy.myapplication.constants.Constants;
import com.squareup.picasso.Picasso;

/**
 * This fragment will show side menu
 */
public class MyMenuFragment extends MenuFragment {

    private ImageView ivMenuUserProfilePhoto;
    private TextView txtUserName;
    private LeftDrawerLayout mLeftDrawerLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void setDrawerLayout(LeftDrawerLayout drawerLayout){
        mLeftDrawerLayout = drawerLayout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container,
                false);
        txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        LinearLayout linShutDown = (LinearLayout) view.findViewById(R.id.linShutDown);
        linShutDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLeftDrawerLayout.closeDrawer();
                Toast.makeText(getActivity(), R.string.systemOff, Toast.LENGTH_LONG).show();
            }
        });
        //ivMenuUserProfilePhoto = (ImageView) view.findViewById(R.id.ivMenuUserProfilePhoto);
        setupHeader();
        return  setupReveal(view) ;
    }

    private void setupHeader() {
        Prefs prefs = SaveEnergy.getInstance().getPrefs();
        if(!TextUtils.isEmpty(prefs.get(Constants.PREF_KEY_USERNAME))){
            //txtUserName.setText(prefs.get(Constants.PREF_KEY_USERNAME));
        }
        //int avatarSize = getResources().getDimensionPixelSize(R.dimen.global_menu_avatar_size);
        /*String profilePhoto = getResources().getString(R.string.user_profile_photo);
        Picasso.with(getActivity())
                .load(profilePhoto)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMenuUserProfilePhoto);*/
    }

    public void onOpenMenu(){

    }
    public void onCloseMenu(){

    }
}
