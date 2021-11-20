package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class AddFriendRequest {
    @SerializedName("friendEmail")
    private String friendEmail;

    public AddFriendRequest(String friendEmail){
        this.friendEmail = friendEmail;
    }
    public String getFriendEmail(){
        return this.friendEmail;
    }
    public void setFriendEmail(String friendEmail){
        this.friendEmail = friendEmail;
    }
}
