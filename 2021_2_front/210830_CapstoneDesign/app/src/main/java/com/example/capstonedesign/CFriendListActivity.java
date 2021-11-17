package com.example.capstonedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.capstonedesign.retrofit.Friend;
import com.example.capstonedesign.retrofit.FriendListResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CFriendListActivity extends AppCompatActivity {

    private CheckBox checkBox = null;
    private String passing_email = null;
    private String friendName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cfriend_list);

        Context thisContext = this;
        ListView listView = findViewById(R.id.cfriend_list_listView);
        CheckBox prev_checkBox = null;
        TextView tb_confirm = findViewById(R.id.cfriend_list_confirm);

        ArrayList<Friend> al_friends = new ArrayList<Friend>();

        RetrofitClient retrofitClient = RetrofitClient.getNewInstance(getApplicationContext());
        initMyApi initMyApi = RetrofitClient.getRetrofitInterface();
        retrofitClient.setContext(getApplicationContext());
        initMyApi.getFriendListResponse().enqueue(new Callback<FriendListResponse>() {
            @Override
            public void onResponse(Call<FriendListResponse> call, Response<FriendListResponse> response) {
                FriendListResponse result = response.body();

                String status_msg = result.getStatus();
                Log.d("Getting friendsList",status_msg);

                List<Friend> friendsList = result.getFriendList();
                al_friends.add(new Friend(null,null,"선택안함"));
                al_friends.addAll(friendsList);

                MyArrayAdapter myArrayAdapter = new MyArrayAdapter(thisContext,al_friends);

                listView.setAdapter(myArrayAdapter);
                checkBox = myArrayAdapter.getFirstCheckBox();
            }
            @Override
            public void onFailure(Call<FriendListResponse> call, Throwable t) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemClick","In");

                Friend friend = (Friend) parent.getItemAtPosition(position);
                friendName = friend.getName();
                checkBox.setChecked(false);
                checkBox = view.findViewById(R.id.cfriend_list_Item_check_box);
                checkBox.setChecked(true);
                passing_email = friend.getEmail();
            }
        });

        tb_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기서 passing_email에 있는 값을 보내준다.
                Intent intent = new Intent(getBaseContext(),Home.class);
                intent.putExtra("IntentType",0);
                intent.putExtra("CompareFriend",passing_email);
                intent.putExtra("FriendName",friendName);
                startActivity(intent);
                finish();
            }
        });
    }
}