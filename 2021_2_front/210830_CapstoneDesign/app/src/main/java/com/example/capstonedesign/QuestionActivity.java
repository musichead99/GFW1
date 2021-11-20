package com.example.capstonedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {
    TextView que1_title, que2_title, que1, que2;
    boolean result1 = true, result2 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        que1_title = findViewById(R.id.que1_title);
        que2_title = findViewById(R.id.que2_title);
        que1 = findViewById(R.id.que1);
        que2 = findViewById(R.id.que2);

        que1_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result1 == true) {
                    que1.setVisibility(View.VISIBLE);
                    result1 = false;
                } else {
                    que1.setVisibility(View.GONE);
                    result1 = true;
                }
            }
        });
        que2_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result2 == true) {
                    que2.setVisibility(View.VISIBLE);
                    result2 = false;
                } else {
                    que2.setVisibility(View.GONE);
                    result2 = true;
                }
            }
        });
    }
}