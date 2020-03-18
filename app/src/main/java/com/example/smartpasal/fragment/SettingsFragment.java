package com.example.smartpasal.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;

import com.example.smartpasal.MyFirebaseMessagingService;
import com.example.smartpasal.R;
import com.google.firebase.messaging.FirebaseMessagingService;

public class SettingsFragment extends PreferenceFragmentCompat {
   SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {

        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        listener=new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {


                if (s.equals("notifications")){


                    Preference notiPref=findPreference(s);
                    notiPref.setDefaultValue(sharedPreferences.getBoolean(s,true));
                    if (sharedPreferences.getBoolean(s,true)){
                        Toast.makeText(getContext(),"Enabled message notifications",Toast.LENGTH_SHORT).show();
                        notiPref.setSummary("Message notifications is on");
                        notiPref.setIcon(R.drawable.ic_notifications_24px);
                         MyFirebaseMessagingService.subscribeTopic("general");
                    }
                    else {
                        Toast.makeText(getContext(), "Disabled message notifications", Toast.LENGTH_SHORT).show();
                        notiPref.setIcon(R.drawable.ic_notifications_off_24px);
                        notiPref.setSummary("Message notifications is off");
                        MyFirebaseMessagingService.unsubscribeAllTopics();

                    }



                }

                sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
            }
        };







    }
}
