package com.poc.saveenergy.myapplication.model;

import android.widget.ImageView;

/**
 * Created by Vaib on 24-04-2016.
 */
public class OnlineListModel {

    private String name;
    private boolean isOnline;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
}
