package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;

/*
{
    "email" : 이메일
    "password" : 비밀번호
    "name" : 이름
}
 */

// Client -> Server로 보낼 데이터의 세팅 및 객체로써 사용시 필요한 함수들도 정의(get/set 함수들).
public class RegisterRequest {

    @SerializedName("email")
    public String inputEmail;

    @SerializedName("password")
    public String inputPw;

    @SerializedName("name")
    public String inputName;

    public String getInputEmail() {
        return inputEmail;
    }

    public String getInputPw() {
        return inputPw;
    }

    public String getInputName(){
        return inputName;
    }

    public void setInputEmail(String inputEmail) {
        this.inputEmail = inputEmail;
    }
    public void setInputPw(String inputPw) {
        this.inputPw = inputPw;
    }
    public void setInputName(String inputName){
        this.inputName = inputName;
    }

    public RegisterRequest(String inputEmail, String inputPw, String inputName) {
        this.inputEmail = inputEmail;
        this.inputPw = inputPw;
        this.inputName = inputName;
    }
}
