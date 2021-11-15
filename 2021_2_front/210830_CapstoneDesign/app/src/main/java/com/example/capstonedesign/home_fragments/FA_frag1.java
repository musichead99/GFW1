package com.example.capstonedesign.home_fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.capstonedesign.R;
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

public class FA_frag1 extends Fragment {

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fa_frag1,container,false);

        String friend_email = null;
        String friend_name = null;
        Bundle bundle = getArguments();
        if(bundle != null){
            // 여기서 친구의 데이터를 가져오고.
            friend_email = bundle.getString("CompareFriend");
            friend_name = bundle.getString("FriendName");
        }

        Context appContext = rootView.getContext().getApplicationContext();
        Context curContext = rootView.getContext();
        ToggleButton toggle_btn = rootView.findViewById(R.id.fa_frag1_toggleButton);
        boolean tf_period = toggle_btn.isChecked();
        float[] result = new float[30];


        /** LineChart의 기본 세팅을 해주는 부분 **/
        LineChart lineChart = rootView.findViewById(R.id.fa_frag1_lineChart);
        LineChartSetter lineChartSetter = LineChartSetter.newLineChartSetter()
                .setLineChart(lineChart)
                .setPeriod(tf_period)
                .setBasic()
                .setLabel();

        Arrays.fill(result,(float)0);

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
                .getPeriodicData(dataType,curContext,tf_period,result,lineChart);
        // if(친구 비교 자료 필요) 여기서 친구의 그래프 그려주면 될듯. tf_period : True 30일, False 7일
        // myGoogleFit.setLineChart(float[] result, LineChart lineChart,boolean tf_period,String name);

        toggle_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lineChart.clear();
                if(isChecked){
                    myGoogleFit.getPeriodicData(dataType,curContext,isChecked,result,lineChart);
                }else{
                    myGoogleFit.getPeriodicData(dataType,curContext,isChecked,result,lineChart);
                }
            }
        });
        return rootView;
    }
}
