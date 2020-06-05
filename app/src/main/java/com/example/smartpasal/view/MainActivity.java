package com.example.smartpasal.view;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartpasal.service.MyFirebaseMessagingService;
import com.example.smartpasal.R;

public class MainActivity extends AppCompatActivity {
    Animation topAnim,bottomAnim;
    ImageView image;
    TextView logo;
    TextView slogan;
    public  static String Smart_api_key="YHAXZODHTH8E6V5ARVWVSRFNTVAPR7FD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MyFirebaseMessagingService();
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        image=findViewById(R.id.robot);
        logo=findViewById(R.id.logo);
        slogan=findViewById(R.id.slogan);
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.transition);
        mp.start();
        new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

            @Override

            public void run() {

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                overridePendingTransition(R.anim.left2right,R.anim.right2left);
                startActivity(i);

                finish();

            }

        }, 3*1000); // wait for 3 seconds
    }




}
