package com.example.capstonedesign.retrofit;

import com.example.capstonedesign.PreferenceManager;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface initMyApi {

    @POST("/user/register")
    Call<RegisterResponse> getRegisterResponse(@Body RegisterRequest registerRequest);

    @GET("/user/email/{userEmail}")
    Call<ValidateResponse> getValidateResponse(@Path("userEmail") String email);

    @POST("/user/auth")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);

    @POST("/user/password")
    Call<RePassResponse> getRePassResponse(@Body RePassRequest rePassRequest);

    @GET("/service/friendRequestList")
    Call<FriendListResponse> getFriendListResponse();

    @POST("/service/healthData")
    Call<DDTSResponse> getDDTS(@Body DDTSRequest ddtsRequest );
    // DDTS : Daily Data To Server
}
