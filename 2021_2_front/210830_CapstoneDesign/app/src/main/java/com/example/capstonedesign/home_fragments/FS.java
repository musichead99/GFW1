package com.example.capstonedesign.home_fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.capstonedesign.Home;
import com.example.capstonedesign.PreferenceManager;
import com.example.capstonedesign.R;

public class FS extends Fragment {
    FL fl;
    Context mContext;
    private static String name_btn_goal = "btn_goal";
    TextView text_goal;

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
    String[] choiceArray = new String[] {"소모 칼로리 : 100cal","걸음수 : 100보","거리 : 0.2km"};

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fs,container,false);
        mContext = getContext();

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

        /** 주기적으로 갱신해 줘야할 값들을 갱신.
        final Timer timer;
        TimerTask timerTask;
        final long time = 10000;

        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                fs_frag1.page1.EditMyMenuItem(fs_frag1.getContext(),fs_frag1.choiceArray,fs_frag1.myMenuName,fs_frag1.choiceArray.length);
            }

            @Override
            public boolean cancel() {
                return super.cancel();
            }
        };
        timer.schedule(timerTask,0,time);**/

        
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
                                showMyMenuDialog();
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
                // 1. Progressbar의 목표 걸음수 갱신.
                text_goal.setText(String.valueOf(goal_choice_int[which[0]]));

                // 2. Progressbar 자체의 최대값 갱신.


                // 3. PreferenceSharedData에 목표 걸음수 저장.
                PreferenceManager.setInt(mContext,pref_goal,goal_choice_int[which[0]]);

                // 4. PreferenceSharedData에 which[0] 값 저장.
                PreferenceManager.setInt(mContext,pref_goalIndex,which[0]);
            }
        });

        dialog.show();
    }
    public void showMyMenuDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        //다이얼로그창 제목.
        dialog.setTitle("뭐가 필요할까");

        // 앱 데이터에 저장해놓은 정보를 토대로 checkArray의 초기 세팅을 해주면 됨. 현재는 그냥 넣음.
        // 초기에는 default로 들어감.
        final boolean[] checkArray = PreferenceManager.getBooleanArray(mContext,myMenuName,choiceArray.length);

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

            }
        });
        dialog.show();
    }
}