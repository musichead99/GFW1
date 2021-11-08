package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DDTSRequest {
    // Step, Calories, Move Distance, Move Minutes
    @SerializedName("DailyData")
    private float[] dailyData = new float[4];

    public DDTSRequest(float[] dailyData){
        System.arraycopy(dailyData,0,this.dailyData,0,dailyData.length);
    }

    public float[] getDDTS(){
        float[] result = new float[4];
        System.arraycopy(dailyData,0,result,0,result.length);
        return result;
    }
    public void setDDTS(float[] newdailyData){
        System.arraycopy(newdailyData,0,this.dailyData,0,newdailyData.length);
    }
}
