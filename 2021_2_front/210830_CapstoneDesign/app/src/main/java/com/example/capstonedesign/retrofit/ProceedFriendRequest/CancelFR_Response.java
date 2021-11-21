package com.example.capstonedesign.retrofit.ProceedFriendRequest;

import com.google.gson.annotations.SerializedName;

public class CancelFR_Response {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    public CancelFR_Response(String status, String message){
        this.status = status;
        this.message = message;
    }
    public CancelFR_Response(String status){
        this.status = status;
        this.message = "No message";
    }
    public String getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
