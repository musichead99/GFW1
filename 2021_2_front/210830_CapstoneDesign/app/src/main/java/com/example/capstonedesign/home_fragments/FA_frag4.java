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

public class FA_frag4 extends Fragment {

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fa_frag4,container,false);

        Context appContext = rootView.getContext().getApplicationContext();
        Context curContext = rootView.getContext();
        ToggleButton toggle_btn = rootView.findViewById(R.id.fa_frag4_toggleButton);
        boolean tf_period = toggle_btn.isChecked();
        float[] result = new float[30];
        LineChart lineChart = rootView.findViewById(R.id.fa_frag4_lineChart);

        Arrays.fill(result,(float)0);

        /** Google fit 데이터 불러오기. **/
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_MOVE_MINUTES,FitnessOptions.ACCESS_READ)
                        .build();

        MyGoogleFit myGoogleFit = MyGoogleFit.getInstance()
                .setFitnessOptions(fitnessOptions)
                .setAppContext(appContext);


        int dataType = MyGoogleFit.TYPE_MOVE_MIN;
        myGoogleFit.subscription(dataType,curContext)
                .getPeriodicData(dataType,curContext,tf_period,result,lineChart);

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