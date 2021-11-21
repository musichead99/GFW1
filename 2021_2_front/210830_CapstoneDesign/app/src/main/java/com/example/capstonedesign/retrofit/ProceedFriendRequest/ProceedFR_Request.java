package com.example.capstonedesign.retrofit.ProceedFriendRequest;

import com.google.gson.annotations.SerializedName;

public class ProceedFR_Request {
    @SerializedName("friendEmail")
    private String friendEmail;

    @SerializedName("accept")
    private String accept;

    public ProceedFR_Request(String friendEmail,String accept){
        this.friendEmail = friendEmail;
        this.accept = accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }
    public void setFriendEmail(String friendEmail){
        this.friendEmail = friendEmail;
    }
    public String getAccept(){
        return this.accept;
    }
    public String getFriendEmail(){
        return this.friendEmail;
    }
}
