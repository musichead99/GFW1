package com.example.capstonedesign.home_fragments;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class LineChartSetter {
    private LineChartSetter lineChartSetter = null;
    private int period; // True : 30, False : 7
    private LineChart lineChart;

    public static LineChartSetter newLineChartSetter(){
        return new LineChartSetter();
    }
    public LineChartSetter setLineChart(LineChart lineChart){
        this.lineChart = lineChart;
        return lineChartSetter;
    }
    public LineChartSetter setPeriod(boolean bool_period){
        this.period = bool_period ? 30 : 7;;
        return lineChartSetter;
    }

    public LineChartSetter setBasic(){
        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);

        YAxis yAxis_left = lineChart.getAxisLeft();
        YAxis yAxis_right = lineChart.getAxisRight();
        yAxis_left.setAxisMinimum(0.0f);
        yAxis_right.setAxisMinimum(0.0f);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart.animateXY(3000,3000);

        return lineChartSetter;
    }
    public LineChartSetter setLabel(){

        ArrayList<String> labels = new ArrayList<String>();

        for(int i = 0;i<period;i++){
            labels.add(period-i + "일전");
        }

        // Setting ValueFormatter
        XAxis xAxis = lineChart.getXAxis();

        ValueFormatter valueFormatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int position = (int) Math.round(value);
                return labels.get(position);
            }
        };
        xAxis.setValueFormatter(valueFormatter);

        return lineChartSetter;
    }
}
