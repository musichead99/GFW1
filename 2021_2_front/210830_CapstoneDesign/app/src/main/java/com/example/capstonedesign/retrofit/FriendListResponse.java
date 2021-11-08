package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FriendListResponse {
    @SerializedName("friendList")
    private ArrayList<Friend> friendList;

    public FriendListResponse(ArrayList<Friend> friendList){
        this.friendList = friendList;
    }

    public ArrayList<Friend> getFriendList(){
        return friendList;
    }
    public void setFriendList(ArrayList<Friend> newList){
        friendList = newList;
    }
}
