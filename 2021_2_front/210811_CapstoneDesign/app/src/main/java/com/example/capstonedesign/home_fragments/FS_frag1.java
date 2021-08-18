package com.example.capstonedesign.home_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.capstonedesign.Home;
import com.example.capstonedesign.R;

import java.util.ArrayList;

public class FS_frag1 extends Fragment {
    //My 메뉴를 위한 ViewPager2
    ViewPager2 pager;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fs_frag1,container,false);

        // 이 Fragment는 optionMenu를 가지고 있음을 알림.
        setHasOptionsMenu(true);

        pager = rootView.findViewById(R.id.fs_pager);
        pager.setOffscreenPageLimit(3);// 우선 테스트를 위해 3으로 생성.

        MyPagerAdapter adapter = new MyPagerAdapter(getActivity());

        FS_pager_frag1 page1 = new FS_pager_frag1();
        adapter.addItem(page1);

        FS_pager_frag2 page2 = new FS_pager_frag2();
        adapter.addItem(page2);

        FS_pager_frag3 page3 = new FS_pager_frag3();
        adapter.addItem(page3);

        pager.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fs_frag1_menu, menu);
        menu.clear();
        Log.d("onCreateOptionMenu","호출됨");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 기능을 추가해 줘야함.
        // 선택시 선택형 박스창이 나와서 선택하고 그대로 메뉴를 추가해주는 형식.
        Toast.makeText(getActivity(),"옵션 선택됨",Toast.LENGTH_LONG);
        return super.onOptionsItemSelected(item);
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
