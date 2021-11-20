package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class FriendCaloriesDataResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message = "No message";

    @SerializedName("carories_month")
    public float[] calories;


    public FriendCaloriesDataResponse(float[] calories){
        this.calories = calories;
    }

    public float[] getCalories(){
        return this.calories;
    }
    public String getStatus(){return this.status;}
    public String getMessage(){return this.message;}
    public void setStatus(String status){this.status = status;}
    public void setMessage(String message){this.message = message;}
    public void setCalories(float[] calories){
        this.calories = calories;
    }
}
