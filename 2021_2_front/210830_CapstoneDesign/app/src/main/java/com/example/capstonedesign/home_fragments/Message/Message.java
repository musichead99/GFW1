package com.example.capstonedesign.home_fragments.Message;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("profile_img")
    private String profile_img;

    @SerializedName("message_title")
    private String msg_title;

    @SerializedName("message_content")
    private String msg_content;

    public Message(String profile_img, String msg_title, String msg_content ){
        this.profile_img = profile_img;
        this.msg_title = msg_title;
        this.msg_content = msg_content;
    }
    public String getMsgTitle() {return this.msg_title;}
    public String getMsgContent(){ return this.msg_content;}
    public String getImage(){
        return this.profile_img;
    }
}
