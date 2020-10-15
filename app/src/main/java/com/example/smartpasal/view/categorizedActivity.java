package com.example.smartpasal.view;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.databinding.ActivityCategorizedBinding;

import android.app.ProgressDialog;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.TextView;
import android.widget.Toast;

import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.ListAdapterItems;

import com.example.smartpasal.R;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.Session.Session;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class categorizedActivity extends AppCompatActivity {

    private ActivityCategorizedBinding binding;

    RecyclerView.Adapter myadapter;
    ArrayList<ProductItems> listnewsData = new ArrayList<ProductItems>();
    private Session session;

    TextView category;
    List<String> arr;
    Bundle b;
    int page_number=1;
    String sorting="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCategorizedBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        setContentView(view);
        session=new Session(categorizedActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       binding.mScrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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

        myadapter = new ListAdapterItems(listnewsData,getApplicationContext());
        binding.rvlist.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
       binding.rvlist.setAdapter(myadapter);



        arr=new ArrayList<>();
        arr.add("Popularity");
        arr.add("Price low to high");
        arr.add("Price high to low");
        getSupportActionBar().setTitle(b.getString("category","category"));

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,arr);
        binding.spSort.setAdapter(arrayAdapter);
        String category=b.getString("category","");
        String type=b.getString("type","");
       binding.spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arr.get(position).equals("Popularity"))
                {
                    sorting=arr.get(position);
                    if (type.length()==0)
                        getProductsByCategory(category,sorting);
                        else
                    getProductsByCategoryAndType(type,category,sorting);
                    Toast.makeText(getApplicationContext(),"Popularity",Toast.LENGTH_LONG).show();
                    listnewsData.clear();
                    myadapter.notifyDataSetChanged();
                }
                if (arr.get(position).equals("Price low to high"))
                {
                    sorting=arr.get(position);
                    if (type.length()==0)
                        getProductsByCategory(category,sorting);
                    else
                    getProductsByCategoryAndType(type,category,sorting);

                     Toast.makeText(getApplicationContext(),"Price low to high",Toast.LENGTH_LONG).show();
                    listnewsData.clear();
                    myadapter.notifyDataSetChanged();
                }
                if (arr.get(position).equals("Price high to low"))
                {
                    sorting=arr.get(position);
                    if (type.length()==0)
                        getProductsByCategory(category,sorting);
                    else
                        getProductsByCategoryAndType(type,category,sorting);
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

        binding.progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page_number++;
               binding. progressBar.setVisibility(View.GONE);
            }
        },5000);

    }




    private void getProductsByCategoryAndType(String type,String category, String sorting ){
        Call<List<ProductItems>>  items= SmartAPI.getApiService()
                .getProductByCategoryAndType(session.getJWT(),type,category,sorting);

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

    private void getProductsByCategory(String category, String sorting ){
        Call<List<ProductItems>>  items= SmartAPI.getApiService()
                .getProductByCategory(session.getJWT(),"category",category,sorting);
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
