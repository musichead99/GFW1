package com.example.capstonedesign.home_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstonedesign.CFriendListActivity;
import com.example.capstonedesign.PreferenceManager;
import com.example.capstonedesign.R;
import com.example.capstonedesign.retrofit.FriendStepDataResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FA_frag1 extends Fragment {

    public static Context appContext;
    public static LineChart lineChart;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fa_frag1,container,false);

        ImageView btn_compareFriend = rootView.findViewById(R.id.btn_compareFriend);
        appContext = rootView.getContext().getApplicationContext();
        Context curContext = rootView.getContext();
        ToggleButton toggle_btn = rootView.findViewById(R.id.fa_frag1_toggleButton);
        boolean tf_period = toggle_btn.isChecked();
        float[] myData = new float[30];

        btn_compareFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CFriendListActivity.class);
                startActivity(intent);
            }
        });


        /** LineChart의 기본 세팅을 해주는 부분 **/
        lineChart = rootView.findViewById(R.id.fa_frag1_lineChart);
        LineChartSetter lineChartSetter = LineChartSetter.newLineChartSetter()
                .setLineChart(lineChart)
                .setPeriod(tf_period)
                .setBasic()
                .setLabel();

        Arrays.fill(myData,(float)0);

        /** Google fit 데이터 불러오기. **/
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                        .build();

        MyGoogleFit myGoogleFit = MyGoogleFit.getInstance()
                .setFitnessOptions(fitnessOptions)
                .setAppContext(appContext);


        int dataType = MyGoogleFit.TYPE_STEP;
        myGoogleFit.subscription(dataType,curContext)
                .getPeriodicData(dataType,curContext,tf_period,myData,lineChart);


        toggle_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lineChart.clear();
                lineChartSetter.setPeriod(isChecked)
                        .setLabel();
                if(isChecked){
                    myGoogleFit.getPeriodicData(dataType,curContext,isChecked,myData,lineChart);
                }else{
                    myGoogleFit.getPeriodicData(dataType,curContext,isChecked,myData,lineChart);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("FA_fragment1","onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FA_frag1","onResume");
        // Step 1. if(friend_email != null)
        String friend_email = PreferenceManager.getString(appContext,"CFriend_email");
        String friend_name = PreferenceManager.getString(appContext,"CFriend_name");

        if(!friend_email.equals("")){
            // 1. retrofit을 사용해 친구의 데이터 받아오기.
            RetrofitClient retrofitClient = RetrofitClient.getNewInstance(appContext);
            initMyApi initMyApi = RetrofitClient.getRetrofitInterface();

            Call<FriendStepDataResponse> call = initMyApi.getFriendStepDataResponse(friend_email);

            call.enqueue(new Callback<FriendStepDataResponse>() {
                @Override
                public void onResponse(Call<FriendStepDataResponse> call, Response<FriendStepDataResponse> response) {
                    FriendStepDataResponse result = response.body();
                    int[] friend_step = result.getStep();

                    if(friend_step == null){
                        Log.d("friend_step","null");
                    }else{
                        for(float a : friend_step){
                            Log.d("friend_step",String.valueOf(a));
                        }
                    }
                    // 2. 친구의 데이터 LineChart에 띄워주기.
                }

                @Override
                public void onFailure(Call<FriendStepDataResponse> call, Throwable t) {

                }
            });
            // 3. 마지막으로 PreferenceManager로 CFriend_email, CFriend_name null로 초기화.
        }else{
        }
    }
}
