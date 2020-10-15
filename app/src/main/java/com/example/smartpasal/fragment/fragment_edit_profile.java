package com.example.smartpasal.fragment;


import android.Manifest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.JsonResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.databinding.FragmentFragmentEditProfileBinding;
import com.example.smartpasal.Session.Session;

import com.example.smartpasal.model.User;
import com.example.smartpasal.view.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_edit_profile extends Fragment {
    private FragmentFragmentEditProfileBinding binding;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    FusedLocationProviderClient fusedLocationClient;
    private Session session;


    public fragment_edit_profile() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        session=new Session(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFragmentEditProfileBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        getPassedValues();

        binding.getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        binding.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Profile(), "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        binding.buSaveEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString();
                if (email.isEmpty())
                    Toasty.error(getContext(), "Field cant be empty").show();
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    Toasty.error(getContext(), "Please enter valid email address").show();
                else if (email.equals(session.getusername()))
                    Toasty.error(getContext(), "No changes made").show();
                else
                    updateEmail(email);


            }
        });
        binding.buSavePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = binding.etPhone.getText().toString();
                if (phone.isEmpty())
                    Toasty.error(getContext(), "Field cant be empty").show();
                else if (phone.length() != 10)
                    Toasty.error(getContext(), "Please enter valid phone number").show();
                else
                    updatePhone(phone);
            }
        });
        binding.buSaveDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String delivery = binding.etDelivery.getText().toString();
                if (!delivery.isEmpty())
                    updateDelivery(delivery);
                else
                    Toasty.error(getContext(), "Field cant be empty").show();

            }
        });


        return v;
    }

    private void getPassedValues() {
        Bundle b=getArguments();
        if (b!=null){
            User user=b.getParcelable("userObj");
            binding.etEmail.setText(user.getUserName());
            binding.etDelivery.setText(user.getDeliveryAddress());
            binding.etPhone.setText(user.getPhone());
        }
    }

    private void updateEmail(String email) {
        MaterialAlertDialogBuilder alertDialogBuilder=new MaterialAlertDialogBuilder(getContext());
        alertDialogBuilder.setTitle("Logout Confirmation")
                .setMessage("You will be logged out after changing your email address")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Call<JsonResponse> updateEmail = SmartAPI.getApiService().updateEmail(session.getJWT(), session.getUserId(), email);
                        updateEmail.enqueue(new Callback<JsonResponse>() {
                            @Override
                            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                                if (!response.isSuccessful())
                                    Log.d("unsuccessful", "unsuccessful");
                                else {
                                    String status = response.body().getStatus();
                                    String message = response.body().getMessage();
                                    if (status.equalsIgnoreCase("200 Ok"))

                                    {
                                        Intent intent=new Intent(getActivity(), LoginActivity.class);
                                        session.destroy();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                        Toasty.success(getContext(), message).show();
                                    }
                                    else
                                        Toasty.error(getContext(), message).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<JsonResponse> call, Throwable t) {

                            }
                        });


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();


    }

    private void updatePhone(String phone) {
        Call<JsonResponse> updatePhone = SmartAPI.getApiService().updatePhone(session.getJWT(), session.getUserId(), phone);
        updatePhone.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (!response.isSuccessful())
                    Log.d("unsuccessful", "unsuccessful");
                else {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("200 Ok"))
                        Toasty.success(getContext(), message).show();
                    else
                        Toasty.error(getContext(), message).show();
                }

            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });


    }

    private void updateDelivery(String delivery) {
        Call<JsonResponse> updateDelivery = SmartAPI.getApiService().updateDelivery(session.getJWT(), session.getUserId(), delivery);
        updateDelivery.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (!response.isSuccessful())
                    Log.d("unsuccessful", "unsuccessful");
                else {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("200 Ok"))
                        Toasty.success(getContext(), message).show();
                    else
                        Toasty.error(getContext(), message).show();
                }

            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                    Toast.makeText(getContext(), "Access permitted", Toast.LENGTH_SHORT).show();

                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "Access denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {//Can add more as per requirement


            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        } else {
            getLocation();
        }
    }

    private void getLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());


        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Double latitude = location.getLatitude();
                    Double longitude = location.getLongitude();
                    getCompleteAddressString(latitude, longitude);
                }

            }
        });
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
               binding.etDelivery.setText(strAdd);
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
