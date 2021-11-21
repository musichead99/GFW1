package com.example.capstonedesign.retrofit.FriendRequestList;

import com.example.capstonedesign.retrofit.Friend;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendRequestListResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("MyRequestList")
    private List<Friend> myRequestList;

    public FriendRequestListResponse(String status,List<Friend> myRequestList){
        this.status = status;
        this.myRequestList = myRequestList;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setMyRequestList(List<Friend> myRequestList){
        this.myRequestList = myRequestList;
    }
    public List<Friend> getMyRequestList(){
        return this.myRequestList;
    }
}
