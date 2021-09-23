package com.example.capstonedesign.home_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.capstonedesign.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class FA_frag1 extends Fragment {
    // LineChart를 가르킬 변수.
    private LineChart lineChart;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fa_frag1,container,false);

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
}
