package com.example.capstonedesign.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface initMyApi {
    // 서버에서 정의된 메소드, 경로 를 확인해야 하는건가?

    //@[통신방식]("[경로]")
    //[통신방식] : GET,POST,PUT,DELETE,HEAD중에서 어떤 작업인지 선택해주면 됨.
    //[]
    @POST("/user/register")
    Call<RegisterResponse> getRegisterResponse(@Body RegisterRequest registerRequest);

    @GET("/user/email/{userEmail}")
    Call<ValidateResponse> getValidateResponse(@Path("userEmail") String email);

    @POST("/user/auth")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);

    @POST("/user/password")
    Call<RePassResponse> getRePassResponse(@Body RePassRequest rePassRequest);

    @GET("/service/profile")
    Call<ProfileResponse> getProfileResponse(@Header("Authorization") String header);

    @PUT("/service/profile")
    Call<UpdateProfileResponse> getUpdateProfileResponse(@Body UpdateProfileRequest updateProfileRequest, @Header("Authorization") String header);

    @DELETE("/user/auth")
    Call<LogoutResponse> getLogoutResponse(@Header("Authorization") String header);

    @DELETE("/user/register")
    Call<DeleteResponse> getDeleteResponse(@Header("Authorization") String header);

    @GET("/user/kakao/")
    Call<KakaoResponse> getKakaoResponse();

    @GET("/user/Naver/")
    Call<NaverResponse> getNaverResponse();

    @GET("Not Determined yet")
    Call<FriendListResponse> getFriendListResponse();

    @POST("Not Determined yet")
    Call<DDTSResponse> getDDTS(@Body DDTSRequest ddtsRequest );
    // DDTS : Daily Data To Server
}
