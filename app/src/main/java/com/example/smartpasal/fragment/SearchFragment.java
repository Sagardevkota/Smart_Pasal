package com.example.smartpasal.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.smartpasal.R;
import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.SearchAdapterItems;
import com.example.smartpasal.databinding.FragmentSearchBinding;
import com.example.smartpasal.model.ProductItems;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragment extends Fragment {

    private static final String TAG = "SEARCH_FRAGMENT";
    private FragmentSearchBinding binding;
    private final ArrayList<ProductItems> productItemsList = new ArrayList<>();
    private Session session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = FragmentSearchBinding.inflate(getLayoutInflater()).getRoot();
        binding = FragmentSearchBinding.bind(view);
        session = new Session(getContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            String searchedKeyword = bundle.getString("search_keyword");
            showProgressDialog("Loading");
            getSearchedProducts(searchedKeyword);
        }

        return view;
    }

    private void getSearchedProducts(String searchedKeyword) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.endlessView.setLayoutManager(layoutManager);
        SearchAdapterItems searchAdapter = new SearchAdapterItems(productItemsList, getContext());
        binding.endlessView.setHasFixedSize(true);
        binding.endlessView.setAdapter(searchAdapter);

        SmartAPI.getApiService()
                .searchForProduct(session.getJWT(), searchedKeyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productItems -> {

                            binding.tvSearchQuery
                                    .setText(productItems.size() + " items matched your query \"" + searchedKeyword.toUpperCase(Locale.ROOT) + "\"");

                            productItemsList.clear();
                            productItemsList.addAll(productItems);
                            searchAdapter.notifyDataSetChanged();
                            hideProgressDialog();
                        },
                        throwable -> {
                            hideProgressDialog();
                            Log.e(TAG, "productItemList: " + throwable.getMessage());
                        });


    }


    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
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