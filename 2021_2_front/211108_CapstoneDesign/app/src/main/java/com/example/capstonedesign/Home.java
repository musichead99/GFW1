package com.example.capstonedesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        fa = new FA();
        fc = new FC();
        fl = new FL();
        fs = new FS();

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
}