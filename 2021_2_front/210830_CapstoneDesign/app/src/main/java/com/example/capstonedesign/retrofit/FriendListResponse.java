package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendListResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message = "No message";

    @SerializedName("FriendsList")
    public List<Friend> friend;

    public FriendListResponse(List<Friend> friend){
        this.friend = friend;
    }

    public List<Friend> getFriendList(){
        return friend;
    }
    public void setFriendList(List<Friend> newList){
        friend = newList;
    }

    public String getStatus(){
        return this.status;
    }
    public String getMessage(){
        return this.message;
    }
}
