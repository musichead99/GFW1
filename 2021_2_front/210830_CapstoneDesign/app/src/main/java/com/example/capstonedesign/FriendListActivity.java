package com.example.capstonedesign;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonedesign.home_fragments.FR_ListAdapter;
import com.example.capstonedesign.retrofit.AddFriendRequest;
import com.example.capstonedesign.retrofit.AddFriendResponse;
import com.example.capstonedesign.retrofit.Friend;
import com.example.capstonedesign.retrofit.FriendListResponse;
import com.example.capstonedesign.retrofit.FriendRequestList.FriendRequestListResponse;
import com.example.capstonedesign.retrofit.FriendRequestList.FriendRequestedListResponse;
import com.example.capstonedesign.retrofit.FriendStepDataResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
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
        ImageView btn_back = findViewById(R.id.friendlist_btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        initMyApi initMyApi = RetrofitClient.getNewRetrofitInterface();
        
        final MyArrayAdapter[] myArrayAdapter = new MyArrayAdapter[1];
        initMyApi.getFriendListResponse().enqueue(new Callback<FriendListResponse>() {
            @Override
            public void onResponse(Call<FriendListResponse> call, Response<FriendListResponse> response) {
                if(response.isSuccessful()){
                    FriendListResponse result = response.body();

                    String status_msg = result.getStatus();
                    Log.d("Getting friendsList",status_msg);

                    List<Friend> friendsList = result.getFriendList();
                    arrayList.addAll(friendsList);

                    final MyFriendListAdapter myFriendListAdapter = new MyFriendListAdapter(getBaseContext(),arrayList);

                    listView.setAdapter(myFriendListAdapter);
                }
            }
            @Override
            public void onFailure(Call<FriendListResponse> call, Throwable t) {
            }
        });
    }

    public void showDialogFR(Dialog dialog){
        ListView listView = dialog.findViewById(R.id.follow_list);
        TabLayout tablayout = dialog.findViewById(R.id.tab);
        ArrayList<Friend> arrayList = new ArrayList<Friend>();
        final FR_ListAdapter[] frListAdapter = new FR_ListAdapter[1];

        frListAdapter[0] = new FR_ListAdapter(getBaseContext(),arrayList);
        frListAdapter[0].setRetrofit();

        listView.setAdapter(frListAdapter[0]);

        // Retrofit으로 나한테 온 요청 확인.
        RetrofitClient retrofitClient = RetrofitClient.getNewInstance(getApplicationContext());
        initMyApi initMyApi = RetrofitClient.getNewRetrofitInterface();

        initMyApi.FriendRequestList().enqueue(new Callback<FriendRequestListResponse>() {
            @Override
            public void onResponse(Call<FriendRequestListResponse> call, Response<FriendRequestListResponse> response) {
                if(response.isSuccessful()){
                    FriendRequestListResponse result = response.body();
                    List<Friend> list = result.getMyRequestList();
                    String status = result.getStatus();

                    arrayList.clear();

                    arrayList.addAll(list);

                    frListAdapter[0].setListType(FR_ListAdapter.REQUESTING);
                    frListAdapter[0].notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<FriendRequestListResponse> call, Throwable t) {
            }
        });


        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // 우선 ArrayList clear해주고
                arrayList.clear();

                RetrofitClient retrofitClient = RetrofitClient.getNewInstance(getApplicationContext());
                initMyApi initMyApi = RetrofitClient.getNewRetrofitInterface();

                // 새로 데이터 추가한후
                // response에서 adapter.notifyDataSetChanged()를 통해 listview 갱신.
                if(position == 0){
                    // 내가 한 요청
                    initMyApi.FriendRequestList().enqueue(new Callback<FriendRequestListResponse>() {
                        @Override
                        public void onResponse(Call<FriendRequestListResponse> call, Response<FriendRequestListResponse> response) {
                            if(response.isSuccessful()){
                                FriendRequestListResponse result = response.body();
                                List<Friend> newArrayList = result.getMyRequestList();
                                String status = result.getStatus();

                                arrayList.addAll(newArrayList);

                                frListAdapter[0].setListType(FR_ListAdapter.REQUESTING);
                            }
                            frListAdapter[0].notifyDataSetChanged();
                        }
                        @Override
                        public void onFailure(Call<FriendRequestListResponse> call, Throwable t) {}
                    });
                }else{
                    // 받은 요청
                    initMyApi.FriendRequestedList().enqueue(new Callback<FriendRequestedListResponse>() {
                        @Override
                        public void onResponse(Call<FriendRequestedListResponse> call, Response<FriendRequestedListResponse> response) {
                            if(response.isSuccessful()){
                                FriendRequestedListResponse result = response.body();
                                List<Friend> newArrayList = result.getFriendsRequestedList();
                                String status = result.getStatus();

                                arrayList.addAll(newArrayList);

                                frListAdapter[0].setListType(FR_ListAdapter.REQUESTED);
                            }
                            frListAdapter[0].notifyDataSetChanged();
                        }
                        @Override
                        public void onFailure(Call<FriendRequestedListResponse> call, Throwable t) {}
                    });
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
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

                RetrofitClient retrofitClient = RetrofitClient.getNewInstance(getApplicationContext());
                initMyApi initMyApi = RetrofitClient.getNewRetrofitInterface();

                AddFriendRequest addFriendRequest = new AddFriendRequest(email);

                initMyApi.AddFriend(addFriendRequest).enqueue(new Callback<AddFriendResponse>() {
                    @Override
                    public void onResponse(Call<AddFriendResponse> call, Response<AddFriendResponse> response) {
                        if(response.isSuccessful()){
                            AddFriendResponse result = response.body();
                            String status = result.getStatus();
                            String message = result.getMessage();

                            Log.d("AddFriendResponse_status",status);
                            Log.d("AddFriendResponse_message",message);
                        }
                        dialog.dismiss();
                    }
                    @Override
                    public void onFailure(Call<AddFriendResponse> call, Throwable t) {
                    }
                });
            }
        });
        dialog.show();
    }

}