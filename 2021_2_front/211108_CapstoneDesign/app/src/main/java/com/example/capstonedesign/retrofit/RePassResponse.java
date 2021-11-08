package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class RePassResponse {
    @SerializedName("message")
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
