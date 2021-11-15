package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class NaverResponse {
    @SerializedName("link")
    public String link;

    public String getMessage() {
        return link;
    }
    public void setMessage(String message) {
        this.link = message;
    }
}
