package com.example.smartpasal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    SharedPreferences sp;
    Context Context;
    SignInButton Google_sign_in;
    GoogleApiClient googleApiClient;
    public static int Req_code=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);





        sp=getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);
        checkIfLoggedIn();
       GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
       googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();
       Google_sign_in=findViewById(R.id.btn_google_login);
       Google_sign_in.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Google_sign_in=findViewById(R.id.btn_google_login);
               Toast.makeText(getApplicationContext(),"Login with Google",Toast.LENGTH_LONG).show();
               Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
               startActivityForResult(intent,Req_code);

           }
       });



    }

    public void regClick(View view){

        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void buLogin(View view){
        final EditText etEmail= findViewById(R.id.etEmail);
        final String email=etEmail.getText().toString().trim();
        final EditText   etPassword= findViewById(R.id.etPassword);
        final String password=etPassword.getText().toString().trim();;
        if ((email.length()> 0)
                && (password.length()> 0)) {
            String url = "http://idealytik.com/SmartPasalWebServices/Login.php?email=" + email + "&password=" + password;
                                new MyAsyncTaskgetNews().execute(url); }

        else
        {  Toast.makeText(this, "You did not enter email or password", Toast.LENGTH_SHORT).show();}

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==Req_code){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            String email=account.getEmail();
            String img_url=account.getPhotoUrl().toString();
            String id=account.getId();
            sp.edit().putString("user_photo",img_url).apply();
            RegisterViaGoogle(email,img_url);


        }
    }

    private void RegisterViaGoogle(String email,String img_url) {
        String url="http://idealytik.com/SmartPasalWebServices/RegisterViaGoogle.php?email="+email;
      new  MyAsyncTaskgetNews1().execute(url);

    }

    // get news from server
    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
           showProgressDialog("Logging ");
        }
        @Override
        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds

                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    NewsData = ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);
                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

            }catch (Exception ex){}
            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try{
                JSONObject json= new JSONObject(progress[0]);
                //display response data

                if (json.getString("msg").equals("Valid credentials")) {
                    String msg="Logging you in";

                    JSONArray userInfo=new JSONArray( json.getString("user_info"));
                    JSONObject userCredentials=userInfo.getJSONObject(0);
                    String userID=userCredentials.getString("id");
                    String email=userCredentials.getString("email");
                     SaveUserInfo(userID,email);
                    showProgressDialog(msg);
                    goToHomeActivity();
                } 
                if (json.getString("msg").equals("Email is not registered yet")){
                    Toast.makeText(getApplicationContext(), "Email is not registered yet", Toast.LENGTH_LONG).show();

                }
                else
                    Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_LONG).show();

            }


            catch (Exception ex) {
                Log.d("er", ex.getMessage());
            }

        }


        protected void onPostExecute(String  result2){

hideProgressDialog();
        }




    }

    // this method convert any stream to string
    public static String ConvertInputToStringNoChange(InputStream inputStream) {

        BufferedReader bureader=new BufferedReader( new InputStreamReader(inputStream));
        String line ;
        String linereultcal="";

        try{
            while((line=bureader.readLine())!=null) {

                linereultcal+=line;

            }
            inputStream.close();


        }catch (Exception ex){}

        return linereultcal;
    }



    @Override
    public void onBackPressed() {

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
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

    public void SaveUserInfo(String userID, String email){
        sp.edit().putString("userID",userID).apply();
        sp.edit().putString("email",email).apply();
        sp.edit().putBoolean("logged",true).apply();


    }

    public void goToHomeActivity(){
        Intent i = new Intent(this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
    public void checkIfLoggedIn(){


        if (sp.getBoolean("logged",true)){
            goToHomeActivity();
        }




    }

    // get news from server
    public  class MyAsyncTaskgetNews1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
            showProgressDialog("Logging with google");

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds

                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    NewsData = ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);
                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

            } catch (Exception ex) {
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {

            try {
                JSONObject json = new JSONObject(progress[0]);
                //display response data

                if (json.getString("msg").equals("Successfull")) {


                    JSONArray userInfo = new JSONArray(json.getString("user_info"));
                    JSONObject userCredentials = userInfo.getJSONObject(0);
                    String userID = userCredentials.getString("id");
                    String email = userCredentials.getString("email");
                    SaveUserInfo(userID, email);

                    goToHomeActivity();
                }
            } catch (Exception ex) {
                Log.d("er", ex.getMessage());
            }

        }


        protected void onPostExecute(String result2) {

            hideProgressDialog();
        }


    }


    }
