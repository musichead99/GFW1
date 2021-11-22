package com.example.capstonedesign.retrofit.FriendRequestList;

import com.example.capstonedesign.retrofit.Friend;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendRequestedListResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("FriendsRequestList")
    private List<Friend> friendsRequestedList;

    public FriendRequestedListResponse(String status, List<Friend> friendsRequestedList){
        this.status = status;
        this.friendsRequestedList = friendsRequestedList;
    }

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setFriendsRequestList(List<Friend> friendsRequestedList){
        this.friendsRequestedList = friendsRequestedList;
    }
    public List<Friend> getFriendsRequestedList(){
        return this.friendsRequestedList;
    }

}
