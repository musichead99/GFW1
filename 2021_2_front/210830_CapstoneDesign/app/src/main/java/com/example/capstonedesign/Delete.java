package com.example.capstonedesign;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonedesign.retrofit.DeleteResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.initMyApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Delete extends AppCompatActivity {
    private initMyApi initMyApi;
    SharedPreferences sharedPreferences1;
    private boolean result=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(Delete.this);
        builder.setMessage("정말 회원 탈퇴를 하시겠습니까?\n"+"회원 탈퇴 시 걸음수를 포함한 모든 데이터가 삭제됩니다.");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result=true;
                delete();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void delete() {
        sharedPreferences1 = getSharedPreferences("email", MODE_PRIVATE);
        String mytoken = sharedPreferences1.getString("token","");

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        Call<DeleteResponse> call = initMyApi.getDeleteResponse("Bearer "+mytoken);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if(response.isSuccessful() && result==true) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"회원 탈퇴 성공",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"회원 탈퇴 실패",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"다시 시도해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
