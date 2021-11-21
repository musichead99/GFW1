package com.example.capstonedesign.retrofit.ProceedFriendRequest;

import com.google.gson.annotations.SerializedName;

public class CancelFR_Request {
    @SerializedName("friendEmail")
    private String friendEmail;

    public CancelFR_Request(String friendEmail){
        this.friendEmail = friendEmail;
    }
    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }
    public String getFriendEmail() {
        return friendEmail;
    }
}
