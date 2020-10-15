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
    public interface IOnBackPressed {

        /**
         * If you return true the back press will not be taken into account, otherwise the activity will act naturally
         * @return true if your processing has priority if not false
         */
        boolean onBackPressed();
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
        binding=FragmentProfileBinding.inflate(getLayoutInflater());
        View v=binding.getRoot();

        getUserDetails(session.getusername());
        return v;
    }

    private void getUserDetails(String userName) {
        Call<User> userCall= SmartAPI.getApiService().getUserDetails(session.getJWT(),userName);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful())
                    Log.d("unsuccess","unsuccess");
                    else{
                        String user_name=response.body().getUserName();
                        String delivery_address=response.body().getDeliveryAddress();
                        String phone=response.body().getPhone();
                       String role=response.body().getRole();
                       String age=response.body().getAge();
                       String gender=response.body().getGender();
                        binding.tvUserName.setText(user_name);
                        binding.tvAddress.setText(delivery_address);
                        binding.tvPhone.setText(phone);
                      binding.tvAccountType.setText(role);
                      binding.tvAge.setText(age);
                      binding.tvGender.setText(gender);
                        binding.buEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle args=new Bundle();
                                fragment_edit_profile fragmentEditProfile=new fragment_edit_profile();
                                User user=new User(userName,delivery_address,phone);
                                args.putParcelable("userObj",user);
                                fragmentEditProfile.setArguments(args);

                               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragmentEditProfile).commit();

                            }
                        });

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }


    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext(),R.style.AlertDialog);
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


}
