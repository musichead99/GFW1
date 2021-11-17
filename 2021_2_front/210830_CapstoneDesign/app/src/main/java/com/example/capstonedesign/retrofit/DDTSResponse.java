package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DDTSResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message = "No message";

    public String getMessage(){
        return message;
    }
    public String getStatus() { return status; }
}
