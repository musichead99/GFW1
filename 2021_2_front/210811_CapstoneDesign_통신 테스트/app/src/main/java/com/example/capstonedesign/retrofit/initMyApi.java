package com.example.capstonedesign.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface initMyApi {
    // 서버에서 정의된 메소드, 경로 를 확인해야 하는건가?


    //@[통신방식]("[경로]")
    //[통신방식] : GET,POST,PUT,DELETE,HEAD중에서 어떤 작업인지 선택해주면 됨.
    //[]
    @POST("/user/register")
    Call<RegisterResponse> getRegisterResponse(@Body RegisterRequest registerRequest);
}
