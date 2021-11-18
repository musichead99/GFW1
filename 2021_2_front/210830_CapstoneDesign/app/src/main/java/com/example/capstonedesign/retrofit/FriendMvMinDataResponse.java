package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class FriendMvMinDataResponse {
    @SerializedName("time_month")
    public float[] move_min;


    public FriendMvMinDataResponse(float[] move_min){
        this.move_min = move_min;
    }

    public float[] getMoveMin(){
        return this.move_min;
    }
    public void setMoveMin(float[] move_min){
        this.move_min = move_min;
    }
}
