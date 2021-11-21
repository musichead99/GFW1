package com.example.capstonedesign.retrofit.FCM;

import com.google.gson.annotations.SerializedName;

public class FcmMessageRequest {

    @SerializedName("receiver")
    private String receiver;

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    public FcmMessageRequest(String receiver,String title,String body){
        this.receiver = receiver;
        this.title = title;
        this.body = body;
    }

    public String getReceiver(){
        return this.receiver;
    }
    public String getTitle(){
        return this.title;
    }
    public String getBody(){
        return this.body;
    }
    public void setReceiver(String receiver){
        this.receiver = receiver;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setBody(String body){
        this.body = body;
    }
}
