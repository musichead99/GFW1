package com.example.capstonedesign.home_fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.capstonedesign.R;
import com.example.capstonedesign.imageActivity;
import com.example.capstonedesign.retrofit.ProfileResponse;
import com.example.capstonedesign.retrofit.Ranking;
import com.example.capstonedesign.retrofit.RankingResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FL extends Fragment { //친구수 받아오는것 구현 필요
    SharedPreferences sharedPreferences;
    private initMyApi initMyApi;
    ImageView profile_myphoto;
    TextView profile_name, profile_walk;
    ArrayList<Ranking> ranking;
    //LinearLayout tv;
    LinearLayout parent_layout;

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
        parent_layout = rootView.findViewById(R.id.parent_layout);

        //tv = rootView.findViewById(R.id.tv);

        getProfile();
        getRanking();
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
                    int step = result.getStep();
                    String profilePhoto = result.getProfilePhoto();

                    profile_name.setText(name);
                    profile_walk.setText(String.valueOf(step));
                    Glide.with(getActivity()).load(profilePhoto).into(profile_myphoto);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
            }
        });
    }
    private void getRanking() {
        sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);
        String mytoken = sharedPreferences.getString("token","");

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        ranking = new ArrayList<Ranking>();
        Call<RankingResponse> call = initMyApi.getRankingResponse("Bearer "+mytoken);
        call.enqueue(new Callback<RankingResponse>() {
            @Override
            public void onResponse(Call<RankingResponse> call, Response<RankingResponse> response) {
                if(response.isSuccessful()) {
                    RankingResponse result = response.body();
                    String status = result.getStatus();
                    List<Ranking> rank = result.getRanking();
                    for(int i = 0; i<result.getRanking().size(); i++) {
                        //TextView textViewNew = new TextView(getContext());
                        //textViewNew.setText(result.getRanking().get(i).getName());
                        //tv.addView(textViewNew);
                        LinearLayout layout = new LinearLayout(getContext());
                        ImageView profile_phogo_align = new ImageView(getContext());
                        Glide.with(getActivity()).load(result.getRanking().get(i).getProfilePhoto()).into(profile_phogo_align);
                        ViewGroup.LayoutParams profile_photo_params = profile_phogo_align.getLayoutParams();
                        profile_photo_params.width=60;
                        profile_photo_params.height=60;
                        layout.addView(profile_phogo_align);
                        parent_layout.addView(layout);
                    }
                }
            }

            @Override
            public void onFailure(Call<RankingResponse> call, Throwable t) {

            }
        });
    }
    private void createTextView() {
        TextView textViewNew = new TextView(getContext());
        textViewNew.setText("");
        textViewNew.setTextSize(18);
        textViewNew.setId(0);
        //LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //textViewNew.setLayoutParams(param);
        //tv.addView(textViewNew);
    }
}