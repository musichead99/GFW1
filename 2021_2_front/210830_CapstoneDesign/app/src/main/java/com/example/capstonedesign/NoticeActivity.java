package com.example.capstonedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoticeActivity extends AppCompatActivity {
    TextView notice1_title, notice2_title, notice1, notice2;
    boolean result1 = true, result2 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        notice1_title = findViewById(R.id.notice1_title);
        notice2_title = findViewById(R.id.notice2_title);
        notice1 = findViewById(R.id.notice1);
        notice2 = findViewById(R.id.notice2);

        notice1_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result1 == true) {
                    notice1.setVisibility(View.VISIBLE);
                    result1 = false;
                } else {
                    notice1.setVisibility(View.GONE);
                    result1 = true;
                }
            }
        });
        notice2_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result2 == true) {
                    notice2.setVisibility(View.VISIBLE);
                    result2 = false;
                } else {
                    notice2.setVisibility(View.GONE);
                    result2 = true;
                }
            }
        });
    }
}