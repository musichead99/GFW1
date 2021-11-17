package com.example.capstonedesign.home_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.capstonedesign.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FA extends Fragment {
    // FA : Fragment analysis tab이란 의미.
    FA_frag1 fa_frag1;
    FA_frag2 fa_frag2;
    FA_frag3 fa_frag3;
    FA_frag4 fa_frag4;
    // viewPager2 var for fa_viewPager2
    ViewPager2 viewPager2;
    // Indicator for viewPager2
    SpringDotsIndicator springDotsIndicator;
    // Tablayout to connect tablayout with viewPager2
    TabLayout tabLayout;

    // The items for tabLayout
    final List<String> tabItems = Arrays.asList("걸음수","칼로리","거리","이동시간");

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fa,container,false);

        /** findViewById parts**/
        viewPager2 = rootView.findViewById(R.id.fa_viewPager2);
        tabLayout = rootView.findViewById(R.id.tabs);
        // Setting empty space of same height as bottom menu
        rootView.findViewById(R.id.space_bottom_menu).setMinimumHeight(getActivity().findViewById(R.id.bottom_menu).getHeight());

        springDotsIndicator = (SpringDotsIndicator)rootView.findViewById(R.id.spring_dots_indicator);

        // Same num as the actual num of fragments
        viewPager2.setOffscreenPageLimit(4);

        MyPagerAdapter adapter = new MyPagerAdapter(getActivity());

        //Adding fragments into adapter
        fa_frag1 = new FA_frag1();
        adapter.addItem(fa_frag1);

        fa_frag2 = new FA_frag2();
        adapter.addItem(fa_frag2);

        fa_frag3 = new FA_frag3();
        adapter.addItem(fa_frag3);

        fa_frag4 = new FA_frag4();
        adapter.addItem(fa_frag4);

        viewPager2.setAdapter(adapter);
        springDotsIndicator.setViewPager2(viewPager2);

        /** TablayoutMediator part
         *
         * TabLayoutMediator helps cooperating tablayout with viewpager2
         *
         * **/
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) {
                /** 상단탭 추가. **/
                TextView textView = new TextView(rootView.getContext());
                textView.setText(tabItems.get(position));
                textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                tab.setCustomView(textView);
            }
        }).attach();

        return rootView;
    }

    class MyPagerAdapter extends FragmentStateAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public MyPagerAdapter(FragmentActivity fm){
            super(fm);
        }
        public void addItem(Fragment item){
            items.add(item);
        }
        public Fragment createFragment(int position) {
            return items.get(position);
        }
        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
