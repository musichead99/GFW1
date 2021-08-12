package com.example.capstonedesign.home_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.capstonedesign.R;
import com.google.android.material.tabs.TabLayout;

public class FA extends Fragment {
    // FA : Fragment analysis tab이란 의미.
    FA_frag1 fa_frag1;
    FA_frag2 fa_frag2;
    FA_frag3 fa_frag3;
    FA_frag4 fa_frag4;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fa,container,false);

        fa_frag1 = new FA_frag1();
        fa_frag2 = new FA_frag2();
        fa_frag3 = new FA_frag3();
        fa_frag4 = new FA_frag4();

        getChildFragmentManager().beginTransaction().replace(R.id.container,fa_frag1).commit();

        TabLayout tabs = rootView.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("1일"));
        tabs.addTab(tabs.newTab().setText("1주일"));
        tabs.addTab(tabs.newTab().setText("1개월"));
        tabs.addTab(tabs.newTab().setText("6개월"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if(position == 0){
                    getChildFragmentManager().beginTransaction().replace(R.id.container,fa_frag1).commit();
                } else if(position == 1) {
                    getChildFragmentManager().beginTransaction().replace(R.id.container,fa_frag2).commit();
                } else if(position == 2) {
                    getChildFragmentManager().beginTransaction().replace(R.id.container,fa_frag3).commit();
                } else if(position == 3) {
                    getChildFragmentManager().beginTransaction().replace(R.id.container,fa_frag4).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }
}
