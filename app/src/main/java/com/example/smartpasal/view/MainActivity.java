package com.example.smartpasal.view;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;



import com.example.smartpasal.databinding.ActivityMainBinding;
import com.example.smartpasal.repository.UserRepository;
import com.example.smartpasal.service.MyFirebaseMessagingService;
import com.example.smartpasal.R;
import com.example.smartpasal.Session.Session;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity {
    Animation topAnim,bottomAnim;
    ActivityMainBinding binding;
    private Session session;
    private UserRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        session=new Session(MainActivity.this);
        repository=new UserRepository(session,MainActivity.this);
        new MyFirebaseMessagingService();
        setAnimations();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfLoggedIn();
            }
            }, 3*1000); // wait for 3 seconds
    }

    private void setAnimations() {
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        binding.logo.setAnimation(bottomAnim);
        binding.slogan.setAnimation(bottomAnim);
        binding.robot.setAnimation(topAnim);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.transition);
        mp.start();
    }

    public void checkIfLoggedIn() {
        if (repository.checkIfLoggedIn()) goToHomeActivity();
      else goToLoginActivity();
    }

    private void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
    public void goToHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(i);
    }

}
