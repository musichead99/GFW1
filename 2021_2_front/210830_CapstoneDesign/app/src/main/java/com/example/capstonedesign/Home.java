package com.example.capstonedesign;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.capstonedesign.home_fragments.FA;
import com.example.capstonedesign.home_fragments.FC;
import com.example.capstonedesign.home_fragments.FL;
import com.example.capstonedesign.home_fragments.FS;
import com.example.capstonedesign.retrofit.FCM.FcmTokenRequest;
import com.example.capstonedesign.retrofit.FCM.FcmTokenResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {
    FA fa;
    FC fc;
    FL fl;
    FS fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        FcmTokenHandle();

        PreferenceManager.setString(getApplicationContext(),"CFriend_email",null);
        PreferenceManager.setString(getApplicationContext(),"CFriend_name",null);

        fa = new FA();
        fc = new FC();
        fl = new FL();
        fs = new FS();


        // Home 입장시 첫 화면 배치
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fs).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_menu);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.first_bottm_tab: //상태
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fs).commit();
                        return true;
                    case R.id.second_bottm_tab: //분석
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fa).commit();
                        return true;
                    case R.id.third_bottm_tab: //프로필
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fl).commit();
                        return true;
                    case R.id.fourth_bottm_tab: //설정
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fc).commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void FcmTokenHandle() {

        RetrofitClient retrofitClient = RetrofitClient.getNewInstance(getApplicationContext());
        initMyApi initMyApi = RetrofitClient.getNewRetrofitInterface();

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {
                Log.d("MyFCM", "Success : " + token);


                FcmTokenRequest fcmTokenRequest = new FcmTokenRequest(token); // token이 들어가야함.

                initMyApi.FcmToken(fcmTokenRequest).enqueue(new Callback<FcmTokenResponse>() {
                    @Override
                    public void onResponse(Call<FcmTokenResponse> call, Response<FcmTokenResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            FcmTokenResponse result = response.body();
                            String status = result.getStatus();
                            String message = result.getMessage();

                            Log.d("FCM-Server", "Status : " + status +", Message : "+message);
                        } else Log.d("FCM-Server", "Something's wrong 1");
                    }

                    @Override
                    public void onFailure(Call<FcmTokenResponse> call, Throwable t) {
                        Log.d("FCM-Server", "Something's wrong 2");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d("MyFCM", "Something's wrong");
            }
        });
    }

    public void goSetting(View view) {
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);

        finish();
    }
}