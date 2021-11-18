package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class FriendMvMinDataResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message = "No message";

    @SerializedName("time_month")
    public float[] move_min;


    public FriendMvMinDataResponse(float[] move_min){
        this.move_min = move_min;
    }

    public float[] getMoveMin(){
        return this.move_min;
    }
    public String getStatus(){return this.status;}
    public String getMessage(){return this.message;}
    public void setStatus(String status){this.status = status;}
    public void setMessage(String message){this.message = message;}
    public void setMoveMin(float[] move_min){
        this.move_min = move_min;
    }
}
