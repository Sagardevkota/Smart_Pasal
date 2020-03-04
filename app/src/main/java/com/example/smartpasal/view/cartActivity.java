package com.example.smartpasal.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartpasal.model.CartAdapterItems;
import com.example.smartpasal.R;
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
import java.util.ArrayList;

public class cartActivity extends AppCompatActivity {
    ListView LVNews;
    MyCustomAdapter myadapter;
    ArrayList<CartAdapterItems> listnewsData = new ArrayList<CartAdapterItems>();
    ListView lvlist;
    Bundle b;
    int qty = 1;
    TextView tvTotalCosts;
    SharedPreferences sp;
    FrameLayout progressBarHolder;
    Animation bounce_animation;
    ImageView bouncing_image;
    LinearLayout calculateLayout;
    ImageView ivDelete;
    Integer TotalPrice;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Cart");
        Bundle b = getIntent().getExtras();
        sp=getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);



        String url = "http://idealytik.com/SmartPasalWebServices/CartLists.php?id="+sp.getString("userID","");
        new MyAsyncTaskgetNews().execute(url);
        calculateLayout=findViewById(R.id.calcualteLayout);

        //Cart listview
        lvlist = (ListView) findViewById(R.id.LVNews);
        tvTotalCosts=findViewById(R.id.tvTotalCosts);


        myadapter = new MyCustomAdapter(listnewsData);

        lvlist.setAdapter(myadapter);




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

    private class MyCustomAdapter extends BaseAdapter {


        public ArrayList<CartAdapterItems> listnewsDataAdpater;


        public MyCustomAdapter(ArrayList<CartAdapterItems> listnewsDataAdpater) {
            this.listnewsDataAdpater = listnewsDataAdpater;

        }



        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {

            return null;
        }


        public Integer grandTotal() {
            Integer totalPrice=0;


            for (int i = 0; i < listnewsDataAdpater.size(); i++) {
                totalPrice +=Integer.valueOf(listnewsDataAdpater.get(i).fixed_price) ;
            }
              TotalPrice=totalPrice;



            return TotalPrice;
        }

        public  void updateTotal(String msg,Integer amount){

            if (msg.equalsIgnoreCase("Decrease")){
                TotalPrice=TotalPrice-amount;

            }
            if (msg.equalsIgnoreCase("Increase")){
                TotalPrice=TotalPrice+amount;
                   }


        }
        public Integer getTotal(){
            return TotalPrice;
        }





        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = getLayoutInflater();
            final View myView = mInflater.inflate(R.layout.layout_cart_lists, null);


            final CartAdapterItems s = listnewsDataAdpater.get(position);
            TextView tvProduct_Name = (TextView) myView.findViewById(R.id.tvProduct_Name);
            tvProduct_Name.setText(s.tvName);
            TextView tvFixed_Price = (TextView) myView.findViewById(R.id.tvFixed_Price);
            tvFixed_Price.setText("Rs. " + s.fixed_price);
            final Button buIncrease = myView.findViewById(R.id.buIncrease);
            final Button buDecrease = myView.findViewById(R.id.buDecrease);
            final TextView tvQty = myView.findViewById(R.id.tvQty);
            ivDelete=myView.findViewById(R.id.ivDelete);
            final Button buDelete = findViewById(R.id.buDelete);
            final Button buCheckOut = findViewById(R.id.buCheckOut);

            buDecrease.setBackgroundColor(Color.parseColor("#ffffff"));
            buDecrease.setTextColor(Color.BLACK);
            buIncrease.setBackgroundColor(Color.parseColor("#ffffff"));
            buIncrease.setTextColor(Color.BLACK);
               calculateLayout.setVisibility(View.VISIBLE);
            buDelete.setEnabled(false);

            buDecrease.setEnabled(false);
            buCheckOut.setEnabled(false);
            tvTotalCosts.setText(String.valueOf(myadapter.grandTotal()));
             ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "http://idealytik.com/SmartPasalWebServices/RemoveFromCart.php?product_id=" + s.user_id+ "&id=" +sp.getString("userID","");
                    new MyAsyncTaskgetNews1().execute(url);

                    listnewsData.remove(position);
                    myadapter.notifyDataSetChanged();

                }
            });









            buIncrease.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                qty = qty + 1;


                                if (qty > 1) {
                                    buDecrease.setEnabled(true);
                                }
                                tvQty.setText(String.valueOf(qty));

                                Integer price=myadapter.grandTotal();

                                price=price+ (qty-1)*Integer.valueOf(listnewsDataAdpater.get(position).fixed_price);
                                Toast.makeText(getApplicationContext(),"new Price is"+price,Toast.LENGTH_SHORT).show();





                                tvTotalCosts.setText(String.valueOf(price));





                            }
                        });
                        buDecrease.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                qty = qty - 1;
                                if (qty < 2) {
                                    buDecrease.setEnabled(false);
                                }
                                tvQty.setText(String.valueOf(qty));
                                Integer price=myadapter.grandTotal();
                                price=price-qty*Integer.valueOf(listnewsDataAdpater.get(position).fixed_price);



                                tvTotalCosts.setText(String.valueOf(price));
                                Toast.makeText(getApplicationContext(),"new Price is"+price,Toast.LENGTH_SHORT).show();


                            }
                        });






            ImageView ivImg = (ImageView) myView.findViewById(R.id.ivImg);


            try {
                String url = s.picture_path;

                Picasso.get()
                        .load(url)
                        .fit()
                        .into(ivImg, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d("Load", "Successfull");

                            }

                            @Override
                            public void onError(Exception e) {
                                Log.d("Load", e.getMessage());
                            }
                        });
            } catch (Exception e) {
                Log.d("Cartlist error", e.getMessage());
            }


            return myView;
        }


    }





    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
            progressBarHolder = findViewById(R.id.progressBarHolder);
            progressBarHolder.setVisibility(View.VISIBLE);
            bouncing_image=findViewById(R.id.bouncing_image);

            bounce_animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce_animation);

            bouncing_image.setAnimation(bounce_animation);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);


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

            } catch (Exception ex) {
                Log.d("error", ex.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {

            try {
                //display response data



                JSONArray userInfo = new JSONArray(progress[0]);
                for (int i = 0; i < userInfo.length(); i++) {
                    JSONObject userCredentials = userInfo.getJSONObject(i);

                    listnewsData.add(new CartAdapterItems(userCredentials.getString("name"), userCredentials.getString("picture_path"), userCredentials.getString("product_id"), userCredentials.getString("marked_price"), userCredentials.getString("fixed_price")));


                }


                myadapter.notifyDataSetChanged();
                //display response data

            } catch (Exception ex) {
                Log.d("Cartlist error is", ex.getMessage());
            }

        }


        protected void onPostExecute(String result2) {

progressBarHolder.setVisibility(View.GONE);
bounce_animation.cancel();
        }


    }

    // this method convert any stream to string
    public static String ConvertInputToStringNoChange(InputStream inputStream) {

        BufferedReader bureader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String linereultcal = "";

        try {
            while ((line = bureader.readLine()) != null) {

                linereultcal += line;

            }
            inputStream.close();


        } catch (Exception ex) {
        }

        return linereultcal;
    }

    public class MyAsyncTaskgetNews1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
            progressBarHolder = findViewById(R.id.progressBarHolder);
            progressBarHolder.setVisibility(View.VISIBLE);
            bouncing_image=findViewById(R.id.bouncing_image);

            bounce_animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce_animation);

            bouncing_image.setAnimation(bounce_animation);
        }

        @Override
        protected String doInBackground(String... params) {
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

            } catch (Exception ex) {
                Log.d("error", "eo");
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {

            try {
                JSONObject json = new JSONObject(progress[0]);
                if (json.getString("msg").equalsIgnoreCase("Item removed from cart")) {
                    Toast.makeText(getApplicationContext(), "Item is removed from cart", Toast.LENGTH_LONG).show();
                }


            } catch (Exception ex) {
                Log.d("error is", ex.getMessage());
            }

        }


        protected void onPostExecute(String result2) {

            progressBarHolder.setVisibility(View.GONE);
            bounce_animation.cancel();
        }


    }
}
