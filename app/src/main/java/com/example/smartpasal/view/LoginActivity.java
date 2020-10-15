package com.example.smartpasal.view;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.JsonResponse;
import com.example.smartpasal.SmartAPI.JwtResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.databinding.ActivityLoginBinding;
import com.example.smartpasal.model.User;
import com.example.smartpasal.Session.Session;
import com.example.smartpasal.repository.UserRepository;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.Base64;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity  {



    ActivityLoginBinding binding;
    private Session session;
    private UserRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        session=new Session(LoginActivity.this);
        repository=new UserRepository(session,LoginActivity.this);



    }

    public void regClick(View view){
        Intent intent=new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void buLogin(View view){
        final String email=binding.etEmail.getEditText().getText().toString().trim();
        final String password=binding.etPassword.getEditText().getText().toString().trim();;
        if ((email.length()> 0)
                && (password.length()> 0))
          doLogin(email,password);
        else
          Toasty.error(this, "You did not enter email or password").show();

    }

    private void doLogin(String email, String password) {
        showProgressDialog("Logging in");
        Call<JwtResponse> login=SmartAPI.getApiService().login(new User(email,password));
        login.enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                if (!response.isSuccessful())
                    Log.d("response",response.message());
                else{
                    String status=response.body().getStatus();
                    String message=response.body().getMessage();
                    String role=response.body().getRole();
                    Log.d("responseBody",response.body().getMessage());
                    if (status.equalsIgnoreCase("200 OK")&&message.equalsIgnoreCase("login successfull"))
                    {
                        if (role.equalsIgnoreCase("USER")){
                            Toasty.success(getApplicationContext(),message).show();
                            String token=response.body().getJwt();
                            session.setToken(token);
                            String jwt="Bearer "+response.body().getJwt();
                            session.setJWT(jwt);
                            getUserDetails( getUserName(jwt));
                            hideProgressDialog();

                        }
                        else
                        {
                            Toasty.error(getApplicationContext(),"Please login via user id not seller id!!!").show();
                            hideProgressDialog();
                        }


                    }

                    else{
                        Toasty.error(getApplicationContext(),message).show();
                        hideProgressDialog();
                    }

                }
            }

            @Override
            public void onFailure(Call<JwtResponse> call, Throwable t) {
                Log.d("JwtResponse",t.getMessage());

            }
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

        MaterialAlertDialogBuilder alert=new MaterialAlertDialogBuilder(this,R.style.AlertDialog);
        alert.setMessage("Are you sure you want to exit?").setTitle("Confirmation").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();


    }






    public void goToHomeActivity() {
        hideProgressDialog();

        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(i);
    }

    public void getUserDetails(String userName) {
        Call<User> getUserId = SmartAPI.getApiService().getUserDetails(session.getJWT(), userName);
        getUserId.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    session.setUserId(response.body().getId());
                    session.setAddress(response.body().getDeliveryAddress());

                    getBadgeCount();


                }


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("error", t.getMessage());

            }
        });




    }

    private void getBadgeCount() {
        SmartAPI.getApiService().getBadgeCount(session.getJWT(),session.getUserId())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(jsonResponse -> {
            if (jsonResponse.getStatus().equalsIgnoreCase("200 OK"))
            {
                int message= Integer.parseInt(jsonResponse.getMessage());
                if (message>0)
                    session.setBadgeCount(message);
                goToHomeActivity();
            }

            else
                Toasty.error(getApplicationContext(),jsonResponse.getMessage()).show();


        },throwable -> {});
        ;

    }

    private String getUserName(String jwt){
        String token = jwt;
        String[] split_string = token.split("\\.");
        String payload = split_string[1];
        String body="";

        if (Build.VERSION.SDK_INT >= 26) {
            body = new String(Base64.getDecoder().decode(payload));
        } else {
            body = new String(android.util.Base64.decode(payload, android.util.Base64.DEFAULT));
        }
        HashMap<String, String> map = new Gson().fromJson(body, new TypeToken<HashMap<String, String>>() {
        }.getType());
        session.setUsername(map.get("sub"));

        return map.get("sub");

    }




}



