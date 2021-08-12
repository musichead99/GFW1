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

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        EditText register_pw = (EditText)findViewById(R.id.register_pw);
        EditText register_pw_check = (EditText)findViewById(R.id.register_pw_check);
        ImageView setImage = (ImageView)findViewById(R.id.setImage);

        register_pw_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(register_pw.getText().toString().equals(register_pw_check.getText().toString())) {
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

    public void registerSuccess(View view) { //회원 가입이 성공해 원래 Main으로 넘어감
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

        finish();
    }

    public void goMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}