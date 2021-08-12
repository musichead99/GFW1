package com.example.capstonedesign;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class Password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        EditText pw = (EditText)findViewById(R.id.pw);
        EditText repw = (EditText)findViewById(R.id.repw);
        ImageView setImage = (ImageView)findViewById(R.id.setImage);

        repw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pw.getText().toString().equals(repw.getText().toString())) {
                    setImage.setImageResource(R.drawable.correct);
                } else {
                    setImage.setImageResource(R.drawable.differ);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void registerAgain(View view) { //비밀번호 변경해서 새롭게 정보 등록해야함
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

        finish();
    }
}