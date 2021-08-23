package com.example.capstonedesign;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capstonedesign.home_fragments.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Email extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button check = findViewById(R.id.check);
        EditText email = findViewById(R.id.email);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = email.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success)
                            {
                                String userID = jsonObject.getString("userID");

                                Intent intent = new Intent(Email.this, Password.class);
                                intent.putExtra("userID", userID);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "다시 입력해주세요", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                EmailRequest emailRequest = new EmailRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Email.this);
                queue.add(emailRequest);
            }
        });
    }
}