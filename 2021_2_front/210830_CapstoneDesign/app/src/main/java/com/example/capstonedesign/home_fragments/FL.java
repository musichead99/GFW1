package com.example.capstonedesign.home_fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.capstonedesign.R;
import com.example.capstonedesign.imageActivity;
import com.example.capstonedesign.retrofit.ProfileResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FL extends Fragment {
    SharedPreferences sharedPreferences;
    private initMyApi initMyApi;
    ImageView profile_myphoto;
    TextView profile_name, profile_walk;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fl,container,false);

        Button first_tab = rootView.findViewById(R.id.first_tab);
        first_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), imageActivity.class);
                startActivity(intent);
                //imageActivity activity = (imageActivity) getActivity();
                //activity.onFragmentChanged(0);
            }
        });

        profile_myphoto = rootView.findViewById(R.id.profile_myphoto);
        profile_name = rootView.findViewById(R.id.profile_name);
        profile_walk = rootView.findViewById(R.id.profile_walk);
        getProfile();

        return rootView;
    }
    private void getProfile() {
        sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);
        String mytoken = sharedPreferences.getString("token","");

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        Call<ProfileResponse> call = initMyApi.getProfileResponse("Bearer "+mytoken);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if(response.isSuccessful()) {
                    ProfileResponse result = response.body();
                    String status = result.getStatus();
                    String name = result.getName();
                    String step = result.getStep();
                    String profilePhoto = result.getProfilePhoto();

                    profile_name.setText(name);
                    profile_walk.setText(step);
                    Glide.with(getActivity()).load(profilePhoto).into(profile_myphoto);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
            }
        });
    }
}