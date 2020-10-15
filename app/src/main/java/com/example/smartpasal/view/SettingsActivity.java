package com.example.smartpasal.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.example.smartpasal.R;
import com.example.smartpasal.databinding.ActivitySettingsBinding;
import com.example.smartpasal.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        Toolbar toolbar = findViewById(R.id.normal_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment()).commit();


    }
}
