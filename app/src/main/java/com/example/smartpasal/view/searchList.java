package com.example.smartpasal.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.SearchAdapterItems;
import com.example.smartpasal.databinding.ActivitySearchListBinding;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.Session.Session;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class searchList extends AppCompatActivity {

    private ActivitySearchListBinding binding;
    private Session session;
    ArrayList<ProductItems> listnewsData = new ArrayList<>();
    GridLayoutManager layoutManager;
    SearchAdapterItems myadapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchListBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        session=new Session(searchList.this);
        layoutManager=new GridLayoutManager(getApplicationContext(),2);

       binding.endlessView.setLayoutManager(layoutManager);
        myadapter=new SearchAdapterItems(listnewsData,getApplicationContext());


        binding.endlessView.setHasFixedSize(true);
        binding.endlessView.setAdapter(myadapter);



        binding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });





        binding.etSearch.getEditText().addTextChangedListener(new TextWatcher() {
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
                    Call<List<ProductItems>> search= SmartAPI.getApiService().searchForProduct(session.getJWT(),editable.toString());
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
