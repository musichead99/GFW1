package com.example.capstonedesign.retrofit.FCM;

import com.google.gson.annotations.SerializedName;

public class FcmTokenResponse {
    @SerializedName("status")
    private String status = "No Answer";
    @SerializedName("message")
    private String message = "No Message";

    public FcmTokenResponse(String status,String message){
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public String getStatus(){
        return status;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public void setStatus(String status){
        this.status = status;
    }
}
