package com.example.smartpasal.view;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Header;
import ru.nikartm.support.ImageBadgeView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.JsonResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.fragment.home;
import com.example.smartpasal.model.Carts;
import com.example.smartpasal.view.HomeActivity;
import com.example.smartpasal.view.cartActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductDetails extends AppCompatActivity {
    TextView tvProduct_name;
    TextView tvFixed_price;
    TextView tvMarked_price;
    TextView tvBrand;
    TextView tvDesc;
    TextView tvSku;
    ImageView ivProduct_image;
    RatingBar ratingBar;
    Bundle b;
    SharedPreferences sp;
    ImageBadgeView badgeView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem menuItem = menu.findItem(R.id.cart);
        badgeView = menuItem.getActionView().findViewById(R.id.ibv_icon);

        badgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), cartActivity.class);

                startActivity(intent);


            }
        });

        return super.onPrepareOptionsMenu(menu);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        sp=getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.transparent_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






        Bundle b=getIntent().getExtras();
        tvProduct_name=findViewById(R.id.tvProduct_Name);
        getSupportActionBar().setTitle(b.getString("product_name","Product"));
        tvFixed_price=findViewById(R.id.tvFixed_price);
        tvMarked_price=findViewById(R.id.tvMarked_price);
        tvBrand=findViewById(R.id.tvBrand);
        tvSku=findViewById(R.id.tvSku);
        tvDesc=findViewById(R.id.tvDesc);
        ivProduct_image=findViewById(R.id.ivProduct_image);
        tvProduct_name.setText(b.getString("product_name"));
        tvFixed_price.setText("Rs."+b.getString("fixed_price"));
        tvMarked_price.setText("Rs."+b.getString("marked_price"));
        tvMarked_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvBrand.setText(b.getString("brand"));
        tvSku.setText(b.getString("sku"));
        tvDesc.setText(b.getString("desc"));
        ratingBar=findViewById(R.id.ratingBar);
        ratingBar.setRating(4);









        try{
            String url=b.getString("product_photo");

            Picasso.get()
                    .load(url)
                    .fit()
                    .into(ivProduct_image, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Load","Successfull");

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Load",e.getMessage());
                        }
                    });}
        catch (Exception e){
            Log.d("error",e.getMessage());
        }



    }


    public void buAdd(View view) {
        Bundle b=getIntent().getExtras();
        Integer product_id=b.getInt("product_id",0);
        Integer user_id=Integer.valueOf(sp.getString("userId",""));
        Carts carts=new Carts(user_id,product_id);
        Call<JsonResponse> addToCartList= SmartAPI.getApiService().addToCartList(home.jwt,carts);
        addToCartList.enqueue(new retrofit2.Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (!response.isSuccessful())
                    Log.d("","");
                else
                {
                    String status=response.body().getStatus();
                    String message=response.body().getMessage();
                    if (status.equalsIgnoreCase("200 OK")&&message.equalsIgnoreCase("Added to cart"))
                    { Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        goToCartActivity();
                    }
                    if (status.equalsIgnoreCase("401 Conflict")&&message.equalsIgnoreCase("Item is already in cart"))
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });


    }




    private void goToCartActivity() {
        Bundle b=getIntent().getExtras();
        Intent intent=new Intent(getApplicationContext(),cartActivity.class);
        intent.putExtra("UserID",b.getString("UserID") );
        startActivity(intent);
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
