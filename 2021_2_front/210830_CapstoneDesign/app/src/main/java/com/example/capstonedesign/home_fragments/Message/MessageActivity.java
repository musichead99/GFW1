package com.example.capstonedesign.home_fragments.Message;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.capstonedesign.MyArrayAdapter;
import com.example.capstonedesign.MySQLite.DbOpenHelper;
import com.example.capstonedesign.R;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private DbOpenHelper dbOpenHelper;
    private ArrayList<Message> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        dbOpenHelper = new DbOpenHelper(this);
        dbOpenHelper.open();
        dbOpenHelper.create();

        ListView listView = (ListView) findViewById(R.id.act2_listView);

        arrayList = getDatasFromDB();

        final MessageListAdapter messageListAdapter = new MessageListAdapter(this,arrayList);

        listView.setAdapter(messageListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Click 시 해당 아이템 ArrayList에서 삭제.
                arrayList.remove(position);
                messageListAdapter.notifyDataSetChanged();
            }
        });

        ImageView btn = findViewById(R.id.messagelist_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbOpenHelper.deleteAllColumns();
                putArrayListToDB();
                finish();
            }
        });
    }

    public ArrayList<Message> getDatasFromDB(){
        ArrayList<Message> arrayList = new ArrayList<Message>();
        Cursor iCursor = dbOpenHelper.selectColumns();

        while(iCursor.moveToNext()){
            String profile_img = iCursor.getString(iCursor.getColumnIndex("profilePhoto"));
            String title = iCursor.getString(iCursor.getColumnIndex("title"));
            String content = iCursor.getString(iCursor.getColumnIndex("content"));
            arrayList.add(new Message(profile_img,title,content));
        }

        return arrayList;
    }

    public void putArrayListToDB(){
        int size = arrayList.size();
        for(int i=0;i < size;i++){
            Message msg = arrayList.get(i);
            dbOpenHelper.insertColumn(msg.getImage(),msg.getMsgTitle(),msg.getMsgContent());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        dbOpenHelper.deleteAllColumns();
        putArrayListToDB();
        return super.onKeyDown(keyCode, event);
    }
}