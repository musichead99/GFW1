package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DDTSResponse {
    @SerializedName("message")
    private String message;

    public String getMessage(){
        return message;
    }
}
