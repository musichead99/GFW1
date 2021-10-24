package com.example.capstonedesign.home_fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.example.capstonedesign.Home;
import com.example.capstonedesign.Login;
import com.example.capstonedesign.MainActivity;
import com.example.capstonedesign.PreferenceManager;
import com.example.capstonedesign.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.SensorsClient;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FS extends Fragment {
    FL fl;
    Context mContext;
    private static String name_btn_goal = "btn_goal";
    TextView text_goal;
    TextView current_step;

    /** Dialog에서 String 형태로 인자 전달을 위해 choice는 String으로 하고 int 값은 따로 저장. **/
    final String[] goal_choice = {"1000 보","5000 보","10000 보"};
    final int[] goal_choice_int = {1000,5000,10000};
    final String pref_goal = "goal";
    final String pref_goalIndex = "goalIndex";
    int goalStep;

    /** ProgressBar **/
    ProgressBar progressBar;

    /** My메뉴 **/
    String myMenuName = "Mymenu";
    String[] choiceArray = new String[] {"소모 칼로리","거리","평균 속도"};
    boolean[] checkArray; // MyMenu에서 볼 정보들에 대한 정보 저장.
    int[] textViewIds = new int[]{ViewCompat.generateViewId(),ViewCompat.generateViewId(),ViewCompat.generateViewId(),ViewCompat.generateViewId()};

    /** Google Fit **/
    public static final String TAG = "Google Fit";
    public static final String OAUTH_TAG = "REQUEST_OAUTH_REQUEST";
    public static final int CAL_LISTENER = 0;
    public static final int DIS_LISTENER = 1;
    public static final int SPD_LISTENER = 2;
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    private TextView step = null;
    private TextView calories = null;
    private TextView distance = null;
    private TextView speed = null;
    public int userInputSteps = 0;
    public float userInputCalories = 0;
    public float userInputDistance = 0;
    public float userInputSpeed = 0;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fs,container,false);
        mContext = getContext();

        /** SensorClient에 등록된 Listener를 제거해주기 위함.
         * index [0] : Calories Listener
         * index [1] : Distance Listener
         * index [2] : Speed Listener
         * **/
        OnDataPointListener[] ListenerManager = new OnDataPointListener[3];

        // App의 Context를 저장.
        Context appContext = getActivity().getApplicationContext();

        // fitnessOptions Config
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_CALORIES_EXPENDED,FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_DISTANCE_DELTA,FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_SPEED,FitnessOptions.ACCESS_READ)
                        .build();

        // 처음에 checkArray의 정보를 불러와서 저장.
        checkArray = PreferenceManager.getBooleanArray(mContext,myMenuName,choiceArray.length);

        // MyMenu Container view 할당.
        LinearLayout textViewContainer = rootView.findViewById(R.id.fs_lin_scroll);

        // google fit의 정보를 View에 갱신함.
        UpdateMyMenu(appContext,fitnessOptions,textViewContainer,ListenerManager);

        fl = new FL();

        /** 목표 걸음수를 보여주는 텍스트 **/
        text_goal = rootView.findViewById(R.id.text_goal);
        goalStep = PreferenceManager.getInt(mContext,pref_goal);
        if( goalStep != -1){
            // 목표 걸음수를 처음 사용하는 사용자가 아님.
            text_goal.setText(String.valueOf(goalStep));
        }else{
            // 목표 걸음수를 처음 사용하는 사용자.
            goalStep = goal_choice_int[0];
            text_goal.setText(String.valueOf(goalStep));
            PreferenceManager.setInt(mContext,pref_goal,goalStep);
            PreferenceManager.setInt(mContext,pref_goalIndex,0);
        }
        /** 현재 걸음수 텍스트 **/
        current_step = rootView.findViewById(R.id.current_step);

        /** 목표 걸음수 ProgressBar 초기 세팅 **/
        progressBar = rootView.findViewById(R.id.progressbar_goal);
        // 최대값 설정.
        progressBar.setMax(goalStep);

        // progressBar.setProgress(여기에 현재 걸음수를 넣어줘야함.);

        /** 목표 설정 버튼 **/
        Button btn_goal = rootView.findViewById(R.id.btn_goal);
        btn_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** 목표 설정을 위한 다이얼 로그 창 표시 **/
                showGoalSettingDialog();
            }
        });

        
        /** 산책 시작 버튼 **/
        Button btn_workout = rootView.findViewById(R.id.btn_workout);
        btn_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** 산책버튼 누를 시 위치탭의 산책 시작으로 넘어감. **/
                ((Home)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fl).commit();
            }
        });

        /** My 메뉴 편집 더보기 메뉴 설정. **/
        ImageButton see_more_btn = (ImageButton) rootView.findViewById(R.id.see_more_button);
        see_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);

                getActivity().getMenuInflater().inflate(R.menu.fs_frag1_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu1:
                                showMyMenuDialog(appContext,fitnessOptions,textViewContainer,ListenerManager);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        // bottom menu 만큼의 빈공간 여유를 두기 위함.
        rootView.findViewById(R.id.space_bottom_menu).setMinimumHeight(getActivity().findViewById(R.id.bottom_menu).getHeight());

        return rootView;
    }
    public void UpdateMyMenu(Context appContext,FitnessOptions fitnessOptions,LinearLayout textViewContainer,OnDataPointListener[] ListenerManager){
        // 우선 View Clear
        textViewContainer.removeAllViews();

        // 기존의 Listener들을 전부 제거해줌.
        for(int index = 0;index < 3 ; index++){
            if(ListenerManager[index] != null)
            {
                Fitness.getSensorsClient(getContext(),GoogleSignIn.getAccountForExtension(appContext, fitnessOptions))
                        .remove(ListenerManager[index])
                        .addOnSuccessListener(unused -> Log.i("ListenerManager","Listener has been removed!"))
                        .addOnFailureListener(e->Log.i("ListenerManager","Removing Listener has failed"));
                ListenerManager[index] = null;
            }
        }

        // 그 다음 View를 동적으로 할당해주면서 추가.
        if(PreferenceManager.getBoolean(appContext,OAUTH_TAG)){
            int loop_count = checkArray.length;
            for(int i = 0;i<loop_count;i++){
                if(checkArray[i]){
                    // MyMenu에서 보여줄 정보들에 대한 View 생성 및 값 갱신.
                    // 해당 정보에 대한 Subscribe 진행.
                    TextView temp = new TextView(getContext());
                    temp.setTextSize(12);
                    temp.setId(textViewIds[i]);
                    temp.setWidth(textViewContainer.getWidth());
                    textViewContainer.addView(temp);
                    setMyMenuData(appContext,fitnessOptions,temp,i,ListenerManager);
                }
            }
        }
    }
    public void showGoalSettingDialog(){
        final int[] which = new int[1];

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("금일의 목표!");

        // 여기에 icon 하나 추가해서 넣으면 좋을듯.
        dialog.setIcon(R.drawable.ic_checkbox_checked);

        dialog.setSingleChoiceItems(goal_choice, PreferenceManager.getInt(mContext,pref_goalIndex), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // which[0]에는 선택된 목표 걸음수의 index값이 저장.
                which[0] = i;
            }
        });
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /** 목표 걸음수를 저장.**/
                int goal_step = goal_choice_int[which[0]];

                // 1. Progressbar의 목표 걸음수 갱신.
                text_goal.setText(String.valueOf(goal_step));

                // 2. ProgressBar 최대값 설정.
                progressBar.setMax(goal_step);

                // 2. PreferenceSharedData에 목표 걸음수 저장.
                PreferenceManager.setInt(mContext,pref_goal,goal_step);

                // 3. PreferenceSharedData에 which[0] 값 저장.
                PreferenceManager.setInt(mContext,pref_goalIndex,which[0]);
            }
        });
        dialog.show();
    }
    public void showMyMenuDialog(Context appContext,FitnessOptions fitnessOptions,LinearLayout textViewContainer,OnDataPointListener[] ListenerManager){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        //다이얼로그창 제목.
        dialog.setTitle("뭐가 필요할까");

        dialog.setIcon(R.drawable.ic_checkbox_checked);

        dialog.setMultiChoiceItems(choiceArray, checkArray, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {

            }
        });
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 현재 checkArray에 체크시 true, 체크 안할시 false가 들어가 있음.
                // 여기서 tablelayout에 들어갈 table item들을 넣어주면 될듯.
                PreferenceManager.setBooleanArray(mContext,myMenuName,checkArray);
                checkArray = PreferenceManager.getBooleanArray(mContext,myMenuName,choiceArray.length);
                // View도 다시 동적할당.
                UpdateMyMenu(appContext,fitnessOptions,textViewContainer,ListenerManager);
            }
        });
        dialog.show();
    }
    public void setMyMenuData(Context appContext, FitnessOptions fitnessOptions,TextView textView,int index,OnDataPointListener[] ListenerManager){
        // this is the very basic information that we should be listening to at all time.
        Fitness.getRecordingClient(getActivity(), GoogleSignIn.getLastSignedInAccount(appContext))
                .subscribe(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "TYPE_STEP_COUNT_DELTA | Successfully subscribed!");
                                    readStepData(appContext,textView,fitnessOptions);
                                } else {
                                    Log.d(TAG, "There was a problem subscribing.", task.getException());
                                }
                            }
                        });
        switch(index){
            case 0:
                Fitness.getRecordingClient(getActivity(), GoogleSignIn.getLastSignedInAccount(appContext))
                        .subscribe(DataType.TYPE_CALORIES_EXPENDED)
                        .addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "TYPE_CALORIES_EXPENDED | Successfully subscribed!");
                                            readCaloriesData(appContext,textView,fitnessOptions,ListenerManager);
                                        } else {
                                            Log.d(TAG, "There was a problem subscribing.", task.getException());
                                        }
                                    }
                                });
                break;
            case 1:
                Fitness.getRecordingClient(getActivity(), GoogleSignIn.getLastSignedInAccount(appContext))
                        .subscribe(DataType.TYPE_DISTANCE_DELTA)
                        .addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "TYPE_DISTANCE_DELTA | Successfully subscribed!");
                                            readDistanceData(appContext,textView,fitnessOptions,ListenerManager);
                                        } else {
                                            Log.d(TAG, "There was a problem subscribing.", task.getException());
                                        }
                                    }
                                });
                break;
            case 2:
                Fitness.getRecordingClient(getActivity(), GoogleSignIn.getLastSignedInAccount(appContext))
                        .subscribe(DataType.TYPE_SPEED)
                        .addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "TYPE_SPEED | Successfully subscribed!");
                                            readSpeedData(appContext,textView,fitnessOptions,ListenerManager);
                                        } else {
                                            Log.d(TAG, "There was a problem subscribing.", task.getException());
                                        }
                                    }
                                });
                break;
        }
    }
    public void readStepData(Context appContext, TextView textView, FitnessOptions fitnessOptions){
        Log.d("readStepData"," In");
        Fitness.getHistoryClient(getContext(), GoogleSignIn.getLastSignedInAccount(appContext))
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {

                                for (DataPoint dp : dataSet.getDataPoints()) {
                                    for(Field field : dp.getDataType().getFields()) {
                                        Log.d("Stream Name : ", dp.getOriginalDataSource().getStreamName());
                                        if(!"user_input".equals(dp.getOriginalDataSource().getStreamName())){
                                            int steps = dp.getValue(field).asInt();
                                            userInputSteps = steps;
                                        }
                                    }
                                }
                                textView.setText("걸음수 : " + String.valueOf(userInputSteps));
                                current_step.setText(String.valueOf(userInputSteps)+" 걸음");
                                progressBar.setProgress(userInputSteps); // ProgressBar update
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "There was a problem getting the step count.", e);
                            }
                        });


        OnDataPointListener listener = dataPoint -> {
            for(Field field : dataPoint.getDataType().getFields()) {
                Log.d("Stream Name2 : ", dataPoint.getOriginalDataSource().getStreamName());

                if(!"user_input".equals(dataPoint.getOriginalDataSource().getStreamName())){
                    int steps = dataPoint.getValue(field).asInt();
                    userInputSteps += steps;
                }
            }
            textView.setText("걸음수 : "+String.valueOf(userInputSteps));
            current_step.setText(String.valueOf(userInputSteps)+" 걸음");
            progressBar.setProgress(userInputSteps);
        };

        Fitness.getSensorsClient(getContext(), GoogleSignIn.getAccountForExtension(appContext, fitnessOptions))
                .add(
                        new SensorRequest.Builder()
                                .setDataType(DataType.TYPE_STEP_COUNT_DELTA) // Can't be omitted.
                                .setSamplingRate(1, TimeUnit.SECONDS)
                                .build(),
                        listener
                )
                .addOnSuccessListener(unused ->
                        Log.i(TAG, "Listener registered!"))
                .addOnFailureListener(task ->
                        Log.e(TAG, "Listener not registered.", task.getCause()));
    }
    public void readCaloriesData(Context appContext, TextView textView, FitnessOptions fitnessOptions,OnDataPointListener[] ListenerManager){
        Fitness.getHistoryClient(getContext(), GoogleSignIn.getLastSignedInAccount(appContext))
                .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                Log.d("readData2","here2");

                                for (DataPoint dp : dataSet.getDataPoints()) {
                                    for(Field field : dp.getDataType().getFields()) {
                                        Log.d("Stream Name : ", dp.getOriginalDataSource().getStreamName());
                                        if(!"user_input".equals(dp.getOriginalDataSource().getStreamName())){
                                            float calories = dp.getValue(field).asFloat();
                                            userInputCalories = Math.round(calories);
                                        }
                                    }
                                }
                                textView.setText("소모 칼로리 : " + String.valueOf(userInputCalories) + " Kcal");
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("readData2","here3");
                                Log.w(TAG, "There was a problem getting the step count.", e);
                            }
                        });

        OnDataPointListener listener = dataPoint -> {
            for(Field field : dataPoint.getDataType().getFields()) {
                Log.d("Stream Name2 : ", dataPoint.getOriginalDataSource().getStreamName());

                if(!"user_input".equals(dataPoint.getOriginalDataSource().getStreamName())){
                    float steps = dataPoint.getValue(field).asFloat();
                    userInputCalories += steps;
                }
            }
            textView.setText("소모 칼로리 : "+String.valueOf(userInputCalories) + " Kcal");
        };
        ListenerManager[CAL_LISTENER] = listener;

        Fitness.getSensorsClient(getContext(), GoogleSignIn.getAccountForExtension(appContext, fitnessOptions))
                .add(
                        new SensorRequest.Builder()
                                .setDataType(DataType.TYPE_CALORIES_EXPENDED) // Can't be omitted.
                                .setSamplingRate(1, TimeUnit.SECONDS)
                                .build(),
                        listener
                )
                .addOnSuccessListener(unused ->
                        Log.i(TAG, "Listener registered!"))
                .addOnFailureListener(task ->
                        Log.e(TAG, "Listener not registered.", task.getCause()));
    }
    public void readDistanceData(Context appContext, TextView textView, FitnessOptions fitnessOptions,OnDataPointListener[] ListenerManager){
        Log.d("readData3","here3");
        Fitness.getHistoryClient(getContext(), GoogleSignIn.getLastSignedInAccount(appContext))
                .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {

                                for (DataPoint dp : dataSet.getDataPoints()) {
                                    for(Field field : dp.getDataType().getFields()) {
                                        Log.d("Stream Name : ", dp.getOriginalDataSource().getStreamName());
                                        if(!"user_input".equals(dp.getOriginalDataSource().getStreamName())){
                                            float dis = dp.getValue(field).asFloat();
                                            userInputDistance = Math.round(dis);
                                        }
                                    }
                                }
                                textView.setText("이동 거리 : "+String.valueOf(userInputDistance) +" m");
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("readData3","here3");
                                Log.w(TAG, "There was a problem getting the step count.", e);
                            }
                        });


        OnDataPointListener listener = dataPoint -> {
            for(Field field : dataPoint.getDataType().getFields()) {
                Log.d("Stream Name2 : ", dataPoint.getOriginalDataSource().getStreamName());

                if(!"user_input".equals(dataPoint.getOriginalDataSource().getStreamName())){
                    float dis = dataPoint.getValue(field).asFloat();
                    userInputDistance += dis;
                }
            }
            textView.setText("이동 거리 : "+String.valueOf(userInputDistance) + " m");
        };
        ListenerManager[DIS_LISTENER] = listener;

        Fitness.getSensorsClient(getContext(), GoogleSignIn.getAccountForExtension(appContext, fitnessOptions))
                .add(
                        new SensorRequest.Builder()
                                .setDataType(DataType.TYPE_DISTANCE_DELTA) // Can't be omitted.
                                .setSamplingRate(1, TimeUnit.SECONDS)
                                .build(),
                        listener
                )
                .addOnSuccessListener(unused ->
                        Log.i(TAG, "Listener registered!"))
                .addOnFailureListener(task ->
                        Log.e(TAG, "Listener not registered.", task.getCause()));
    }
    public void readSpeedData(Context appContext, TextView textView, FitnessOptions fitnessOptions,OnDataPointListener[] ListenerManager){
        Log.d("readData4","here4");
        ZonedDateTime startTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

        DataReadRequest request = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_SPEED)
                .bucketByTime(1,TimeUnit.DAYS)
                .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(),TimeUnit.SECONDS)
                .build();

        Fitness.getHistoryClient(getContext(), GoogleSignIn.getLastSignedInAccount(appContext))
                .readData(request)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        Log.d("AGGREGATE_SPEED : ","process name getHistoryClient has been successfully done");
                        for(Bucket bucket : dataReadResponse.getBuckets()){
                            Log.d("Bucket reading :", "Success");
                            for(DataSet dataset : bucket.getDataSets()){
                                Log.d("DataSet reading :", "Success");
                                if(dataset.getDataPoints().isEmpty()) Log.d("DataPoint","Empty");
                                for(DataPoint dp : dataset.getDataPoints()){
                                    Log.d("DataPoint reading :", "Success");
                                    for(Field field : dp.getDataType().getFields()){
                                        Log.d("Field reading :", "Success");
                                        if("average".equals(field.getName())){
                                            userInputSpeed = Math.round(dp.getValue(field).asFloat());
                                        }
                                    }
                                }
                            }
                        }
                        textView.setText("평균 속도 : " + String.valueOf(userInputSpeed)+" m/s");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("readData4","Here's some problem of getting history client");
                    }
                });


        OnDataPointListener listener = dataPoint -> {
            for(Field field: dataPoint.getDataType().getFields()) {
                Log.d("Field Name : ", field.getName());
                if ("average".equals(field.getName())) {
                    float avg_speed = dataPoint.getValue(field).asFloat();
                    userInputSpeed += avg_speed;
                }
            }
            textView.setText("평균 속도 : " + String.valueOf(userInputSpeed) +" m/s");
        };
        ListenerManager[SPD_LISTENER] = listener;

        Fitness.getSensorsClient(getContext(), GoogleSignIn.getAccountForExtension(appContext, fitnessOptions))
                .add(
                        new SensorRequest.Builder()
                                .setDataType(DataType.TYPE_SPEED) // Can't be omitted.
                                .setSamplingRate(1, TimeUnit.SECONDS)
                                .build(),
                        listener
                )
                .addOnSuccessListener(unused ->
                        Log.i(TAG, "Listener registered!"))
                .addOnFailureListener(task ->
                        Log.e(TAG, "Listener not registered.", task.getCause()));
    }
}