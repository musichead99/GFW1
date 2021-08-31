package com.example.capstonedesign;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstonedesign.retrofit.RegisterRequest;
import com.example.capstonedesign.retrofit.RegisterResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.ValidateResponse;
import com.example.capstonedesign.retrofit.initMyApi;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.Call;

public class Register extends AppCompatActivity {

    private EditText register_name, register_email, register_pw, register_pw_check;
    private Button validateButton;
    private boolean validate=false, return_email=false, return_name=false, return_pw=false, return_pw_check=false;
    private initMyApi initMyApi;
    private String pw_regex = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8,20}.$";
    private TextView email_notion, pw_notion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        pw_notion=findViewById(R.id.pw_notion);
        register_pw = findViewById(R.id.register_pw);
        register_pw_check = findViewById(R.id.register_pw_check);
        ImageView setImage = findViewById(R.id.setImage);
        register_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pa=register_pw.getText().toString().trim();
                if(pa.matches(pw_regex)) {
                    pw_notion.setText("올바른 비밀번호 형식입니다.");
                    pw_notion.setTextColor(Color.parseColor("#000000"));
                    register_pw.setTextColor(Color.parseColor("#000000"));
                    return_pw=true;
                } else {
                    pw_notion.setText("8~20자 영어, 숫자, 특수문자를 사용해주세요.");
                    pw_notion.setTextColor(Color.parseColor("#FF0000"));
                    return_pw=false;

                }
            }
        });

        register_name = findViewById(R.id.register_name);
        register_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0) {
                    return_name=true;
                } else {
                    return_name=false;
                }
            }
        });

        email_notion = findViewById(R.id.email_notion);
        register_email = findViewById(R.id.register_email);
        register_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    email_notion.setText("이메일 형식으로 입력해주세요.");
                    email_notion.setTextColor(Color.parseColor("#FF0000"));
                    return_email=false;
                } else {
                    email_notion.setText("올바른 이메일 형식입니다.");
                    email_notion.setTextColor(Color.parseColor("#000000"));
                    register_email.setTextColor(Color.parseColor("#000000"));
                    return_email=true;
                }
            }
        });

        validateButton = findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateResponse();
            }
        });

        register_pw_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(return_pw==true && register_pw.getText().toString().equals(register_pw_check.getText().toString())) {
                    setImage.setImageResource(R.drawable.correct);
                    register_pw_check.setTextColor(Color.parseColor("#000000"));
                    return_pw_check=true;
                } else {
                    setImage.setImageResource(R.drawable.differ);
                    return_pw_check=false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void goMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    public void registerSuccess(View view) {
        RegisterResponse();
    }

    public void ValidateResponse() {
        String userEmail = register_email.getText().toString().trim();

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Log.d("retrofit", "About to accessing to server");

        Call<ValidateResponse> call = initMyApi.getValidateResponse(userEmail);

        call.enqueue(new Callback<ValidateResponse>() {
            @Override
            public void onResponse(Call<ValidateResponse> call, Response<ValidateResponse> response) {
                if(response.isSuccessful() && return_email==true) {
                    ValidateResponse result = response.body();
                    String status = result.getStatus();
                    Toast.makeText(getApplicationContext(),"사용할 수 있는 이메일입니다.",Toast.LENGTH_SHORT).show();
                    register_email.setInputType(InputType.TYPE_NULL);
                    validate=true;
                } else {
                    Toast.makeText(getApplicationContext(),"사용할 수 없는 이메일입니다.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValidateResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"다시 시도해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void RegisterResponse() {
        String userName = register_name.getText().toString().trim();
        String userEmail = register_email.getText().toString().trim();
        String userPwd = register_pw.getText().toString().trim();
        String PassCK = register_pw_check.getText().toString().trim();
        //registerRequest에 사용자가 입력한 email, pwd, name을 저장
        RegisterRequest registerRequest = new RegisterRequest(userEmail, userPwd, userName);

        //retrofit 생성
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        initMyApi.getRegisterResponse(registerRequest).enqueue(new Callback<RegisterResponse>() {
            // 성공시.
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.d("value","값들" + validate+return_name+return_pw_check);
                //통신이 성공함.
                if (userPwd.equals(PassCK) && validate==true && return_name == true && return_pw_check == true) {
                    if (response.isSuccessful() && response.body() != null) {
                        //response.body()을 result에 저장.
                        RegisterResponse result = response.body();
                        String message = result.getMessage();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(),"회원 등록에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"다시 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
            }

            //실패시.
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"회원 등록에 실패하였습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}