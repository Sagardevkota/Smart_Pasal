package com.example.smartpasal.view;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Address;

import android.location.Geocoder;
import android.location.Location;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.JsonResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.databinding.ActivityRegisterBinding;
import com.example.smartpasal.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;

    private static final String TAG = "REGISTER_ACTIVITY";

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    FusedLocationProviderClient fusedLocationClient;
    Location mylocation=new Location("");
    String addregex = ("^([\\d-]{0,}[\\s-]{0,}[\\d/]+)[\\s]{0,}");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

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
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.getLocation.setOnClickListener(view1 -> checkPermission());

    }


    private boolean validateEmail() {
        String emailInput = binding.etEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            binding.etEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            binding.etEmail.setError("Please enter a valid email address");
            return false;
        } else {
            binding.etEmail.setError(null);
            return true;
        }
    }

    private boolean validateDelivery() {
        String delivery = binding.etDelivery.getEditText().getText().toString().trim();

        if (delivery.isEmpty()) {
            binding.etDelivery.setError("Field can't be empty");
            return false;
        }



        else {
            binding.etDelivery.setError(null);
            return true;
        }
    }


    private boolean validatePassword1() {
        String passwordInput1 = binding.etPassword.getEditText().getText().toString().trim();

        if (passwordInput1.isEmpty()) {
            binding.etPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput1).matches()) {
            binding.etPassword.setError("Password too weak");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput1).matches()) {
            binding.etPassword.setError("Password too weak");
            return false;
        } else {
            binding.etPassword.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String phone = binding.etPhone.getEditText().getText().toString().trim();

        if (phone.isEmpty()) {
            binding.etPhone.setError("Field can't be empty");
            return false;
        } else if (phone.length() != 10) {
            binding.etPhone.setError("Please enter valid phone number");
            return false;
        } else {
            binding.etPhone.setError(null);
            return true;
        }
    }

    private boolean validatePassword2() {
        String passwordInput2 = binding.etPassword2.getEditText().getText().toString().trim();

        if (passwordInput2.isEmpty()) {
            binding.etPassword2.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput2).matches()) {
            binding.etPassword2.setError("Password too weak");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput2).matches()) {
            binding.etPassword2.setError("Password too weak");
            return false;
        } else {


            binding.etPassword2.setError(null);
            return true;
        }
    }

    private boolean confirmPassword() {
        String passwordInput1 = binding.etPassword.getEditText().getText().toString().trim();
        String passwordInput2 = binding.etPassword2.getEditText().getText().toString().trim();
        if (!passwordInput1.equals(passwordInput2)) {
            binding.etPassword2.setError("Password donot match");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge(){
        String age=binding.etAge.getEditText().getText().toString().trim();
        if (age.length()==0)
        {
            binding.etAge.setError("Cant be empty");
            return false;
        }

            else
        {
            binding.etAge.setError(null);
            return true;
        }

    }

    public void buJoin(View view) {


        validateEmail();
        validatePassword1();
        validatePassword2();
        validatePhone();
        validateAge();

        confirmPassword();


        if (!validateEmail() | !validatePassword1() | !validatePassword2() | !confirmPassword() | !validatePhone() | !validateDelivery() |!validateAge() ) {
        } else {
            uploadData();

        }

    }

    private void uploadData() {
        showProgressDialog("Registering");

        final String email = binding.etEmail.getEditText().getText().toString().trim();
        final String password = binding.etPassword.getEditText().getText().toString().trim();
        final String phone = binding.etPhone.getEditText().getText().toString().trim();
        final String delivery = binding.etDelivery.getEditText().getText().toString().trim();
        final String age=binding.etAge.getEditText().getText().toString().trim();
        String gender="";
        if (binding.rbFemale.isChecked())
            gender="female";
       else if (binding.rbMale.isChecked())
            gender="male";
       else
            gender="other";


       LatLng location;
       double latitude;
       double longitude;


        if (mylocation.getLatitude()==0)
        {
            location= getLocationFromAddress(binding.etDelivery.getEditText().getText().toString());
             latitude=location.latitude;
            longitude=location.longitude;
        }


        else{
            latitude=mylocation.getLatitude();
            longitude=mylocation.getLongitude();

        }
        String role="USER";

        User user = new User(email, password, delivery, phone,role,age,gender,latitude,longitude);

        SmartAPI.getApiService().register(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    String status = response.getStatus();
                    String message = response.getMessage();
                    Log.i(TAG, "uploadData: "+response.getMessage());

                    if (message.equalsIgnoreCase("Registered Successfully") && status.equalsIgnoreCase("200 OK")) {
                        Toasty.success(getApplicationContext(), message).show();
                        hideProgressDialog();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else
                        Toasty.error(getApplicationContext(), message).show();
                    hideProgressDialog();
                }, throwable -> {
                    Log.e(TAG, "uploadData: "+throwable.getMessage());
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
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    public void loginclick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(R.anim.left2right, R.anim.right2left);
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                    Toast.makeText(getApplicationContext(), "Access permitted", Toast.LENGTH_SHORT).show();

                } else {
                    // Permission Denied
                    Toast.makeText(getApplicationContext(), "Access denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {//Can add more as per requirement


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        } else {
            getLocation();
        }
    }

    private void getLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location!=null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                mylocation.setLatitude(latitude);
                mylocation.setLongitude(longitude);
                getCompleteAddressString(latitude, longitude);

            }

            else
                Toasty.error(getApplicationContext(),"Please visit google map and come back").show();



        }).addOnFailureListener(e -> Toasty.error(getApplicationContext(), e.getMessage()).show())

        ;
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
               binding. etDelivery.getEditText().setText(strAdd);
                Toast.makeText(getApplicationContext(),strAdd,Toast.LENGTH_LONG).show();
                Log.d("My Current location", strReturnedAddress.toString());
            } else {
                Log.d("My Current loction ", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            Log.d("My Current loction ", "Canont get Address!");
        }
        return strAdd;
    }

    public LatLng getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1=null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng( (location.getLatitude() ),
                    (location.getLongitude() ));

            return p1;
        }

        catch (Exception e){
            Log.d("latngException",e.getMessage());
        }
        return p1;
    }



}



