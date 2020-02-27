package com.example.smartpasal;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_edit_profile extends Fragment {
    ImageView ivCancel;
    EditText etEmail;
    EditText etPhone;
    Button buSave;
    Bundle b;
    SharedPreferences sp;



    public fragment_edit_profile() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fragment_edit_profile, container, false);
        sp=getActivity().getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);
         ivCancel=v.findViewById(R.id.ivCancel);
        buSave=v.findViewById(R.id.buSave);
        etEmail=v.findViewById(R.id.etEmail);
        etPhone=v.findViewById(R.id.etPhone);
         b=getArguments();
     if (b!=null) {

    etEmail.setText(b.getString("email"));
    etPhone.setText(b.getString("phone"));
}

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Profile(), "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
buSave.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String email=etEmail.getText().toString();
        String phone=etPhone.getText().toString();

        if (!email.isEmpty()&&!phone.isEmpty()){

        if (b.getString("email").equals(etEmail.getText().toString())&& b.getString("phone").equals(etPhone.getText().toString()) )
        {
            Toast.makeText(getContext(),"you have made no changes",Toast.LENGTH_LONG).show();
        }
        else {
            String url = "http://idealytik.com/SmartPasalWebServices/EditProfile.php?id=" + sp.getString("id", "") + "&email=" + etEmail.getText().toString() + "&phone=" + etPhone.getText().toString();
        }
    }
    else
        Toast.makeText(getContext(),"Fields cant be empty",Toast.LENGTH_LONG).show();
    }
});


        return v;
    }
    // get news from server
    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works

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
                urlConnection.setRequestProperty("http.keepAlive", "false");
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

            }catch (Exception ex){
                Log.d("Internet error",ex.getMessage());
            }
            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try{
                JSONObject json= new JSONObject(progress[0]);
                //display response data

                if (json.getString("msg").equals("Profile updated successfully")) {

                     Toast.makeText(getContext(),"Profile updated successfully",Toast.LENGTH_SHORT).show();
                    MaterialAlertDialogBuilder alert=new MaterialAlertDialogBuilder(getContext(),R.style.AlertDialog);
                    alert.setMessage("A confirmation email has been sent to your email..Please verify before you are able to purchase any product").setTitle("Account Verification").setPositiveButton("Continue Shopping", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new Profile(), "findThisFragment")
                                    .addToBackStack(null)
                                    .commit();




                        }
                    }).show();




                }
                if (json.getString("msg").equals("Email is already taken")){
                    Toast.makeText(getContext(),"Email is already taken",Toast.LENGTH_SHORT).show();


                }

            }


            catch (Exception ex) {
                Log.d("er", ex.getMessage());
            }

        }


        protected void onPostExecute(String  result2){


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
}
