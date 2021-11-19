package com.example.capstonedesign;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonedesign.retrofit.Friend;
import com.example.capstonedesign.retrofit.FriendListResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        ListView listView = (ListView) findViewById(R.id.friendList_listView);
        ImageView btn_friendReqest = findViewById(R.id.btn_friend_request_list);
        ImageView btn_addFriend = findViewById(R.id.btn_add_friend);

        Dialog dialogFR = new Dialog(this);
        Dialog dialogAF = new Dialog(this);
        dialogFR.setContentView(R.layout.dialog_friendrequest);
        dialogAF.setContentView(R.layout.dialog_addfriend);

        /** 다이얼로그 세팅 **/


        btn_addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAF(dialogAF);
            }
        });
        btn_friendReqest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFR(dialogFR);
            }
        });

        /** Adapter와 ArrayList 설정 **/
        ArrayList<Friend> arrayList = new ArrayList<Friend>();

        RetrofitClient retrofitClient = RetrofitClient.getNewInstance(getApplicationContext());
        initMyApi initMyApi = RetrofitClient.getRetrofitInterface();
        retrofitClient.setContext(getApplicationContext());
        final MyArrayAdapter[] myArrayAdapter = new MyArrayAdapter[1];
        initMyApi.getFriendListResponse().enqueue(new Callback<FriendListResponse>() {
            @Override
            public void onResponse(Call<FriendListResponse> call, Response<FriendListResponse> response) {
                FriendListResponse result = response.body();

                String status_msg = result.getStatus();
                Log.d("Getting friendsList",status_msg);

                List<Friend> friendsList = result.getFriendList();
                arrayList.addAll(friendsList);

                final MyFriendListAdapter myFriendListAdapter = new MyFriendListAdapter(getBaseContext(),arrayList);

                listView.setAdapter(myFriendListAdapter);
            }
            @Override
            public void onFailure(Call<FriendListResponse> call, Throwable t) {
            }
        });
    }

    public void showDialogFR(Dialog dialog){
        ListView listView = dialog.findViewById(R.id.follow_list);
        TabLayout tablayout = dialog.findViewById(R.id.tab);

        // Retrofit으로 나한테 온 요청 확인.
        // Adapter Setting.
        // listview에 adapter 할당.

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // 우선 ArrayList clear해주고
                // Retrofit의 response에서 데이터를 받아서
                // 새로 데이터 추가한후
                // response에서 adapter.notifyDataSetChanged()를 통해 listview 갱신.
                if(position == 0){
                    // 내 요청
                }else{
                    // 받은 요청
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        dialog.show();
    }
    public void showDialogAF(Dialog dialog){
        TextView btn_send = dialog.findViewById(R.id.btn_send);
        EditText input_email = dialog.findViewById(R.id.input_email);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = input_email.getText().toString();

                // Retrofit으로 친구 요청 전송.
            }
        });
        dialog.show();
    }
}