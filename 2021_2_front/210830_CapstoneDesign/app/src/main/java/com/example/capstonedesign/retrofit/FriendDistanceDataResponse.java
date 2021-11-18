package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class FriendDistanceDataResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message = "No message";

    @SerializedName("distance_month")
    public float[] distance;


    public FriendDistanceDataResponse(float[] distance){
        this.distance = distance;
    }

    public float[] getDistance(){
        return this.distance;
    }
    public String getStatus(){return this.status;}
    public String getMessage(){return this.message;}
    public void setStatus(String status){this.status = status;}
    public void setMessage(String message){this.message = message;}
    public void setDistance(float[] distance){
        this.distance = distance;
    }
}
