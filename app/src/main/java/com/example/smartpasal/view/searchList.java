package com.example.smartpasal.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.SearchAdapterItems;
import com.example.smartpasal.fragment.home;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.model.SearchItems;
import com.example.smartpasal.view.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class searchList extends AppCompatActivity {
    ImageView backimg;
    EditText etSearch;
    RecyclerView endless_view;

    ArrayList<ProductItems> listnewsData = new ArrayList<>();
    GridLayoutManager layoutManager;
    RecyclerView.Adapter myadapter;
    ProgressBar progressBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        endless_view=findViewById(R.id.endless_view);
        layoutManager=new GridLayoutManager(getApplicationContext(),2);

        endless_view.setLayoutManager(layoutManager);
        myadapter=new SearchAdapterItems(listnewsData,getApplicationContext());
        progressBar=findViewById(R.id.progress_bar);



        endless_view.setHasFixedSize(true);
        endless_view.setAdapter(myadapter);


        backimg = findViewById(R.id.back_img);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        etSearch = findViewById(R.id.etSearch);




        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                listnewsData.clear();


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {





            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){




                    String url="http://idealytik.com/SmartPasalWebServices/SearchLists.php?keyword="+editable.toString();
                    Call<List<ProductItems>> search= SmartAPI.getApiService().searchForProduct(home.jwt,editable.toString());
                   search.enqueue(new Callback<List<ProductItems>>() {
                       @Override
                       public void onResponse(Call<List<ProductItems>> call, Response<List<ProductItems>> response) {
                           if (!response.isSuccessful())
                               Log.d("unsuccess","unsuccess");
                           else{
                               for (ProductItems p:response.body()){
                                   listnewsData.add(new ProductItems(p));
                                   myadapter.notifyDataSetChanged();
                               }
                           }
                       }

                       @Override
                       public void onFailure(Call<List<ProductItems>> call, Throwable t) {

                       }
                   });



                }
                else {
                    listnewsData.clear();
                }



            }
        });

    }




}
