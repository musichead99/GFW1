package com.example.capstonedesign;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.capstonedesign.retrofit.RePassRequest;
import com.example.capstonedesign.retrofit.RePassResponse;
import com.example.capstonedesign.retrofit.RegisterResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Password extends AppCompatActivity {

    private EditText pw, repw;
    private initMyApi initMyApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        EditText pw = findViewById(R.id.pw);
        EditText repw = findViewById(R.id.repw);
        ImageView setImage = findViewById(R.id.setImage);

        Button check = findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReResponse();
            }
        });

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
    public void ReResponse() {
        String userPwd = pw.getText().toString().trim();
        String PassCK = repw.getText().toString().trim();

        RePassRequest rePassRequest = new RePassRequest(userPwd);

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        initMyApi.getRePassResponse(rePassRequest).enqueue(new Callback<RePassResponse>() {
            @Override
            public void onResponse(Call<RePassResponse> call, Response<RePassResponse> response) {
                if(userPwd.equals(PassCK)) {
                    if(response.isSuccessful() && response.body() != null) {
                        RePassResponse result = response.body();
                        String message = result.getMessage();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(),"비밀번호가 변경되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"다시 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RePassResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"회원 등록에 실패하였습니다.",Toast.LENGTH_SHORT).show();
            }
        });

    }
}