package com.example.smartpasal.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.databinding.FragmentProfileBinding;
import com.example.smartpasal.model.User;
import com.example.smartpasal.Session.Session;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    private FragmentProfileBinding binding;
    private Session session;


    public Profile() {
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
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();

        getUserDetails(session.getusername());
        return v;
    }

    private void getUserDetails(String userName) {
        SmartAPI.getApiService().getUserDetails(session.getJWT())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(user -> {
                    String user_name = user.getUserName();
                    String delivery_address = user.getDeliveryAddress();
                    String phone = user.getPhone();
                    String role = user.getRole();
                    String age = user.getAge();
                    String gender = user.getGender();
                    binding.tvUserName.setText(user_name);
                    binding.tvAddress.setText(delivery_address);
                    binding.tvPhone.setText(phone);
                    binding.tvAccountType.setText(role);
                    binding.tvAge.setText(age);
                    binding.tvGender.setText(gender);
                    binding.buEdit.setOnClickListener(view -> {
                        Bundle args = new Bundle();
                        fragment_edit_profile fragmentEditProfile = new fragment_edit_profile();
                        User user1 = new User(userName, delivery_address, phone);
                        args.putParcelable("userObj", user1);
                        fragmentEditProfile.setArguments(args);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, fragmentEditProfile)
                                .commit();

                    });
                });


    }


}
