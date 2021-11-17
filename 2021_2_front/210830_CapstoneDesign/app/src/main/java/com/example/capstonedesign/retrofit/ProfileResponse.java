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

    @SerializedName("dateOfBirth")
    public String dateOfBirth;
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public String setDateOfBrith() {
        return dateOfBirth;
    }

    @SerializedName("abode")
    public String abode;
    public String getAbode() {
        return abode;
    }
    public String setAbode() {
        return abode;
    }

    @SerializedName("profilePhoto")
    public String profilePhoto;
    public String getProfilePhoto() {
        return profilePhoto;
    }
    public String setProfilePhoto() {
        return profilePhoto;
    }

    @SerializedName("step_count")
    public int step_count;
    public int getStep() {
        return step_count;
    }
    public int setStep() {
        return step_count;
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
    public String getName() {
        return profile.getName();
    }
    public String getDateOfBirth() {
        return profile.getDateOfBirth();
    }
    public String getAbode() {
        return profile.getAbode();
    }
    public String getProfilePhoto() {
        return profile.getProfilePhoto();
    }
    public int getStep() {
        return profile.getStep();
    }
}