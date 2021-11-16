package com.example.capstonedesign.home_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.capstonedesign.R;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;

import java.util.Arrays;

public class FA_frag2 extends Fragment {

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fa_frag2,container,false);

        Context appContext = rootView.getContext().getApplicationContext();
        Context curContext = rootView.getContext();
        ToggleButton toggle_btn = rootView.findViewById(R.id.fa_frag2_toggleButton);
        boolean tf_period = toggle_btn.isChecked();
        float[] myData = new float[30];
        float[] friendData = new float[30];

        /** LineChart의 기본 세팅을 해주는 부분 **/
        LineChart lineChart = rootView.findViewById(R.id.fa_frag2_lineChart);
        LineChartSetter lineChartSetter = LineChartSetter.newLineChartSetter()
                .setLineChart(lineChart)
                .setPeriod(tf_period)
                .setBasic()
                .setLabel();

        Arrays.fill(myData,(float)0);

        /** Google fit 데이터 불러오기. **/
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_CALORIES_EXPENDED,FitnessOptions.ACCESS_READ)
                        .build();

        MyGoogleFit myGoogleFit = MyGoogleFit.getInstance()
                .setFitnessOptions(fitnessOptions)
                .setAppContext(appContext);


        int dataType = MyGoogleFit.TYPE_CALORIES;
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
}