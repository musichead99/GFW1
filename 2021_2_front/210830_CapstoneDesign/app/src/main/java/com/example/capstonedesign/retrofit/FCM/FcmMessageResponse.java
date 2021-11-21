package com.example.capstonedesign.retrofit.FCM;

import com.google.gson.annotations.SerializedName;

public class FcmMessageResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message = "No message";

    public FcmMessageResponse(String status,String message){
        this.status = status;
        this.message = message;
    }

    public void setStatus(String status){
        this.status = status;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getStatus(){
        return this.status;
    }
    public String getMessage(){
        return this.message;
    }
}
