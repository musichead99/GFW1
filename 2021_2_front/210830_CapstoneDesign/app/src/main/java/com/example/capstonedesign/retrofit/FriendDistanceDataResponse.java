package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class FriendDistanceDataResponse {
    @SerializedName("distance_month")
    public float[] distance;


    public FriendDistanceDataResponse(float[] distance){
        this.distance = distance;
    }

    public float[] getDistance(){
        return this.distance;
    }
    public void setDistance(float[] distance){
        this.distance = distance;
    }
}
