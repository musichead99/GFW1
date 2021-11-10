package com.example.capstonedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.capstonedesign.retrofit.ProfileResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class imageActivity extends AppCompatActivity {

    private TextView name0;
    private initMyApi initMyApi;
    SharedPreferences sharedPreferences, sharedPreferences1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        name0=findViewById(R.id.name);
        getName();
    }
    private void getName() {
        sharedPreferences1 = getSharedPreferences("email", MODE_PRIVATE);
        String mytoken = sharedPreferences1.getString("token","");

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        Call<ProfileResponse> call = initMyApi.getProfileResponse("Bearer "+mytoken);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if(response.isSuccessful()) {
                    ProfileResponse result = response.body();
                    String status = result.getStatus();
                    String name = result.getProFile();
                    name0.setText(name);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
            }
        });
    }
}