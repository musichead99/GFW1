package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class FriendStepDataResponse {
    @SerializedName("step_count_month")
    public int[] step;

    public FriendStepDataResponse(int[] step){
        this.step = step;
    }

    public int[] getStep(){
        return this.step;
    }
    public void setStep(int[] step){
        this.step = step;
    }
}
