package com.example.smartpasal.view;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.JsonResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout etPassword1;
    TextInputLayout etPassword2;

    TextInputLayout etEmail;
    TextInputLayout etDelivery;
    TextInputLayout etPhone;


    TextView getLocation;
    SharedPreferences sp;
    final private  int REQUEST_CODE_ASK_PERMISSIONS=123;
    FusedLocationProviderClient fusedLocationClient;
    Pattern addregex = Pattern.compile("^([\\d-]{0,}[\\s-]{0,}[\\d/]+)[\\s]{0,}");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etEmail =  findViewById(R.id.etEmail);
        etPassword1 =  findViewById(R.id.etPassword);
        etPassword2 =  findViewById(R.id.etPassword2);
        etPhone =  findViewById(R.id.etPhone);
        etDelivery=findViewById(R.id.etDelivery);
        getLocation=findViewById(R.id.getLocation);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });



        sp=getSharedPreferences("s-martlogin",Context.MODE_PRIVATE);



    }

    private boolean validateEmail() {
        String emailInput = etEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            etEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etEmail.setError("Please enter a valid email address");
            return false;
        } else {
            etEmail.setError(null);
            return true;
        }
    }

    private boolean validateDelivery() {
        String emailInput = etEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            etDelivery.setError("Field can't be empty");
            return false;
        }

        else {
            etDelivery.setError(null);
            return true;
        }
    }



    private boolean validatePassword1() {
        String passwordInput1 = etPassword1.getEditText().getText().toString().trim();

        if (passwordInput1.isEmpty()) {
            etPassword1.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput1).matches()) {
            etPassword1.setError("Password too weak");
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(passwordInput1).matches()) {
            etPassword1.setError("Password too weak");
            return false;
        }



        else {
            etPassword1.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String phone = etPhone.getEditText().getText().toString().trim();

        if (phone.isEmpty()) {
            etPhone.setError("Field can't be empty");
            return false;
        }
        else if (phone.length()!=10)
        {
            etPhone.setError("Please enter valid phone number");
            return false;
        }



        else {
            etPhone.setError(null);
            return true;
        }
    }

    private boolean validatePassword2() {
        String passwordInput2 = etPassword2.getEditText().getText().toString().trim();

        if (passwordInput2.isEmpty()) {
            etPassword2.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput2).matches()) {
            etPassword2.setError("Password too weak");
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(passwordInput2).matches()) {
            etPassword2.setError("Password too weak");
            return false;
        }

        else {


            etPassword2.setError(null);
            return true;
        }
    }

    private boolean confirmPassword(){
        String passwordInput1 = etPassword1.getEditText().getText().toString().trim();
        String passwordInput2 = etPassword2.getEditText().getText().toString().trim();
        if (!passwordInput1.equals(passwordInput2)){
            etPassword2.setError("Password donot match");
            return false;}
        else
        {
            return true;
        }
    }
    public void buJoin(View view) {


        validateEmail();
        validatePassword1();
        validatePassword2();
        validatePhone();

        confirmPassword();



        if ( !validateEmail()  | !validatePassword1() | !validatePassword2() | !confirmPassword()| !validatePhone()|!validateDelivery() )
        {
        }

        else {
uploadData();

        }

    }
    private void uploadData() {

        final  String  email = etEmail.getEditText().getText().toString().trim();
        final  String  password = etPassword1.getEditText().getText().toString().trim();
        final  String  phone = etPhone.getEditText().getText().toString().trim();
        final String delivery=etDelivery.getEditText().getText().toString().trim();


        User user=new User(email,password,delivery,phone,false);
        Call<JsonResponse> register=SmartAPI.getApiService().register(user);
        register.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (!response.isSuccessful()){
                    Log.d("response",response.body().toString());
                }
                String status=response.body().getStatus();
                String message=response.body().getMessage();
                Log.d("response",response.body().getMessage());


                if (message.equalsIgnoreCase("Registered Successfully")&&status.equalsIgnoreCase("200 OK")) {
                    Toasty.success(getApplicationContext(),message).show();
                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                    Toasty.error(getApplicationContext(),message).show();
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Log.d("error in response",t.getMessage().toString());

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


    public void loginclick(View view) {
        Intent intent=new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(R.anim.left2right,R.anim.right2left);

        startActivity(intent);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                    Toast.makeText(getApplicationContext(),"Access permitted",Toast.LENGTH_SHORT).show();

                } else {
                    // Permission Denied
                    Toast.makeText( getApplicationContext(),"Access denied" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){//Can add more as per requirement


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
        else
        {
            getLocation();
        }
    }

    private void getLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Double latitude=location.getLatitude();
                Double longitude=location.getLongitude();
                getCompleteAddressString(latitude,longitude);
            }
        });
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
                etDelivery.getEditText().setText(strAdd);
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
}



