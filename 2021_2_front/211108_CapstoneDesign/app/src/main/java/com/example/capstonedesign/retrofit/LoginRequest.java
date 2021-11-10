package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    public String inputEmail;

    @SerializedName("password")
    public String inputPw;

    public String getInputEmail() {
        return inputEmail;
    }

    public String getInputPw() {
        return inputPw;
    }

    public void setInputEmail(String inputEmail) {
        this.inputEmail = inputEmail;
    }
    public void setInputPw(String inputPw) {
        this.inputPw = inputPw;
    }

    public LoginRequest(String inputEmail, String inputPw) {
        this.inputEmail = inputEmail;
        this.inputPw = inputPw;
    }
}
