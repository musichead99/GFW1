package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class FriendStepDataResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message = "No message";

    @SerializedName("step_count_month")
    public float[] step;

    public FriendStepDataResponse(float[] step){
        this.step = step;
    }

    public float[] getStep(){
        return this.step;
    }
    public String getStatus(){return this.status;}
    public String getMessage(){return this.message;}
    public void setStatus(String status){this.status = status;}
    public void setMessage(String message){this.message = message;}
    public void setStep(float[] step){
        this.step = step;
    }
}
