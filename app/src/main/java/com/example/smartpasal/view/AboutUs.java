package com.example.smartpasal.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.smartpasal.R;
import com.example.smartpasal.databinding.ActivityAboutUsBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AboutUs extends AppCompatActivity {

    private ActivityAboutUsBinding binding;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.fragment_fade_enter, R.anim.slide_out_right);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAboutUsBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        getSupportActionBar().setTitle("About Us");


    final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(this,R.style.AlertDialog).setView(R.layout.layout_contact);

        binding.tvContact.setOnClickListener(view1 -> builder.show());

    }
}
