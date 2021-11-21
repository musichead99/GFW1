package com.example.capstonedesign.retrofit;

import com.example.capstonedesign.PreferenceManager;
import com.example.capstonedesign.retrofit.FCM.FcmMessageRequest;
import com.example.capstonedesign.retrofit.FCM.FcmMessageResponse;
import com.example.capstonedesign.retrofit.FCM.FcmTokenRequest;
import com.example.capstonedesign.retrofit.FCM.FcmTokenResponse;
import com.example.capstonedesign.retrofit.FriendRequestList.FriendRequestListResponse;
import com.example.capstonedesign.retrofit.FriendRequestList.FriendRequestedListResponse;
import com.example.capstonedesign.retrofit.ProceedFriendRequest.CancelFR_Request;
import com.example.capstonedesign.retrofit.ProceedFriendRequest.CancelFR_Response;
import com.example.capstonedesign.retrofit.ProceedFriendRequest.ProceedFR_Request;
import com.example.capstonedesign.retrofit.ProceedFriendRequest.ProceedFR_Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface initMyApi {

    @POST("/user/register")
    Call<RegisterResponse> getRegisterResponse(@Body RegisterRequest registerRequest);

    @GET("/user/email/{userEmail}")
    Call<ValidateResponse> getValidateResponse(@Path("userEmail") String email);

    @POST("/user/auth")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);

    @PUT("/user/register")
    Call<RePassResponse> getRePassResponse(@Body RePassRequest rePassRequest, @Header("Authorization") String header);

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

    //@GET("Not Determined yet")

    @GET("/service/friendsList")
    Call<FriendListResponse> getFriendListResponse();

    @POST("/service/healthData")
    Call<DDTSResponse> getDDTS(@Body DDTSRequest ddtsRequest );
    // DDTS : Daily Data To Server

    @GET("/service/ranking")
    Call<RankingResponse> getRankingResponse(@Header("Authorization") String header);

    @GET("/service/healthData/step/{friendEmail}")
    Call<FriendStepDataResponse> getFriendStepDataResponse(@Path("friendEmail") String email);

    @GET("/service/healthData/calorie/{friendEmail}")
    Call<FriendCaloriesDataResponse> getFriendCaloriesDataResponse(@Path("friendEmail") String email);

    @GET("/service/healthData/distance/{friendEmail}")
    Call<FriendDistanceDataResponse> getFriendDistanceDataResponse(@Path("friendEmail") String email);

    @GET("/service/healthData/time/{friendEmail}")
    Call<FriendMvMinDataResponse> getFriendMvMinDataResponse(@Path("friendEmail") String email);

    @POST("/user/fcmToken")
    Call<FcmTokenResponse> FcmToken(@Body FcmTokenRequest fcmTokenRequest);

    @POST("/service/friends")
    Call<AddFriendResponse> AddFriend(@Body AddFriendRequest addFriendRequest);

    @POST("/service/notification")
    Call<FcmMessageResponse> FcmMessage(@Body FcmMessageRequest fcmMessageRequest);

    @GET("/service/friendsRequestList") // 사용자가 받은 요청
    Call<FriendRequestedListResponse> FriendRequestedList();

    @GET("/service/friendsMyRequest") // 사용자가 한 요청
    Call<FriendRequestListResponse> FriendRequestList();

    @PUT("/service/friends")
    Call<ProceedFR_Response> ProceedFR_Request(@Body ProceedFR_Request proceedFR_request);

    @DELETE("/service/friendsMyRequest")
    Call<CancelFR_Response> CancelFR_Request(@Body CancelFR_Request cancelFR_request);
}
