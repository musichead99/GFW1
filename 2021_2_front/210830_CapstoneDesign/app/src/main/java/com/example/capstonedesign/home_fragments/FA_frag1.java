package com.example.capstonedesign.home_fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.capstonedesign.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class FA_frag1 extends Fragment {
    // LineChart를 가르킬 변수.
    private LineChart lineChart;
    private boolean WEEK = false;
    private boolean MONTH = true;
    private String TAG = "Google Fit Step Count Delta Subscription";

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fa_frag1,container,false);


        Context appContext = rootView.getContext().getApplicationContext();

        /** Google fit 데이터 불러오기. **/
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                        .build();

        AccessGoogleFit(fitnessOptions,appContext,WEEK);

        ToggleButton toggle_btn = rootView.findViewById(R.id.toggleButton);

        ArrayList<Entry> entry_chart = new ArrayList<>();

        lineChart = rootView.findViewById(R.id.lineChart);
        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);

        entry_chart.add(new Entry(0,3));
        entry_chart.add(new Entry(1,1));
        entry_chart.add(new Entry(2,4));
        entry_chart.add(new Entry(3,10));
        entry_chart.add(new Entry(4,6));
        entry_chart.add(new Entry(5,2));


        LineDataSet lineDataSet = new LineDataSet(entry_chart, "나의 정보");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("월");
        labels.add("화");
        labels.add("수");
        labels.add("목");
        labels.add("금");
        labels.add("토");
        labels.add("일");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMaximum(6.0f);

        ValueFormatter valueFormatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int position = (int) Math.round(value);
                return labels.get(position);
            }
        };

        xAxis.setValueFormatter(valueFormatter);

        // LineData가 필요하여 선언.
        LineData chartData = new LineData();
        chartData.addDataSet(lineDataSet);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setData(chartData);
        lineChart.invalidate();

        return rootView;
    }

    public void AccessGoogleFit(FitnessOptions fitnessOptions, Context appContext, boolean typeOfGraph){

        Fitness.getRecordingClient(getActivity(), GoogleSignIn.getLastSignedInAccount(appContext))
                .subscribe(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "TYPE_STEP_COUNT_DELTA | Successfully subscribed!");
                                    readStepData(appContext,fitnessOptions);
                                } else {
                                    Log.d(TAG, "There was a problem subscribing.", task.getException());
                                }
                            }
                        });
    }
}
