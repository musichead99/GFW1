package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class Ranking {
    @SerializedName("user_friend_email")
    public String user_friend_email;

    @SerializedName("name")
    public String name;

    @SerializedName("profilePhoto")
    public String profilePhoto;

    @SerializedName("step_count")
    public int step_count;

    @SerializedName("rank")
    public int rank;

    public Ranking(String user_friend_email, String name, String profilePhoto, int step_count, int rank) {
        this.user_friend_email = user_friend_email;
        this.name = name;
        this.profilePhoto = profilePhoto;
        this.step_count = step_count;
        this.rank = rank;
    }

    public String getUser_friend_email() {
        return this.user_friend_email;
    }
    public String getName() {
        return this.name;
    }
    public String getProfilePhoto() {
        return this.profilePhoto;
    }
    public int getStep_count() {
        return this.step_count;
    }
    public int getRank() {
        return this.rank;
    }
}
