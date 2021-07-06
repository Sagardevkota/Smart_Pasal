package com.example.smartpasal.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.JsonResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.model.Feedback;
import com.example.smartpasal.service.MyFirebaseMessagingService;
import com.example.smartpasal.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SETTINGS_FRAGMENT";
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private Session session;

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
        session = new Session(getContext());


        listener = (sharedPreferences, s) -> {


            if (s.equals("notifications")) {
                Preference notiPref = findPreference(s);
                notiPref.setDefaultValue(sharedPreferences.getBoolean(s, true));
                if (sharedPreferences.getBoolean(s, true)) {
                    Toast.makeText(getContext(), "Enabled message notifications", Toast.LENGTH_SHORT).show();
                    notiPref.setSummary("Message notifications is on");
                    notiPref.setIcon(R.drawable.ic_notifications_24px);
                    MyFirebaseMessagingService.subscribeTopic("general");
                } else {
                    Toast.makeText(getContext(), "Disabled message notifications", Toast.LENGTH_SHORT).show();
                    notiPref.setIcon(R.drawable.ic_notifications_off_24px);
                    notiPref.setSummary("Message notifications is off");
                    MyFirebaseMessagingService.unsubscribeAllTopics();
                }

            }

            sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        };


        Preference myPref = findPreference("feedback");
        myPref.setOnPreferenceClickListener(preference -> {
            showFeedbackDialog();
            return true;
        });


    }

    private void showFeedbackDialog() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.feedback_dialog, null);
        TextInputEditText etSubject = dialogView.findViewById(R.id.etSubject);
        TextInputEditText etMessage = dialogView.findViewById(R.id.etMessage);


        alertDialogBuilder.setView(dialogView)
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Send", (dialogInterface, i) -> {
                    String subject = etSubject.getText().toString().trim();
                    String message = etMessage.getText().toString().trim();
                    sendFeedback(subject, message);

                })
                .create()
                .show();
    }

    private void sendFeedback(String subject, String message) {
        Feedback feedback = new Feedback(session.getUserId(), subject, message);
        SmartAPI.getApiService().addFeedback(session.getJWT(), feedback)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> Toasty.success(getContext(), response.getMessage()).show(),
                        throwable -> Log.e(TAG, "sendFeedback: " + throwable.getMessage()));

    }
}
