package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendListResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message = "No message";

    @SerializedName("FriendsList")
    private List<Friend> friend;

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
        return status;
    }
    public String getMessage(){
        return message;
    }
}
