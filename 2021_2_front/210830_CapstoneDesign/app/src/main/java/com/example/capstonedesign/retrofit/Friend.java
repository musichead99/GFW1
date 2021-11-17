package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;


public class Friend {
    @SerializedName("email")
    public String email;

    @SerializedName("name")
    public String name;

    @SerializedName("profile_img")
    public String profile_img;

    public Friend(String profile_img, String email, String name){
        this.profile_img = profile_img;
        this.email = email;
        this.name = name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getImage(){
        return this.profile_img;
    }
    public String getName(){
        return this.name;
    }
}
