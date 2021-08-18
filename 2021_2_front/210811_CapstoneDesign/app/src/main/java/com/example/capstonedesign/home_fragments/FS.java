package com.example.capstonedesign.home_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.capstonedesign.R;

public class FS extends Fragment {
    FS_frag1 fs_frag1;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        fs_frag1 = new FS_frag1();

        getChildFragmentManager().beginTransaction().replace(R.id.container,fs_frag1).commit();

        return inflater.inflate(R.layout.fs,container,false);
    }
}