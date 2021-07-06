package com.example.smartpasal.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpasal.R;
import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.databinding.ActivityLoginBinding;
import com.example.smartpasal.model.User;
import com.example.smartpasal.repository.UserRepository;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LOGIN_ACTIVITY";
    private ActivityLoginBinding binding;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        session = new Session(LoginActivity.this);


    }

    public void regClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void buLogin(View view) {
        final String email = binding.etEmail.getEditText().getText().toString().trim();
        final String password = binding.etPassword.getEditText().getText().toString().trim();
        ;
        if ((email.length() > 0)
                && (password.length() > 0))
            doLogin(email, password);
        else
            Toasty.error(this, "You did not enter email or password").show();

    }

    private void doLogin(String email, String password) {
        showProgressDialog("Logging in");
         SmartAPI.getApiService().login(new User(email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    String status = response.getStatus();
                    String message = response.getMessage();
                    String role = response.getRole();
                    String[] roles = role
                            .substring(1,role.length()-1)
                            .split(","); //role can be "[ADMIN,USER]"


                    Log.d("responseBody", response.toString());
                    if (status.equalsIgnoreCase("200 OK") && message.equalsIgnoreCase("login successful")) {
                        if (Arrays.stream(roles)
                                .anyMatch(role1->role1.equalsIgnoreCase("USER"))) {
                            Toasty.success(getApplicationContext(), message).show();
                            String token = response.getJwt();
                            session.setToken(token);
                            String jwt = "Bearer " + response.getJwt();
                            session.setJWT(jwt);
                            goToHomeActivity();

                        } else {
                            Toasty.error(getApplicationContext(), "Please login via user id not seller or admin id!!!").show();
                            hideProgressDialog();
                        }
                    }

                    else {
                        Toasty.error(getApplicationContext(), message).show();
                        hideProgressDialog();
                    }

                }, throwable -> {
                    Log.e(TAG, "doLogin: " + throwable.getMessage());
                    Toasty.error(getApplicationContext(), Objects.requireNonNull(throwable.getMessage())).show();

                    hideProgressDialog();
                });

    }


    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public void onBackPressed() {

        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(this, R.style.AlertDialog);
        alert.setMessage("Are you sure you want to exit?").setTitle("Confirmation").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        }).setNegativeButton("No", (dialog, which) -> {

        }).show();


    }


    public void goToHomeActivity() {
        hideProgressDialog();
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }




}



