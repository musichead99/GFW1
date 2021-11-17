package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class FriendCaloriesDataResponse {
    @SerializedName("carories_month")
    public float[] calories;


    public FriendCaloriesDataResponse(float[] calories){
        this.calories = calories;
    }

    public float[] getCalories(){
        return this.calories;
    }
    public void setCalories(float[] calories){
        this.calories = calories;
    }
}
