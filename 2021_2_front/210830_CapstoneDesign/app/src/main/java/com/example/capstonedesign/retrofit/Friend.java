package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;


public class Friend {
    @SerializedName("email")
    public String email;

    @SerializedName("name")
    public String name;

    @SerializedName("profilePhoto")
    public String profilePhoto;

    public Friend(String profilePhoto, String email, String name){
        this.profilePhoto = profilePhoto;
        this.email = email;
        this.name = name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getImage(){
        return this.profilePhoto;
    }
    public String getName(){
        return this.name;
    }
}
