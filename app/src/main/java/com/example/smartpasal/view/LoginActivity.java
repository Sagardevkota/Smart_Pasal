package com.example.smartpasal.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.JwtResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.model.User;
import com.github.florent37.shapeofview.shapes.BubbleView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    SharedPreferences sp;
    Context Context;
    SignInButton Google_sign_in;
    GoogleApiClient googleApiClient;
    public static int Req_code=1000;
    TextInputLayout etEmail;
    TextInputLayout etPassword;
    String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState==null)
            overridePendingTransition(R.anim.left2right,R.anim.right2left);






        sp=getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);




        checkIfLoggedIn();




    }

    public void regClick(View view){

        Intent intent=new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void buLogin(View view){
         etEmail= findViewById(R.id.etEmail);
        final String email=etEmail.getEditText().getText().toString().trim();
          etPassword= findViewById(R.id.etPassword);
        final String password=etPassword.getEditText().getText().toString().trim();;
        if ((email.length()> 0)
                && (password.length()> 0))
        {
          doLogin(email,password);






        }

        else
        {  Toasty.error(this, "You did not enter email or password").show();}

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
                    Log.d("responseBody",response.body().getMessage());
                    if (status.equalsIgnoreCase("200 OK")&&message.equalsIgnoreCase("login successfull"))
                    {
                        Toasty.success(getApplicationContext(),message).show();
                        String jwt=response.body().getJwt();
                        sp.edit().putString("jwt",jwt).apply();
                        hideProgressDialog();
                        goToHomeActivity();

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("connection failed",connectionResult.getErrorMessage());

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


    public void goToHomeActivity(){
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(i);
    }
    public void checkIfLoggedIn(){
        if (sp.contains("jwt")){
            goToHomeActivity();
        }



    }



    }



