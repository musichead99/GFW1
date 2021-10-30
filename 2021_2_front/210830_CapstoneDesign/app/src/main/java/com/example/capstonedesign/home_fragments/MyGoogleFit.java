package com.example.capstonedesign.home_fragments;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/** 현 Class에서 가능한 작업 정리.
 *  1. 얻고자 하는 데이터 타입에 따른 client들 등록.
 *  2. Listener의 등록 & 해제.
 *  3. 원하는 데이터의 return 값.
 * **/

public class MyGoogleFit {
    private static MyGoogleFit myGoogleFit = null;
    private static Context appContext = null;
    private static FitnessOptions fitnessOptions = null;

    // TAGs
    private static final String _TAG_ = "MyGoogleFit";

    // DATA_TYPE
    public static final int TYPE_STEP       = 0b00001;
    public static final int TYPE_CALORIES   = 0b00010;
    public static final int TYPE_DISTANCE   = 0b00100;
    public static final int TYPE_MOVE_MIN   = 0b01000;

    // Period
    public static final boolean PERIOD_WEEK = true;
    public static final boolean PERIOD_MONTH = false;

    // Masks
    private static final int[] TYPE_MASK = {
            TYPE_STEP,
            TYPE_CALORIES,
            TYPE_DISTANCE,
            TYPE_MOVE_MIN
    };

    // DataTypes
    private static final DataType[] DATA_TYPE = {
            DataType.TYPE_STEP_COUNT_DELTA,
            DataType.TYPE_CALORIES_EXPENDED,
            DataType.TYPE_DISTANCE_DELTA,
            DataType.TYPE_MOVE_MINUTES
    };

    /** ListnerManager cover area of each index
     * index[0] : TYPE_STEP_COUNT_DELTA     ( format: int    )
     * index[1] : TYPE_CALORIES_EXPENDED    ( format: float  )
     * index[2] : TYPE_DISTANCE_DELTA       ( format: float  )
     * index[3] : TYPE_MOVE_MINUTES         ( format: int    )
     * **/
    private static ArrayList<OnDataPointListener> ListenerManager = new ArrayList<OnDataPointListener>();

    public static MyGoogleFit getInstance(){
        if(myGoogleFit == null){
            myGoogleFit = new MyGoogleFit();
        }
        return myGoogleFit;
    }

    public MyGoogleFit setAppContext(Context appContext){
        MyGoogleFit.appContext = appContext;
        return myGoogleFit;
    }
    public MyGoogleFit setFitnessOptions(FitnessOptions fitnessOptions){
        MyGoogleFit.fitnessOptions = fitnessOptions;
        return myGoogleFit;
    }
    public Context getAppContext(){
        return MyGoogleFit.appContext;
    }
    public FitnessOptions getFitnessOptions(){
        return MyGoogleFit.fitnessOptions;
    }

    /** 아직 빌딩 해야하는 Method.
    public void subPeriodicData(int dataType, Context current_context ,boolean period){
        int actual_period = period ? 7 : 30;
    }
    **/
    public MyGoogleFit subDailyData(int dataType, Context cur_context){
        int length = TYPE_MASK.length;
        for(int i =0 ; i<length ; i++){
            if((dataType & TYPE_MASK[i]) != 0){
                Fitness.getRecordingClient(cur_context, GoogleSignIn.getLastSignedInAccount(appContext))
                        .subscribe(DATA_TYPE[i])
                        .addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(_TAG_, "TYPE_STEP_COUNT_DELTA | Successfully subscribed!");
                                        } else {
                                            Log.d(_TAG_, "There was a problem subscribing.", task.getException());
                                        }
                                    }
                                });
            }
        }
        return myGoogleFit;
    }
    public MyGoogleFit unsubDailyData(int dataType, Context cur_context){
        int length = TYPE_MASK.length;
        for(int i =0 ; i<length ; i++){
            if((dataType & TYPE_MASK[i]) != 0){
                Fitness.getRecordingClient(cur_context, GoogleSignIn.getLastSignedInAccount(appContext))
                        .unsubscribe(DATA_TYPE[i])
                        .addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(_TAG_, "TYPE_STEP_COUNT_DELTA | Successfully subscribed!");
                                        } else {
                                            Log.d(_TAG_, "There was a problem subscribing.", task.getException());
                                        }
                                    }
                                });
            }
        }
        return myGoogleFit;
    }
    public MyGoogleFit updateDailyTotal(Context cur_context,int dataType,float[] result, TextView[] textViews,TextView cur_step, ProgressBar prgBar) {
        int length = TYPE_MASK.length;
        Log.d("DataType",String.valueOf(dataType));
        for(int i=0;i<length;i++){

            if((dataType & TYPE_MASK[i]) != 0){
                int finalI = i;
                Log.d("ActDatas",String.valueOf(finalI));
                Fitness.getHistoryClient(cur_context, GoogleSignIn.getLastSignedInAccount(appContext))
                        .readDailyTotal(DATA_TYPE[finalI])
                        .addOnSuccessListener(
                                new OnSuccessListener<DataSet>() {
                                    @Override
                                    public void onSuccess(DataSet dataSet) {
                                        for (DataPoint dp : dataSet.getDataPoints()) {
                                            for (Field field : dp.getDataType().getFields()) {
                                                Log.d("Stream Name : ", dp.getOriginalDataSource().getStreamName());
                                                if (!"user_input".equals(dp.getOriginalDataSource().getStreamName())) {

                                                    float act_data = (finalI == 0 || finalI == 3) ? (float)dp.getValue(field).asInt() : dp.getValue(field).asFloat();
                                                    result[finalI]= act_data;
                                                    if(finalI != 0){
                                                        Log.d("DailyTotal",String.valueOf(finalI));
                                                        textViews[finalI-1].setText(setTextView(finalI,result));
                                                    }else{
                                                        cur_step.setText(setTextView(finalI,result));
                                                        prgBar.setProgress((int)result[finalI]);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(_TAG_, "There was a problem getting the step count.", e);
                                    }
                                });
            }
        }
        return myGoogleFit;
    }
    public String setTextView(int index,float[] real_data){
        switch(index){
            case 0 :
                return Math.round(real_data[index]) + "걸음";
            case 1 :
                return "소모 칼로리 : " + Math.round(real_data[index]) + " Kcal";
            case 2 :
                return "이동 거리 : " + Math.round(real_data[index]) + " m";
            case 3 :
                return "이동 시간 : " + (int)real_data[index] + " 분";
            default :
                return "ERROR";
        }

    }


    public MyGoogleFit addListener(Context cur_context,int dataType, OnDataPointListener[] listenerManager,float[] real_data, TextView[] textView, int myMenu_length) {
        // Listener should be registered one at a time.
        for(int i=0;i<myMenu_length;i++){
            int mask = 1 <<(i+1);
            int trigger = dataType & mask;
            if(trigger != 0){
                int index = Math.getExponent(trigger);
                int finalI = i;

                OnDataPointListener listener = dataPoint -> {
                    Log.d("hellp","hello");
                    for (Field field : dataPoint.getDataType().getFields()) {
                        Log.d("Listener_others","Working");

                        if (!"user_input".equals(dataPoint.getOriginalDataSource().getStreamName())) {
                            float data = (index != 3) ? dataPoint.getValue(field).asFloat() : (float)dataPoint.getValue(field).asInt();
                            real_data[index] += data;
                        }
                    }
                    textView[finalI].setText(setTextView(index,real_data));
                };

                Fitness.getSensorsClient(cur_context, GoogleSignIn.getAccountForExtension(appContext, fitnessOptions))
                        .add(
                                new SensorRequest.Builder()
                                        .setDataType(DATA_TYPE[index]) // Can't be omitted.
                                        .setSamplingRate(1, TimeUnit.SECONDS)
                                        .build(),
                                listener
                        )
                        .addOnSuccessListener(unused ->
                                Log.i(_TAG_, DATA_TYPE[index].getName() + "Listener registered!"))
                        .addOnFailureListener(task ->
                                Log.e(_TAG_, "Listener not registered.", task.getCause()));

                listenerManager[index] =listener;
            }
        }

        return myGoogleFit;
    }
    public MyGoogleFit addListener(Context cur_context, int dataType, OnDataPointListener[] listenerManager, float[] real_data, TextView cur_step, ProgressBar prgBar) {
        // StepData는 ProgressBar의 갱신이 들어가고, MyMenu 편집에는 존재하지 않기 때문에 Method Overloading을 통해 다른 형태로 만듦.
        int index = Math.getExponent(dataType);

        OnDataPointListener listener = dataPoint -> {
            for (Field field : dataPoint.getDataType().getFields()) {
                Log.d("Listener_Step","Working");
                if (!"user_input".equals(dataPoint.getOriginalDataSource().getStreamName())) {
                    float data = (float)dataPoint.getValue(field).asInt();
                    real_data[index] += data;
                }
            }
            int temp = (int)real_data[index];
            cur_step.setText(String.valueOf(temp) + " 걸음");
            prgBar.setProgress(temp);
        };

        Fitness.getSensorsClient(cur_context, GoogleSignIn.getAccountForExtension(appContext, fitnessOptions))
                .add(
                        new SensorRequest.Builder()
                                .setDataType(DATA_TYPE[index]) // Can't be omitted.
                                .setSamplingRate(1, TimeUnit.SECONDS)
                                .build(),
                        listener
                )
                .addOnSuccessListener(unused ->
                        Log.i(_TAG_, "Step Listener registered!"))
                .addOnFailureListener(task ->
                        Log.e(_TAG_, "Step Listener not registered.", task.getCause()));

        listenerManager[index] =listener;
        return myGoogleFit;
    }
    public MyGoogleFit removeListeners(Context cur_context, OnDataPointListener[] ListenerManager,int index) {

        Fitness.getSensorsClient(cur_context, GoogleSignIn.getAccountForExtension(appContext, fitnessOptions))
                .remove(ListenerManager[index])
                .addOnSuccessListener(unused -> Log.i("ListenerManager", "Listener has been removed!"))
                .addOnFailureListener(e -> Log.i("ListenerManager", "Removing Listener has failed"));
        ListenerManager[index] = null;

        return myGoogleFit;
    }

}
