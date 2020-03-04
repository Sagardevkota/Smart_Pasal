package com.example.smartpasal.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;

import com.example.smartpasal.R;

public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
