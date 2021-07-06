package com.example.smartpasal.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.OrderAdapter;
import com.example.smartpasal.databinding.FragmentOrderBinding;
import com.example.smartpasal.model.OrderResponse;
import com.example.smartpasal.Session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class order extends Fragment {
    private FragmentOrderBinding binding;
    private OrderAdapter adapter;
    private final ArrayList<OrderResponse> orderResponseList = new ArrayList<>();
    private Session session;
    private static final String TAG = "ORDER";

    public order() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        session = new Session(context);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        initRecyclerview();
        initSpinner();
        return v;

    }

    private void initSpinner() {
        String[] arr = {"Waiting","Dispatched","Completed","Cancelled"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arr);
        binding.spSort.setAdapter(arrayAdapter);
        binding.spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Waiting to waiting and so on
                getOrders(arr[position].toLowerCase(Locale.ROOT));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getOrders(String status) {
        SmartAPI.getApiService()
                .getOrders(session.getJWT(), status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderResponses -> {
                    adapter.notifyItemRangeRemoved(0,orderResponseList.size());
                    orderResponseList.clear();
                    orderResponseList.addAll(orderResponses);
                    adapter.notifyItemRangeInserted(0,orderResponses.size());
                },throwable -> Log.e(TAG, "getOrders: "+throwable.getMessage() ));

    }

    private void initRecyclerview() {
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(orderResponseList, getActivity());
        binding.rvOrders.setAdapter(adapter);
        binding.rvOrders.setItemAnimator(new LandingAnimator());
        binding.rvOrders.getItemAnimator().setAddDuration(200);

    }

}
