package com.example.capstonedesign;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

public class Setting extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey);
    }
}
