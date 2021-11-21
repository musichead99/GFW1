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

import androidx.fragment.app.Fragment;

import com.example.capstonedesign.CFriendListActivity;
import com.example.capstonedesign.PreferenceManager;
import com.example.capstonedesign.R;
import com.example.capstonedesign.retrofit.FriendMvMinDataResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FA_frag4 extends Fragment {

    public static Context appContext;
    public static Context curContext;
    public static LineChart lineChart;
    public static MyGoogleFit myGoogleFit;
    public float[] friend_movemin;
    public float[] myData;
    public int dataType = MyGoogleFit.TYPE_MOVE_MIN;

    private LineChartSetter lineChartSetter;
    private String friend_email;
    private String friend_name;
    private boolean compare = false;
    private ToggleButton toggle_btn;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fa_frag4,container,false);

        ImageView btn_compareFriend = rootView.findViewById(R.id.btn_compareFriend);
        appContext = rootView.getContext().getApplicationContext();
        curContext = rootView.getContext();
        toggle_btn = rootView.findViewById(R.id.fa_frag4_toggleButton);
        boolean tf_period = toggle_btn.isChecked();
        myData = new float[30];

        btn_compareFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CFriendListActivity.class);
                startActivity(intent);
            }
        });

        /** LineChart의 기본 세팅을 해주는 부분 **/
        lineChart = rootView.findViewById(R.id.fa_frag4_lineChart);
        lineChartSetter = LineChartSetter.newLineChartSetter()
                .setLineChart(lineChart)
                .setPeriod(tf_period)
                .setBasic()
                .setLabel();

        Arrays.fill(myData,(float)0);

        /** Google fit 데이터 불러오기. **/
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_MOVE_MINUTES,FitnessOptions.ACCESS_READ)
                        .build();

        myGoogleFit = MyGoogleFit.getInstance()
                .setFitnessOptions(fitnessOptions)
                .setAppContext(appContext);


        myGoogleFit.subscription(dataType,curContext)
                .getPeriodicData(dataType,curContext,tf_period,myData,lineChart);

        toggle_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lineChart.clear();
                lineChartSetter.setPeriod(isChecked)
                        .setLabel();

                if(!compare) myGoogleFit.getPeriodicData(dataType,curContext,isChecked,myData,lineChart);
                else myGoogleFit.getPeriodicData(dataType,curContext,isChecked,myData,friend_movemin,lineChart);
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

        friend_email = PreferenceManager.getString(appContext,"CFriend_email");
        friend_name = PreferenceManager.getString(appContext,"CFriend_name");

        Log.d("friend_email",friend_email);
        Log.d("friend_name",friend_name);

        if(friend_name.equals("선택안함")){
            Log.d("선택안함","in");
            compare = false;
            boolean isChecked = toggle_btn.isChecked();

            lineChart.clear();
            lineChartSetter.setPeriod(isChecked)
                    .setLabel();

            myGoogleFit.getPeriodicData(dataType,curContext,isChecked,myData,lineChart);

        }else if(!friend_email.equals("")){
            compare = true;

            RetrofitClient retrofitClient = RetrofitClient.getNewInstance(appContext);
            initMyApi initMyApi = RetrofitClient.getNewRetrofitInterface();

            Call<FriendMvMinDataResponse> call = initMyApi.getFriendMvMinDataResponse(friend_email);

            call.enqueue(new Callback<FriendMvMinDataResponse>() {
                @Override
                public void onResponse(Call<FriendMvMinDataResponse> call, Response<FriendMvMinDataResponse> response) {
                    FriendMvMinDataResponse result = response.body();
                    friend_movemin = result.getMoveMin();
                    boolean isChecked = toggle_btn.isChecked();

                    if(friend_movemin == null){
                        Log.d("friend_movemin","null");
                    }else{
                        lineChart.clear();
                        lineChartSetter.setPeriod(isChecked)
                                .setLabel();
                        if(!compare) myGoogleFit.getPeriodicData(dataType,curContext,isChecked,myData,lineChart);
                        else myGoogleFit.getPeriodicData(dataType,curContext,isChecked,myData,friend_movemin,lineChart);
                    }
                }
                @Override
                public void onFailure(Call<FriendMvMinDataResponse> call, Throwable t) {

                }
            });
        }
    }
}