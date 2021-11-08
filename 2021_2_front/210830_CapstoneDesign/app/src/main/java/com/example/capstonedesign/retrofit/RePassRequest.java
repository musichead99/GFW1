package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RePassRequest {
    @SerializedName("password")
    public String inputPw;

    public String getInputPw() {
        return inputPw;
    }

    public void setInputPw(String inputPw) {
        this.inputPw = inputPw;
    }

    public RePassRequest(String inputPw) {
        this.inputPw = inputPw;
    }
}
