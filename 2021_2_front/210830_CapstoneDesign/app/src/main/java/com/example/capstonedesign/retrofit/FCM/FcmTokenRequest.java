package com.example.capstonedesign.retrofit.FCM;

import com.google.gson.annotations.SerializedName;

public class FcmTokenRequest {
    @SerializedName("token")
    public String token;

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public FcmTokenRequest(String token){
        this.token =token;
    }

}
