package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RePassRequest {
    @SerializedName("new_password")
    public String inputPw1;
    @SerializedName("new_password_again")
    public String inputPw2;

    public String getInputPw1() {
        return inputPw1;
    }
    public String getInputPw2() {
        return inputPw2;
    }

    public void setInputPw1(String inputPw) {
        this.inputPw1 = inputPw;
    }
    public void setInputPw2(String inputPw) {
        this.inputPw2 = inputPw;
    }

    public RePassRequest(String inputPw1, String inputPw2) {
        this.inputPw1 = inputPw1;
        this.inputPw2 = inputPw2;
    }
}
