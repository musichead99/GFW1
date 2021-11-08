package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

public class ValidateResponse {
    //서버로부터 받을 데이터들을 정의
    @SerializedName("status")
    public String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
