package com.example.capstonedesign.retrofit.ProceedFriendRequest;

import com.google.gson.annotations.SerializedName;

public class ProceedFR_Response {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    public ProceedFR_Response(String status,String message){
        this.status = status;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
