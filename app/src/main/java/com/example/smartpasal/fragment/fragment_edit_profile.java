package com.example.smartpasal.fragment;


import android.Manifest;

import android.app.Dialog;
import android.content.Context;
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

import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.databinding.FragmentFragmentEditProfileBinding;
import com.example.smartpasal.Session.Session;

import com.example.smartpasal.model.User;
import com.example.smartpasal.view.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_edit_profile extends Fragment {
    private FragmentFragmentEditProfileBinding binding;
    private static final String TAG = "FRAGMENT_EDIT_PROFILE";
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private FusedLocationProviderClient fusedLocationClient;
    private Session session;
    private User myUser;


    public fragment_edit_profile() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        session = new Session(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFragmentEditProfileBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        getPassedValues();

        binding.getLocation.setOnClickListener((View.OnClickListener) view -> checkPermission());
        binding.buSave.setEnabled(false);

        binding.ivCancel.setOnClickListener((View.OnClickListener) view -> getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new Profile(), "findThisFragment")
                .addToBackStack(null)
                .commit());

        binding.buSave.setOnClickListener(v1 -> updateUserAccount());

        binding.tvChangePassword.setOnClickListener(v12 -> createChangePasswordDialog());
        return v;
    }

    private void createChangePasswordDialog() {
        final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.change_password_dialog);
        dialog.create();
        dialog.show();

        TextView tvCurrentPassword = dialog.findViewById(R.id.etCurrentPassword);
        TextView tvNewPassword = dialog.findViewById(R.id.etNewPassword);
        TextView tvRePassword = dialog.findViewById(R.id.etRePassword);
        Button buSave = dialog.findViewById(R.id.buSave);



        buSave.setOnClickListener(v -> {
            String currentPassword = tvCurrentPassword.getText().toString();
            String newPassword = tvNewPassword.getText().toString();
            String rePassword = tvRePassword.getText().toString();

            if (!validateText(currentPassword) || !validateText(newPassword) ||
                    !validateText(rePassword) || !validateEqualPassword(newPassword, rePassword)) {
            }
            else {
                SmartAPI.getApiService()
                        .updatePassword(session.getJWT(),
                                currentPassword,
                                newPassword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.getStatus().equalsIgnoreCase("200 OK")) {
                                Toasty.success(getContext(), response.getMessage()).show();
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                requireActivity().finish();
                            }
                            else Toasty.error(getContext(),response.getMessage()).show();

                        }, throwable -> Log.e(TAG, "createChangePasswordDialog: " + throwable.getMessage()));

            }
        });


    }

    private boolean validateEqualPassword(String newPassword, String rePassword) {
        if (!newPassword.equalsIgnoreCase(rePassword))
            Toasty.error(getContext(), "Password doesn't match").show();
        else return true;
        return false;
    }

    private boolean validateText(String text) {
        Log.i(TAG, "validateText: "+text);
        if (text.length() < 5)
            Toasty.error(getContext(), "Characters cant be less than 5").show();
        else return true;
        return false;
    }

    private void updateUserAccount() {

        if (!validateEmail() || !validatePhone() || !validatePassword() || !validateDelivery())
            Toasty.error(getContext(), "Please fill up valid info").show();
        else {
            binding.buSave.setEnabled(true);
            String email = binding.etEmail.getText().toString();
            String phone = binding.etPhone.getText().toString();
            String delivery = binding.etDelivery.getText().toString();
            String password = binding.etPassword.getText().toString();

            myUser.setUserName(email);
            myUser.setPassword(password);
            myUser.setPhone(phone);
            myUser.setDeliveryAddress(delivery);

            SmartAPI.getApiService()
                    .updateAccount(session.getJWT(), myUser)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {

                        if (response.getStatus().equalsIgnoreCase("200 Ok")) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            session.destroy();
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Toasty.success(getContext(), response.getMessage()).show();
                        } else
                            Toasty.error(getContext(), response.getMessage()).show();
                    }, throwable -> Log.e(TAG, "updateUserAccount: " + throwable.getMessage()));
        }
    }

    private boolean validateEmail() {
        String email = binding.etEmail.getText().toString();
        if (email.isEmpty())
            Toasty.error(getContext(), "Field cant be empty").show();
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            Toasty.error(getContext(), "Please enter valid email address").show();
        else if (email.equals(session.getusername()))
            Toasty.error(getContext(), "No changes made").show();
        else
            return true;

        return false;

    }

    private boolean validatePassword() {

        String password = binding.etPassword.getText().toString();
        if (password.isEmpty())
            Toasty.error(getContext(), "Field cant be empty").show();
        else if (password.length() < 5)
            Toasty.error(getContext(), "Please enter at least 5 characters").show();
        else
            return true;
        return false;

    }

    private boolean validatePhone() {
        String phone = binding.etPhone.getText().toString();
        if (phone.isEmpty())
            Toasty.error(getContext(), "Field cant be empty").show();
        else if (phone.length() != 10)
            Toasty.error(getContext(), "Please enter valid phone number").show();
        else
            return true;
        return false;

    }

    private boolean validateDelivery() {
        String delivery = binding.etDelivery.getText().toString();
        if (!delivery.isEmpty())
            return true;
        else
            Toasty.error(getContext(), "Field cant be empty").show();
        return false;

    }

    private void getPassedValues() {
        Bundle b = getArguments();
        if (b != null) {
            myUser = b.getParcelable("userObj");
            binding.etEmail.setText(myUser.getUserName());
            binding.etDelivery.setText(myUser.getDeliveryAddress());
            binding.etPhone.setText(myUser.getPhone());
        }
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
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
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
                Toast.makeText(getContext(), strAdd, Toast.LENGTH_LONG).show();
                Log.d("My Current location", strReturnedAddress.toString());
            } else {
                Log.d("My Current loction ", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("My Current loction ", "Canont get Address!");
        }
        return strAdd;
    }


}
