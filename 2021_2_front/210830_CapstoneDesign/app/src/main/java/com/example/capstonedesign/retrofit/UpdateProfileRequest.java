package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {
    @SerializedName("name")
    public String inputName;
    @SerializedName("dateOfBirth")
    public String inputDateOfBirth;
    @SerializedName("abode")
    public String inputAbode;
    //@SerializedName("profilePhoto")
    //public String inputProfilePhoto;

    public String getInputName() {
        return inputName;
    }
    public String getInputDateOfBirth() {
        return inputDateOfBirth;
    }
    public String getInputAbode() {
        return inputAbode;
    }
    /*public String getInputProfilePhoto() {
        return inputProfilePhoto;
    }*/

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }
    public void setInputDateOfBirth(String inputDateOfBirth) {
        this.inputDateOfBirth = inputDateOfBirth;
    }
    public void setInputAbode(String inputAbode) {
        this.inputAbode = inputAbode;
    }
    /*public void setInputProfilePhoto(String inputProfilePhoto) {
        this.inputProfilePhoto = inputProfilePhoto;
    }*/

    public UpdateProfileRequest(String inputName, String inputDateOfBirth, String inputAbode) {
        this.inputName = inputName;
        this.inputDateOfBirth = inputDateOfBirth;
        this.inputAbode = inputAbode;
        //this.inputProfilePhoto = inputProfilePhoto;
    }
}
