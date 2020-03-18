package com.example.smartpasal.fragment;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartpasal.R;
import com.example.smartpasal.view.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;


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
    TextView etDelivery;
   final private  int REQUEST_CODE_ASK_PERMISSIONS=123;
    FusedLocationProviderClient fusedLocationClient;



    public fragment_edit_profile() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fragment_edit_profile, container, false);
        sp=getActivity().getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);
        etDelivery=v.findViewById(R.id.etDelivery);
        Button getLocation=v.findViewById(R.id.getLocation);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
         ivCancel=v.findViewById(R.id.ivCancel);
        buSave=v.findViewById(R.id.buSave);
        etEmail=v.findViewById(R.id.etEmail);
        etPhone=v.findViewById(R.id.etPhone);
        etDelivery=v.findViewById(R.id.etDelivery);
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
        String delivery=etDelivery.getText().toString();

        if (!email.isEmpty()&&!phone.isEmpty()&&!delivery.isEmpty()){


            String url = "http://idealytik.com/SmartPasalWebServices/EditProfile.php?id=" + sp.getString("id", "") + "&email=" + etEmail.getText().toString() + "&phone=" + etPhone.getText().toString()+"&delivery_address="+delivery;
            new MyAsyncTaskgetNews().execute(url);

    }
    else
        Toast.makeText(getContext(),"Fields cant be empty",Toast.LENGTH_LONG).show();
    }
});


        return v;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                    Toast.makeText(getContext(),"Access permitted",Toast.LENGTH_SHORT).show();

                } else {
                    // Permission Denied
                    Toast.makeText( getContext(),"Access denied" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){//Can add more as per requirement


            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
        else
        {
            getLocation();
        }
    }

    private void getLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Double latitude=location.getLatitude();
                Double longitude=location.getLongitude();
                getCompleteAddressString(latitude,longitude);
            }
        });
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

                urlConnection.setRequestProperty("APIKEY", MainActivity.Smart_api_key);


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
                Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
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
                if (json.getString("msg").equals("Couldnt update profile")){
                    Toast.makeText(getContext(),"COuldnt update profile",Toast.LENGTH_SHORT).show();


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

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                etDelivery.setText(strAdd);
                Toast.makeText(getContext(),strAdd,Toast.LENGTH_LONG).show();
                Log.d("My Current location", strReturnedAddress.toString());
            } else {
                Log.d("My Current loction ", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            Log.d("My Current loction ", "Canont get Address!");
        }
        return strAdd;
    }
}
