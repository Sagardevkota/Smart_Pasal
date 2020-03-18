package com.example.smartpasal.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.smartpasal.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AboutUs extends AppCompatActivity {
    FrameLayout layoutInfo;
    TextView tvContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setTitle("About Us");


    final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(this,R.style.AlertDialog).setView(R.layout.layout_contact);




        tvContact=findViewById(R.id.tvContact);

        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               builder.show();

            }
        });

    }
}
