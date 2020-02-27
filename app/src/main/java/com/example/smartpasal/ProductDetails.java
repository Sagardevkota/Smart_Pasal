package com.example.smartpasal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import ru.nikartm.support.BadgePosition;
import ru.nikartm.support.ImageBadgeView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
        badgeView.setBadgeValue(HomeActivity.count);
        badgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),cartActivity.class);

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);




        Bundle b=getIntent().getExtras();
        tvProduct_name=findViewById(R.id.tvProduct_Name);
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

        String url="http://idealytik.com/SmartPasalWebServices/AddToCart.php?product_id="+b.getString("product_id")+"&id="+sp.getString("userID","userID");
      new  MyAsyncTaskgetNews().execute(url);
    }

    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works

        }
        @Override
        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds

                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    NewsData = ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);
                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

            }catch (Exception ex){
                Log.d("AddToCarterror",ex.getMessage());
            }
            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try {



                //display response data
                JSONObject json= new JSONObject(progress[0]);

                if (json.getString("msg").equals("Added to cart")) {


                    goToCartActivity();
                }
                if (json.getString("msg").equals("Item is already in cart")){
                    Toast.makeText(getApplicationContext(),"Item is already in cart",Toast.LENGTH_LONG).show();
                }

            }



            catch (Exception ex) {
                Log.d("JSONINCARTerror is", ex.getMessage());
            }

        }


        protected void onPostExecute(String  result2){



        }




    }
    // this method convert any stream to string
    public static String ConvertInputToStringNoChange(InputStream inputStream) {

        BufferedReader bureader=new BufferedReader( new InputStreamReader(inputStream));
        String line ;
        String linereultcal="";

        try{
            while((line=bureader.readLine())!=null) {

                linereultcal+=line;

            }
            inputStream.close();


        }catch (Exception ex){

        }

        return linereultcal;
    }


    private void goToCartActivity() {
        Bundle b=getIntent().getExtras();
        Intent intent=new Intent(getApplicationContext(),cartActivity.class);
        intent.putExtra("UserID",b.getString("UserID") );
        startActivity(intent);
    }


}
