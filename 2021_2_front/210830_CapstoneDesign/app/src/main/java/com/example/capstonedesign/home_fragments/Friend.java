package com.example.capstonedesign.home_fragments;

import android.media.Image;

public class Friend {
    private String email;
    private Image profile_img;
    private String name;

    public Friend(String email,Image profile_img,String name){
        this.email = email;
        this.profile_img = profile_img;
        this.name = name;
    }
    public String getEmail(){
        return this.email;
    }
    public Image getImage(){
        return this.profile_img;
    }
    public String getName(){
        return this.name;
    }
}
