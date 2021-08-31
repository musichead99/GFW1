package com.example.capstonedesign;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capstonedesign.retrofit.RegisterRequest;
import com.example.capstonedesign.retrofit.RegisterResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private static TextView textName;
    private static TextView textEmail;
    private static TextView textPwd;
    private initMyApi initMyApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        textEmail = findViewById(R.id.register_email);
        textName = findViewById(R.id.register_name);
        textPwd = findViewById(R.id.register_pw);

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
        RegisterResponse();

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

        finish();
    }

    public void goMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    public void RegisterResponse(){
        String userName = textName.getText().toString().trim();
        String userEmail = textEmail.getText().toString().trim();
        String userPwd = textPwd.getText().toString().trim();

        //RegisterRequest에 사용자가 입력한 이메일, 이름, 비밀번호 저장.
        RegisterRequest registerRequest = new RegisterRequest(userEmail,userPwd,userName);

        //retrofit 생성.
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        Log.d("retrofit","About to accessing to server");

        //registerRequest에 저장된 데이터와 함꼐 init에서 정의한 getRegisterResponse함수를 실행 후 응답을 받음.
        initMyApi.getRegisterResponse(registerRequest).enqueue(new Callback<RegisterResponse>() {
            // 성공시.
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                //통신이 성공함.
                if(response.isSuccessful() && response.body() !=null){
                    //response.body()을 result에 저장.
                    RegisterResponse result = response.body();

                    String message = result.getMessage();

                    Log.d("retrofit","Data fetch Success");
                }else{
                    Log.d("retrofit","Data fetch Fail1");
                }
            }

            //실패시.
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d("retrofit","Data fetch Fail2");
            }
        });


    }
}