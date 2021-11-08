package com.example.capstonedesign;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class Setting extends PreferenceFragmentCompat {
    AlertDialog.Builder builder;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey);
    }

}
