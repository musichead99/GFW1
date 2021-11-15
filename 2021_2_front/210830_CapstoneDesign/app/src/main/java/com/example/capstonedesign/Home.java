package com.example.capstonedesign;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.capstonedesign.home_fragments.FA;
import com.example.capstonedesign.home_fragments.FC;
import com.example.capstonedesign.home_fragments.FL;
import com.example.capstonedesign.home_fragments.FS;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {
    FA fa;
    FC fc;
    FL fl;
    FS fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fa = new FA();
        fc = new FC();
        fl = new FL();
        fs = new FS();

        // Intent를 확인해서 다른 Fragment로 넘어가야하는 경우 이를 수행해주는 함수.
        checkIntent();

        // Home 입장시 첫 화면 배치
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fs).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_menu);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.first_bottm_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fs).commit();
                        return true;
                    case R.id.second_bottm_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fl).commit();
                        return true;
                    case R.id.third_bottm_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fa).commit();
                        return true;
                    case R.id.fourth_bottm_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fc).commit();
                        return true;
                }
                return false;
            }
        });
    }

    public void goSetting(View view) {
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);

        finish();
    }
    public void checkIntent(){
        Intent intent = getIntent();
        if(intent == null) return;
        else{
            // Step 1. Checking the 'IntentType' value
            switch (intent.getIntExtra("IntentType",-1)){
                case 0:
                    String email = intent.getStringExtra("CompareFriend");
                    String name = intent.getStringExtra("FriendName");
                    Bundle bundle = new Bundle();
                    bundle.putString("CompareFriend",email);
                    bundle.putString("FriendName",name);
                    fa.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fa).commit();
                    break;
                default :
                    break;
            }
        }
    }
}