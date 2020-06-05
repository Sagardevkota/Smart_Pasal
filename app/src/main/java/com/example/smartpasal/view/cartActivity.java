package com.example.smartpasal.view;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.JsonResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.CartAdapter;
import com.example.smartpasal.model.Carts;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.fragment.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class cartActivity extends AppCompatActivity {

   CartAdapter myadapter;
    ArrayList<ProductItems> listnewsData = new ArrayList<>();
    RecyclerView lvlist;
    Bundle b;
    int qty = 1;
    TextView tvEmptyCart;
    Boolean success=false;

    SharedPreferences sp;
    TextView tvTotalPrice;
    int counter=0;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.cart_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete:
                if (myadapter.getNumCheckBoxes().size()==0)
                    Toast.makeText(getApplicationContext(),"Please select any item to delete",Toast.LENGTH_SHORT).show();
                else{
                    Log.d("dataArray",myadapter.getNumCheckBoxes().toString());

                    for (int i=0;i<myadapter.getNumCheckBoxes().size();i++){

                        String productId=myadapter.getNumCheckBoxes().get(i).get("Id");
                        int position=Integer.parseInt(myadapter.getNumCheckBoxes().get(i).get("Position"));
                        Log.d("dataArray","item is "+ productId +" at " + String.valueOf(position));
                 showProgressDialog("Removing from cart");
                  removeFromCart(productId);
                  hideProgressDialog();
                    }

                        hideProgressDialog();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);







                }

        }
        return super.onOptionsItemSelected(item);
    }

    private void removeFromCart(String productId) {
        String userId=sp.getString("userId","");
        Carts cart=new Carts(Integer.valueOf(userId),Integer.valueOf(productId));
        Call<JsonResponse> removeFromCart=SmartAPI.getApiService().removeFromCart(home.jwt,cart);
        removeFromCart.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (!response.isSuccessful())
                    Log.d("unsuccussful","unsuccessful");
                else{
                    String status=response.body().getStatus();
                    String message=response.body().getMessage();
                    if (status.equalsIgnoreCase("200 OK")&&message.equalsIgnoreCase("Item is removed from cart"))
                    {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Bundle b = getIntent().getExtras();
        sp = getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);


        //Cart listview
        lvlist = findViewById(R.id.LVNews);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        myadapter = new CartAdapter(listnewsData, getApplicationContext(),tvTotalPrice);
        lvlist.setAdapter(myadapter);
        lvlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //get Cart List

        getCartItems();


        Button buCheckOut = findViewById(R.id.buCheckOut);





        buCheckOut.setOnClickListener((View v) ->
        {
            Log.d("totalprice",String.valueOf(myadapter.getTotal()));
            Toast.makeText(getApplicationContext(), String.valueOf(myadapter.getTotal()), Toast.LENGTH_SHORT).show();
        });

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    public void getCartItems(){
        Integer userId=Integer.valueOf(sp.getString("userId",""));
     Call<List<ProductItems>> cartList=   SmartAPI.getApiService().getCartList(home.jwt,userId);
     cartList.enqueue(new Callback<List<ProductItems>>() {
         @Override
         public void onResponse(Call<List<ProductItems>> call, Response<List<ProductItems>> response) {
             if (!response.isSuccessful())
                 Log.d("unsuccesful","unsuccessful");
             else {
                 if (response.body().size()==0)
                 tvEmptyCart.setVisibility(View.VISIBLE);
                 for (ProductItems p:response.body()){
                     Log.d("products",p.toString());
                     listnewsData.add(new ProductItems(p));
                     myadapter.notifyDataSetChanged();
                 }

             }

         }

         @Override
         public void onFailure(Call<List<ProductItems>> call, Throwable t) {
             Log.d("Error",t.getMessage());

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
