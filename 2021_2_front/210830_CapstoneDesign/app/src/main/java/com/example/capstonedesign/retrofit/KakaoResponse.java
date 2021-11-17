package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class KakaoResponse {
    @SerializedName("link")
    public String link;

    @SerializedName("access token")
    public String token;

    public String getMessage() {
        return link;
    }
    public void setMessage(String message) {
        this.link = message;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
