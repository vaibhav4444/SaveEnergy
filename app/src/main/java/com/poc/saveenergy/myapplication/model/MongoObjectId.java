package com.poc.saveenergy.myapplication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaib on 19-06-2016.
 */
public class MongoObjectId {
    @SerializedName("$oid")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
