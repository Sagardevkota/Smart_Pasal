package com.example.smartpasal;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.VisibleForTesting;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;




/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {
    Context context;
   private SharedPreferences sp;
   TextView verified;
    TextView tvSneakers;
    TextView tvPhones;
    TextView tvTVs;
    TextView tvLaptops;
    TextView tvJackets;
    TextView tvSunglasses;
    TextView tvWatches;
    TextView tvBags;
    GridView LVNews;
    MyCustomAdapter myadapter;
    ArrayList<ProductsAdapterItems> listnewsData = new ArrayList<ProductsAdapterItems>();
    ScrollView mScrollView;
    GridView lvlist;
    FrameLayout progressBarHolder;
    ImageView bouncing_image;
    Animation bounce_animation;
    View v;
    Integer page_number=1;
    Integer item_count=6;



    public home() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_home, container, false);
        lvlist = (GridView)v.findViewById(R.id.LVNews);

        verified=v.findViewById(R.id.verified);
        tvSneakers=v.findViewById(R.id.tvSneakers);
        tvPhones=v.findViewById(R.id.tvPhones);
        tvTVs=v.findViewById(R.id.tvTv);
        tvLaptops=v.findViewById(R.id.tvLaptops);
        tvJackets=v.findViewById(R.id.tvJackets);
        tvSunglasses=v.findViewById(R.id.tvSunGlasses);
        tvWatches=v.findViewById(R.id.tvWatch);
        tvBags=v.findViewById(R.id.tvBag);
        tvSneakers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Shoes");

                startActivity(intent);

            }
        });
        tvBags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Bags");

                startActivity(intent);

            }
        });
        tvPhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Phones");

                startActivity(intent);
            }
        });
        tvTVs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Tvs");

                startActivity(intent);

            }
        });
        tvLaptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Laptops");

                startActivity(intent);

            }
        });
        tvJackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Jackets");

                startActivity(intent);

            }
        });
        tvSunglasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Sunglasses");

                startActivity(intent);

            }
        }); tvWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Watches");

                startActivity(intent);

            }
        });


           sp=this.getActivity().getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);
          String id= sp.getString("userID","userID");
          //Check if user is verified or not
        String url = "http://idealytik.com/SmartPasalWebServices/checkVerification.php?id="+id;
        new MyAsyncTaskgetNews().execute(url);
          //Product listview


        myadapter = new MyCustomAdapter(listnewsData);
        lvlist.setAdapter(myadapter);


        String Producturl = "http://idealytik.com/SmartPasalWebServices/ProductLists.php?page_number="+page_number+"&item_count="+item_count;
        new MyAsyncTaskgetNews1().execute(Producturl);



        return v;
    }
    // get news from server
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
                urlConnection.setRequestProperty("http.keepAlive", "false");
                urlConnection.setUseCaches(false);
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
                Log.d("Internet error",ex.getMessage());
            }
            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try{
                JSONObject json= new JSONObject(progress[0]);
                //display response data

                if (json.getString("msg").equals("Email is not verified")) {

                    verified.setText("Please verify your account so that you can purchase any products");
                      Toast.makeText(getContext(),"Acc is not verified",Toast.LENGTH_SHORT).show();


                }

                if (json.getString("msg").equals("Email is verified"))
                {

                    verified.setVisibility(View.GONE);

                     }
            }


            catch (Exception ex) {
                Log.d("er", ex.getMessage());
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


        }catch (Exception ex){}

        return linereultcal;
    }
    private class MyCustomAdapter extends BaseAdapter {


        public ArrayList<ProductsAdapterItems> listnewsDataAdpater ;

        public MyCustomAdapter(ArrayList<ProductsAdapterItems>  listnewsDataAdpater) {
            this.listnewsDataAdpater=listnewsDataAdpater;
        }


        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            LayoutInflater mInflater = getLayoutInflater();
            final View myView = mInflater.inflate(R.layout.layout_product_lists, null);

            final ProductsAdapterItems s = listnewsDataAdpater.get(position);
            TextView tvProduct_Name=( TextView)myView.findViewById(R.id.tvProduct_Name);
            tvProduct_Name.setText(s.tvName);
            TextView tvFixed_Price=( TextView)myView.findViewById(R.id.tvFixed_Price);
            tvFixed_Price.setText("Rs. "+ s.fixed_price);
            TextView tvMarked_Price=( TextView)myView.findViewById(R.id.tvMarked_Price);
            tvMarked_Price.setText("Rs. "+s.marked_price);
            tvMarked_Price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);



            ImageView ivImg=(ImageView)myView.findViewById(R.id.ivImg);


            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getContext(),ProductDetails.class);
                    intent.putExtra("product_id",s.user_id);
                    intent.putExtra("product_name",s.tvName);
                    intent.putExtra("product_photo",s.picture_path);
                    intent.putExtra("fixed_price",s.fixed_price);
                    intent.putExtra("marked_price",s.marked_price);
                    intent.putExtra("brand",s.brand);
                    intent.putExtra("desc",s.desc);
                    intent.putExtra("sku",s.sku);

                    startActivity(intent);

                }
            });






            try{
                String url=s.picture_path;

                Picasso.get()
                        .load(url)
                        .fit()
                        .into(ivImg, new Callback() {
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

            return myView;
        }





    }
    public class MyAsyncTaskgetNews1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
            progressBarHolder = v.findViewById(R.id.progressBarHolder);
            progressBarHolder.setVisibility(View.VISIBLE);
            bouncing_image=v.findViewById(R.id.bouncing_image);

            bounce_animation= AnimationUtils.loadAnimation(getContext(),R.anim.bounce_animation);

            bouncing_image.setAnimation(bounce_animation);

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
                urlConnection.setRequestProperty("http.keepAlive", "false");
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

            }catch (Exception ex){
                Log.d("error",ex.getMessage());
            }
            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try {
                JSONObject json = new JSONObject(progress[0]);
                //display response data

                if (json.getString("msg").equals("Loading")) {
                    JSONArray userInfo = new JSONArray(json.getString("user_info"));

                    for (int i = 0; i < userInfo.length(); i++) {
                        JSONObject userCredentials = userInfo.getJSONObject(i);

                        listnewsData.add(new ProductsAdapterItems(userCredentials.getString("name"),userCredentials.getString("picture_path"),userCredentials.getString("product_id"),userCredentials.getString("marked_price"),userCredentials.getString("fixed_price"),userCredentials.getString("brand"),userCredentials.getString("desc"),userCredentials.getString("sku")));


                    }

                }








                myadapter.notifyDataSetChanged();
                //display response data


            }



            catch (Exception ex) {
                Log.d("error is", ex.getMessage());
                Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
            }

        }


        protected void onPostExecute(String  result2){

            progressBarHolder.setVisibility(View.GONE);
            bounce_animation.cancel();

        }




    }

    // this method convert any stream to string




}
