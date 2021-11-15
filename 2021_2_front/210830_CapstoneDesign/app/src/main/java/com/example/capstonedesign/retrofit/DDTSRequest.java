package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DDTSRequest {
    // Step, Calories, Move Distance, Move Minutes
    @SerializedName("stepCount")
    public float stepCount;

    @SerializedName("calories")
    public float calories;

    @SerializedName("distance")
    public float distance;

    @SerializedName("time")
    public float time;

    public DDTSRequest(float stepCount,float calories,float distance,float time){
        this.stepCount = stepCount;
        this.calories = calories;
        this.distance = distance;
        this.time = time;
    }
    public float[] getDDTS(){
        float[] result = new float[]{
                stepCount,
                calories,
                distance,
                time
        };
        return result;
    }
    public void setStepCount(float stepCount){
        this.stepCount = stepCount;
    }
    public void setCalories(float calories){
        this.calories = calories;
    }
    public void setDistance(float distance){
        this.distance = distance;
    }
    public void setTime(float time){
        this.time = time;
    }
}
