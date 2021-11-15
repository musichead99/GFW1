package com.example.capstonedesign;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstonedesign.retrofit.LoginRequest;
import com.example.capstonedesign.retrofit.LoginResponse;
import com.example.capstonedesign.retrofit.RegisterResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class Login extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;
    private EditText et_id, et_pass;
    private Button btn_login;
    SharedPreferences sharedPreferences, sharedPreferences1;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString().trim();
                String pw = et_pass.getText().toString().trim();
                hideKeyboard();

                if(id.trim().length() == 0 || pw.trim().length() == 0 || id == null || pw == null) {
                    Toast.makeText(getApplicationContext(),"로그인 정보를 입력해주세요.",Toast.LENGTH_SHORT).show();
                } else {
                    LoginResponse();
                }
            }
        });

    }

    public void LoginResponse() {
        String userEmail = et_id.getText().toString().trim();
        String userPass = et_pass.getText().toString().trim();
        sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences("name", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor1 = sharedPreferences1.edit();

        LoginRequest loginRequest = new LoginRequest(userEmail, userPass);

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        initMyApi.getLoginResponse(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    LoginResponse result = response.body();
                    String message = result.getMessage();
                    String token = result.getToken();
                    editor.putString("token", token);
                    editor.commit();
                    editor1.putString("userEmail", userEmail);
                    editor1.commit();

                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"다시 시도해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
    public void pwSearch(View view) {
        Intent intent = new Intent(this, Email.class);
        startActivity(intent);

        finish();
    }

    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_id.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et_pass.getWindowToken(), 0);
    }
    public void setPreference(String key, String value){ //데이터를 내부 저장소에 저장하기
        /*SharedPreferences pref = getSharedPreferences(DATA_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();*/
    }
}