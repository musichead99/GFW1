package com.example.capstonedesign;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.capstonedesign.home_fragments.MyGoogleFit;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;

public class DateChangeReceiver extends BroadcastReceiver {
    private final static String TAG = "DateChangeReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive invoked");

        int dataType = MyGoogleFit.TYPE_STEP | MyGoogleFit.TYPE_CALORIES | MyGoogleFit.TYPE_DISTANCE | MyGoogleFit.TYPE_MOVE_MIN;

        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_CALORIES_EXPENDED,FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_DISTANCE_DELTA,FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_MOVE_MINUTES,FitnessOptions.ACCESS_READ)
                        .build();
        MyGoogleFit myGoogleFit = MyGoogleFit.getInstance();

        myGoogleFit.setFitnessOptions(fitnessOptions)
                .setAppContext(context.getApplicationContext());

        myGoogleFit.subscription(dataType,context);
    }
}