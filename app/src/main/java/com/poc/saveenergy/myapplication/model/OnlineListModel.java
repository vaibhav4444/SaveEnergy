package com.poc.saveenergy.myapplication.model;

import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaib on 24-04-2016.
 */
public class OnlineListModel {
    @SerializedName("_id")
    private MongoObjectId mongoObjectId;

    public MongoObjectId getMongoObjectId() {
        return mongoObjectId;
    }

    public void setMongoObjectId(MongoObjectId mongoObjectId) {
        this.mongoObjectId = mongoObjectId;
    }

    @SerializedName("user_name")
    private String name;
    @SerializedName("is_online")
    private boolean isOnline;

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    private int imageResource = 0;
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
