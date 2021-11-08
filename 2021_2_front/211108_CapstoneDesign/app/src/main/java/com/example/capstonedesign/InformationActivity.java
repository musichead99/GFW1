package com.example.capstonedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class InformationActivity extends AppCompatActivity {
    PackageInfo pInfo=null;
    TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        version = findViewById(R.id.version);
        version.setText(getVersionInfo(InformationActivity.this));
    }

    public String getVersionInfo(Context context) {
        String version=null;
        try{
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = i.versionName;
        } catch(PackageManager.NameNotFoundException e) {

        }
        return version;
    }
}