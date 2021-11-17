package com.example.capstonedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.capstonedesign.home_fragments.FL;
import com.example.capstonedesign.retrofit.ProfileResponse;
import com.example.capstonedesign.retrofit.RetrofitClient;
import com.example.capstonedesign.retrofit.UpdateProfileRequest;
import com.example.capstonedesign.retrofit.UpdateProfileResponse;
import com.example.capstonedesign.retrofit.initMyApi;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class imageActivity extends AppCompatActivity {
    private TextView name0, year0, house0;
    private EditText update_name;
    Spinner update_house, update_year_y, update_year_m, update_year_d;
    Button complete;
    private ImageView person0;
    private initMyApi initMyApi;
    SharedPreferences sharedPreferences1;
    ArrayAdapter<CharSequence> adspin, adspin1, adspin2, adspin3;
    String userName, userYear, userAbode;
    FL fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        name0 = findViewById(R.id.name);
        year0 = findViewById(R.id.year);
        house0 = findViewById(R.id.house);
        person0 = findViewById(R.id.person);

        update_name = findViewById(R.id.update_name);

        update_house = findViewById(R.id.update_house);
        adspin = ArrayAdapter.createFromResource(this, R.array.house, android.R.layout.simple_spinner_dropdown_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_house.setAdapter(adspin);

        update_year_y = findViewById(R.id.update_year_y);
        adspin1 = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_year_y.setAdapter(adspin1);

        update_year_m = findViewById(R.id.update_year_m);
        adspin2 = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_dropdown_item);
        adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_year_m.setAdapter(adspin2);

        update_year_d = findViewById(R.id.update_year_d);
        adspin3 = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_dropdown_item);
        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_year_d.setAdapter(adspin3);

        getProfile();

        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getSupportFragmentManager().beginTransaction().replace(R.id.container,fl).commit();
                Intent intent = new Intent(imageActivity.this, Home.class);
                startActivity(intent);
            }
        });

        Button update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name0.setVisibility(v.GONE);
                update_name.setVisibility(v.VISIBLE);
                year0.setVisibility(v.GONE);
                update_year_y.setVisibility(v.VISIBLE);
                update_year_m.setVisibility(v.VISIBLE);
                update_year_d.setVisibility(v.VISIBLE);
                house0.setVisibility(v.GONE);
                update_house.setVisibility(v.VISIBLE);

                putUpdate();
            }
        });
    }
    private void getProfile() {
        sharedPreferences1 = getSharedPreferences("email", MODE_PRIVATE);
        String mytoken = sharedPreferences1.getString("token","");

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        Call<ProfileResponse> call = initMyApi.getProfileResponse("Bearer "+mytoken);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if(response.isSuccessful()) {
                    ProfileResponse result = response.body();
                    String status = result.getStatus();
                    String name = result.getName();
                    String dateOfBirth = result.getDateOfBirth();
                    String abode = result.getAbode();
                    String profilePhoto = result.getProfilePhoto();

                    name0.setText(name);
                    year0.setText(dateOfBirth);
                    house0.setText(abode);
                    Glide.with(getApplication()).load(profilePhoto).into(person0);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
            }
        });
    }
    private void putUpdate() {

        complete = findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userYear = update_year_y.getSelectedItem().toString()+"-"+update_year_m.getSelectedItem().toString()+"-"+update_year_d.getSelectedItem().toString();
                userAbode = update_house.getSelectedItem().toString();
                if (update_name.getText().toString().replace(" ", "").equals("")) {
                    userName = name0.getText().toString().trim();
                } else {
                    userName = update_name.getText().toString().trim();
                }

                UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(userName, userYear, userAbode);

                sharedPreferences1 = getSharedPreferences("email", MODE_PRIVATE);
                String mytoken = sharedPreferences1.getString("token", "");

                RetrofitClient retrofitClient = RetrofitClient.getInstance();
                initMyApi = RetrofitClient.getRetrofitInterface();

                Call<UpdateProfileResponse> call = initMyApi.getUpdateProfileResponse(updateProfileRequest, "Bearer "+mytoken);
                call.enqueue(new Callback<UpdateProfileResponse>() {
                    @Override
                    public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                        if (response.isSuccessful()) {
                            UpdateProfileResponse result = response.body();
                            String status = result.getStatus();
                            Log.d("성공", "성공");

                            getProfile();
                            name0.setVisibility(v.VISIBLE);
                            update_name.setVisibility(v.GONE);
                            year0.setVisibility(v.VISIBLE);
                            update_year_y.setVisibility(v.GONE);
                            update_year_m.setVisibility(v.GONE);
                            update_year_d.setVisibility(v.GONE);
                            house0.setVisibility(v.VISIBLE);
                            update_house.setVisibility(v.GONE);

                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                        Log.d("실패","실패");
                    }
                });
            }
        });
    }
}