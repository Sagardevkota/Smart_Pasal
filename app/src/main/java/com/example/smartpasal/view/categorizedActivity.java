package com.example.smartpasal.view;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpasal.fragment.home;

import android.app.ProgressDialog;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.ListAdapterItems;

import com.example.smartpasal.R;
import com.example.smartpasal.model.ProductItems;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class categorizedActivity extends AppCompatActivity {

    RecyclerView.Adapter myadapter;
    ArrayList<ProductItems> listnewsData = new ArrayList<ProductItems>();

    TextView category;
    List<String> arr;
    Bundle b;
    RecyclerView lvlist;

    NestedScrollView mScrollView;
    ProgressBar progressBar;
    int page_number=1;

    String sorting="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorized);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mScrollView=findViewById(R.id.mScrollview);
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                  fetchData();
                }

            }
        }

        );

      b = getIntent().getExtras();
           //Cart listview
        lvlist =  findViewById(R.id.LVNews);
        myadapter = new ListAdapterItems(listnewsData,getApplicationContext());
        lvlist.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        lvlist.setAdapter(myadapter);

        Spinner spSort=findViewById(R.id.spSort);

        arr=new ArrayList<>();
        arr.add("Popularity");
        arr.add("Price low to high");
        arr.add("Price high to low");
        getSupportActionBar().setTitle(b.getString("category","category"));

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,arr);
        spSort.setAdapter(arrayAdapter);
        String category=b.getString("category","");
        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arr.get(position).equals("Popularity"))
                {
                    sorting=arr.get(position).toString();
                    getProductsByCategory(sorting,category);


                    Toast.makeText(getApplicationContext(),"Popularity",Toast.LENGTH_LONG).show();
                    listnewsData.clear();
                    myadapter.notifyDataSetChanged();
                }
                if (arr.get(position).equals("Price low to high"))
                {
                    sorting=arr.get(position).toString();
                    getProductsByCategory(sorting,category);

                     Toast.makeText(getApplicationContext(),"Price low to high",Toast.LENGTH_LONG).show();
                    listnewsData.clear();
                    myadapter.notifyDataSetChanged();
                }
                if (arr.get(position).equals("Price high to low"))
                {
                    sorting=arr.get(position).toString();

                   getProductsByCategory(sorting,category);
                    Toast.makeText(getApplicationContext(),"Price high to low",Toast.LENGTH_LONG).show();
                    listnewsData.clear();
                    myadapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }

    private void fetchData() {
        b=getIntent().getExtras();
        progressBar=findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                page_number++;
                progressBar.setVisibility(View.GONE);
            }
        },5000);



    }




    private void getProductsByCategory(String sorting, String category ){
        Call<List<ProductItems>>  items= SmartAPI.getApiService()
                .getProductByCategory( home.jwt,"category",category,sorting);

        items.enqueue(new retrofit2.Callback<List<ProductItems>>() {
            @Override
            public void onResponse(Call<List<ProductItems>> call, Response<List<ProductItems>> response) {
                if (!response.isSuccessful())
                    Log.d("response",response.message());
                else{
                    for (ProductItems productItems:response.body()){
                        listnewsData.add(new ProductItems(productItems));
                        myadapter.notifyDataSetChanged();
                    }
                }


            }

            @Override
            public void onFailure(Call<List<ProductItems>> call, Throwable t) {

            }
        });


    }




    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this,R.style.AlertDialog);
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
