package com.example.smartpasal.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.R;
import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.ListAdapterItems;
import com.example.smartpasal.databinding.ActivityCategorizedBinding;
import com.example.smartpasal.model.ProductItems;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class categorizedActivity extends AppCompatActivity {

    private ActivityCategorizedBinding binding;
    private static final String TAG = "CATEGORIZED_ACTIVITY";

    private RecyclerView.Adapter myadapter;
    private ArrayList<ProductItems> listnewsData = new ArrayList<>();
    private Session session;
    private List<String> arr;
    private Bundle b;
    private int page_number = 1;
    private String sorting = "";

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
        binding = ActivityCategorizedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(view);
        session = new Session(categorizedActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.mScrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        fetchData();
                    }

                }

        );

        b = getIntent().getExtras();
        //Cart listview

        myadapter = new ListAdapterItems(listnewsData, getApplicationContext());
        binding.rvlist.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        binding.rvlist.setAdapter(myadapter);


        arr = new ArrayList<>();
        arr.add("Popularity");
        arr.add("Price low to high");
        arr.add("Price high to low");
        getSupportActionBar().setTitle(b.getString("category", "category"));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arr);
        binding.spSort.setAdapter(arrayAdapter);
        String category = b.getString("category", "");
        String type = b.getString("type", "");
        binding.spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arr.get(position).equals("Popularity")) {
                    sorting = arr.get(position);
                    if (type.length() == 0)
                        getProductsByCategory(category, sorting);
                    else
                        getProductsByCategoryAndType(type, category, sorting);
                    Toast.makeText(getApplicationContext(), "Popularity", Toast.LENGTH_LONG).show();
                    myadapter.notifyItemRangeRemoved(0, listnewsData.size());
                    listnewsData.clear();
                }
                if (arr.get(position).equals("Price low to high")) {
                    sorting = arr.get(position);
                    if (type.length() == 0)
                        getProductsByCategory(category, sorting);
                    else
                        getProductsByCategoryAndType(type, category, sorting);

                    Toast.makeText(getApplicationContext(), "Price low to high", Toast.LENGTH_LONG).show();
                    myadapter.notifyItemRangeRemoved(0, listnewsData.size());
                    listnewsData.clear();
                }
                if (arr.get(position).equals("Price high to low")) {
                    sorting = arr.get(position);
                    if (type.length() == 0)
                        getProductsByCategory(category, sorting);
                    else
                        getProductsByCategoryAndType(type, category, sorting);
                    Toast.makeText(getApplicationContext(), "Price high to low", Toast.LENGTH_LONG).show();
                    myadapter.notifyItemRangeRemoved(0, listnewsData.size());
                    listnewsData.clear();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void fetchData() {
        b = getIntent().getExtras();

        binding.progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            page_number++;
            binding.progressBar.setVisibility(View.GONE);
        }, 5000);

    }


    private void getProductsByCategoryAndType(String type, String category, String sorting) {
        SmartAPI.getApiService()
                .getProductByCategoryAndType(session.getJWT(), type, category, sorting)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productItems -> {
                            listnewsData.addAll(productItems);
                            myadapter.notifyItemRangeInserted(0,productItems.size());
                        },
                        throwable -> Log.e(TAG, "getProductsByCategoryAndType: " + throwable.getMessage()));

    }

    private void getProductsByCategory(String category, String sorting) {
        SmartAPI.getApiService()
                .getProductByCategory(session.getJWT(), "category", category, sorting)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productItems -> {
                    listnewsData.addAll(productItems);
                    myadapter.notifyItemRangeInserted(0,productItems.size());
                }, throwable -> Log.e(TAG, "getProductsByCategory: " + throwable.getMessage()));


    }


    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.AlertDialog);
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
