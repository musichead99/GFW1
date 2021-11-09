package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Profile {
    @SerializedName("name")
    public String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
public class ProfileResponse {
    //서버로부터 받을 데이터들을 정의
    @SerializedName("status")
    public String status;
    @SerializedName("profile")
    @Expose
    public Profile profile;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getProFile() {
        return profile.getName();
    }
}