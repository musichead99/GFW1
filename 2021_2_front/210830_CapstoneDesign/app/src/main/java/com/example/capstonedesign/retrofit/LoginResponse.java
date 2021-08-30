package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("message")
    public String message;

    @SerializedName("access token")
    public String token;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
