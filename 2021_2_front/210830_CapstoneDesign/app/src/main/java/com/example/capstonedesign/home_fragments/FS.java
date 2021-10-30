package com.example.capstonedesign.home_fragments;

import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.example.capstonedesign.Home;
import com.example.capstonedesign.PreferenceManager;
import com.example.capstonedesign.R;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataUpdateListenerRegistrationRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;

import java.util.Timer;
import java.util.TimerTask;

public class FS extends Fragment {
    FL fl;
    private Context mContext;
    private static String name_btn_goal = "btn_goal";
    public TextView text_goal;
    public TextView current_step;

    /** Dialog에서 String 형태로 인자 전달을 위해 choice는 String으로 하고 int 값은 따로 저장. **/
    final String[] goal_choice = {"1000 보","5000 보","10000 보"};
    final int[] goal_choice_int = {1000,5000,10000};
    final String pref_goal = "goal";
    final String pref_goalIndex = "goalIndex";
    int goalStep;

    /** ProgressBar **/
    public ProgressBar progressBar;

    /** My메뉴 **/
    String myMenu_name = "Mymenu";
    public static final String[] myMenu_choice = new String[] {"소모 칼로리","이동 거리","이동 시간"};
    public static final int myMenu_length = myMenu_choice.length;
    boolean[] myMenu_check; // MyMenu에서 볼 정보들에 대한 정보 저장.
    public static float[] ActDatas = new float[myMenu_length+1];
    public static TextView[] textView_actData;

    /** Listener 대체용 Timer **/
    Timer scheduler = new Timer();
    TimerTask task;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fs,container,false);
        mContext = getContext();

        progressBar = rootView.findViewById(R.id.progressbar_goal);
        current_step = rootView.findViewById(R.id.current_step);

        // App의 Context를 저장.
        Context appContext = getActivity().getApplicationContext();
        // fitnessOptions Config
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_CALORIES_EXPENDED,FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_DISTANCE_DELTA,FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_MOVE_MINUTES,FitnessOptions.ACCESS_READ)
                        .build();

        textView_actData = new TextView[]{
                rootView.findViewById(R.id.textView_cal),
                rootView.findViewById(R.id.textView_dis),
                rootView.findViewById(R.id.textView_moveMin)
        };

        // 처음에 checkArray의 정보를 불러와서 저장.
        myMenu_check = PreferenceManager.getBooleanArray(mContext, myMenu_name, myMenu_length);

        /** ListnerManager cover area of each index
         * index[0] : TYPE_STEP_COUNT_DELTA
         * index[1] : TYPE_CALORIES_EXPENDED
         * index[2] : TYPE_DISTANCE_DELTA
         * index[3] : TYPE_MOVE_MINUTES
         * **/
        OnDataPointListener[] ListenerManager = new OnDataPointListener[5];

        /** myGoogleFit 사용 순서.
         * 이제 준비를 마쳤음으로 Google fit subscription & Listener 등록이 이루어 져야 함.
         * 1. MyGoogleFit 객체 생성.
         * 2. subscription 진행.
         *      1) myMenu_check를 확인해서 현재 보여주려고 하는 데이터 확인.
         *      2) 해당 타입의 데이터들을 subscription.
         * 3. Dailytotal가져오기.
         * 4. Listener등록.
         * **/
        MyGoogleFit myGoogleFit = MyGoogleFit.getInstance()
                .setAppContext(appContext)
                .setFitnessOptions(fitnessOptions);

        // sub할 dataType을 구하는 부분.
        int dataType = 1;
        for(int i=0;i<myMenu_length;i++){
            boolean tf = myMenu_check[i];
            // myMenu_check에는 step에 대한 정보는 X,
            if(tf == true)dataType += 1<<(i+1);
            else textView_actData[i].setVisibility(TextView.GONE);
        }
        // subscription & updating dailytotal.
        myGoogleFit.subDailyData(dataType,getContext());
                /*
                .updateDailyTotal(getContext(),dataType,ActDatas,textView_actData,current_step,progressBar)
                .addListener(getContext(),1,ListenerManager,ActDatas,current_step,progressBar)
                .addListener(getContext(),dataType,ListenerManager,ActDatas,textView_actData,myMenu_length);
                 */

        int finalDataType = dataType;
        task = new TimerTask() {
            @Override
            public void run() {
                Log.d("TimerTask","Tick & Tok");
                myGoogleFit.updateDailyTotal(getContext(), finalDataType,ActDatas,textView_actData,current_step,progressBar);
            }
        };
        scheduler.schedule(task,0,60000); // 0초 뒤 1분마다 반복실행.
        
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

        /** 목표 걸음수 ProgressBar 초기 세팅 **/
        progressBar.setMax(goalStep);

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
        fl = new FL();
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
                                showMyMenuDialog(ListenerManager);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        task.cancel();
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
    public void showMyMenuDialog(OnDataPointListener[] ListenerManager){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        //다이얼로그창 제목.
        dialog.setTitle("뭐가 필요할까");

        dialog.setIcon(R.drawable.ic_checkbox_checked);

        dialog.setMultiChoiceItems(myMenu_choice, myMenu_check, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {

            }
        });

        MyGoogleFit myGoogleFit = MyGoogleFit.getInstance();

        // 이전과 비교하기 위함.
        boolean[] prev_myMenu_check = new boolean[myMenu_length];
        for(int count=0;count<myMenu_length;count++){
            prev_myMenu_check[count] = myMenu_check[count];
        }

        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 현재 checkArray에 체크시 true, 체크 안할시 false가 들어가 있음.
                Log.d("MyMenu","DiaLog");

                PreferenceManager.setBooleanArray(mContext, myMenu_name, myMenu_check);
                myMenu_check = PreferenceManager.getBooleanArray(mContext, myMenu_name, myMenu_length);

                /** 여기서 MyMenu에 들어갈 보여줄 View가 수정.
                 * Tasks
                 * 1. 필요없는 데이터 타입 Listener 제거.
                 * 2. 필요없는 데이터 타입 unsubscribe.
                 * 3. 필요한 데이터 타입 다시 subscribe
                 * 4. 필요한 데이터 타입 DailyData 다시 가져오기.
                 * 5. 필요한 데이터 타입 Listener 등록.
                 * **/
                int dataType=1;
                for(int count=0;count<myMenu_length;count++){

                    if(prev_myMenu_check[count] != myMenu_check[count]){
                        dataType += 1<<(count+1);
                        Log.d("MyMenuEdit","checking");
                        if(!myMenu_check[count]){

                            // Step 1.
                            scheduler.cancel();
                            // Step 2.
                            myGoogleFit.unsubDailyData(dataType,getContext());
                            textView_actData[count].setVisibility(TextView.GONE);
                        }else{
                            // Step 3 & 4
                            myGoogleFit.subDailyData(dataType,mContext);
                            textView_actData[count].setVisibility(TextView.VISIBLE);
                        }
                    }
                }
                int finalDataType = dataType;
                task = new TimerTask() {
                    @Override
                    public void run() {
                        Log.d("TimerTask","Tick & Tok");
                        myGoogleFit.updateDailyTotal(getContext(), finalDataType,ActDatas,textView_actData,current_step,progressBar);
                    }
                };
                scheduler.schedule(task,0,60000);
            }
        });
        dialog.show();
    }
}
