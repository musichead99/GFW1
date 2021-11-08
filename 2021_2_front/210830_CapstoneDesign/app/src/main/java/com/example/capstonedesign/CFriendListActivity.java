package com.example.capstonedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.capstonedesign.retrofit.Friend;

import java.util.ArrayList;

public class CFriendListActivity extends AppCompatActivity {

    private static CheckBox checkBox = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cfriend_list);

        ListView listView = findViewById(R.id.cfriend_list_listView);
        CheckBox prev_checkBox = null;


        // 1. Retrofit을 이용해서 POST 방식으로 친구의 목록(아마 배열의 형태로 받아올 것이라 예상)을 받아오기.
        // 2. 받아온 친구 목록을 ArrayList 형태의 friends 변수에 add 해줌.
        // 3. ArrayAdapter를 통해서 listView에 관련 정보를 띄워줌. (제일 처음으로는 선택 안함이라는
        //    Friend 객체를 생성해서 넣을 것임.)
        // 4.

        ArrayList<Friend> friends = new ArrayList<Friend>();

        MyArrayAdapter myArrayAdapter = new MyArrayAdapter(this,friends);

        listView.setAdapter(myArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(checkBox == null) checkBox = view.findViewById(R.id.cfriend_list_Item_check_box);
                Friend friend = (Friend) parent.getItemAtPosition(position);
                checkBox.setChecked(false);
                checkBox = view.findViewById(R.id.cfriend_list_Item_check_box);
                checkBox.setChecked(true);
            }
        });

    }
}