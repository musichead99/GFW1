package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class AddFriendResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message = "No message";

    public AddFriendResponse(String status,String message){
        this.status = status;
        this.message = message;
    }

    public void setStatus(String status){
        this.status = status;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getStatus(){
        return this.status;
    }
    public String getMessage(){
        return this.message;
    }
}
