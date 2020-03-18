package com.example.smartpasal.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartpasal.adapter.ListAdapterItems;
import com.example.smartpasal.adapter.SliderAdapterExample;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.R;
import com.example.smartpasal.model.SliderItems;
import com.example.smartpasal.view.HomeActivity;
import com.example.smartpasal.view.MainActivity;
import com.example.smartpasal.view.categorizedActivity;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

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
    RecyclerView endless_view;

    ArrayList<ProductItems> listnewsData = new ArrayList<ProductItems>();
    NestedScrollView mScrollView;
    GridView lvlist;
    FrameLayout progressBarHolder;
    ImageView bouncing_image;
    Animation bounce_animation;
    View v;
    GridLayoutManager layoutManager;
    RecyclerView.Adapter myadapter;


    public int page_number=1;

    private boolean isLoading=true;

    ProgressBar progressBar;
    private List<SliderItems> mSliderItems = new ArrayList<>();





    public home() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_home, container, false);
        SliderView sliderView = v.findViewById(R.id.imageSlider);

        SliderAdapterExample adapter = new SliderAdapterExample(getContext());

        sliderView.setSliderAdapter(adapter);
        adapter.addItem(new SliderItems("first_image","https://cdn.accentuate.io/27542519867/5482824892475/VIPFlashsale-Desktop.jpg","2"));
        adapter.addItem(new SliderItems("second_image","https://gpcdn.ams3.cdn.digitaloceanspaces.com/promotions/honor-10-lite-rs1-sale.png","2"));

        adapter.addItem(new SliderItems("third_image","https://i1.wp.com/www.igeekphone.com/wp-content/uploads/2017/11/gdggdg.png","2"));



        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(5); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        endless_view=v.findViewById(R.id.endless_view);
        layoutManager=new GridLayoutManager(getContext(),2);
        mScrollView=v.findViewById(R.id.mScrollview);
        endless_view.setLayoutManager(layoutManager);
        myadapter=new ListAdapterItems(listnewsData,getContext());


        endless_view.setHasFixedSize(true);
        endless_view.setAdapter(myadapter);


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
                Intent intent=new Intent(getContext(), categorizedActivity.class);
                intent.putExtra("category","Shoes");
                intent.putExtra("type","Both");

                startActivity(intent);

            }
        });
        tvBags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Bags");
                intent.putExtra("type","Both");

                startActivity(intent);

            }
        });
        tvPhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Cell Phones And Accessories");
                intent.putExtra("type","Electronics");

                startActivity(intent);
            }
        });
        tvTVs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Television And Video");
                intent.putExtra("type","Electronics");

                startActivity(intent);

            }
        });
        tvLaptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Computers And Accessories");
                intent.putExtra("type","Electronics");

                startActivity(intent);

            }
        });
        tvJackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Jackets");
                intent.putExtra("type","Both");

                startActivity(intent);

            }
        });
        tvSunglasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Sunglasses");
                intent.putExtra("type","Both");

                startActivity(intent);

            }
        }); tvWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),categorizedActivity.class);
                intent.putExtra("category","Watches");
                intent.putExtra("type","Both");

                startActivity(intent);

            }
        });


           sp=this.getActivity().getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);
          String id= sp.getString("userID","userID");
          //Check if user is verified or not
        String url = "http://idealytik.com/SmartPasalWebServices/checkVerification.php?id="+id;
        new MyAsyncTaskgetNews().execute(url);
          //Product listview



        String Producturl = "http://idealytik.com/SmartPasalWebServices/ProductLists.php?page_number="+page_number;
        new MyAsyncTaskgetNews1().execute(Producturl);
        endless_view.setNestedScrollingEnabled(false);


        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View)mScrollView.getChildAt(mScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (mScrollView.getHeight() + mScrollView
                        .getScrollY()));

                if (diff == 0) {

                     fetchData();
                    // your pagination code
                }

            }
        });




        return v;
    }


    private void fetchData() {

        progressBar=v.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {

                 page_number++;
                 String Producturl = "http://idealytik.com/SmartPasalWebServices/ProductLists.php?page_number="+page_number;
                 new MyAsyncTaskgetNews1().execute(Producturl);
                 progressBar.setVisibility(View.GONE);
             }
         },5000);



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

                urlConnection.setRequestProperty("APIKEY", MainActivity.Smart_api_key);


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




    public class MyAsyncTaskgetNews1 extends AsyncTask<String, String, String> {
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
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds
                urlConnection.setDoOutput(true);

                urlConnection.setRequestProperty("APIKEY",MainActivity.Smart_api_key);



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
                JSONArray userInfo = new JSONArray(progress[0]);


                    for (int i = 0; i < userInfo.length(); i++) {
                        JSONObject userCredentials = userInfo.getJSONObject(i);

                        listnewsData.add(new ProductItems(userCredentials.getString("name"),userCredentials.getString("picture_path"),userCredentials.getString("product_id"),userCredentials.getString("marked_price"),userCredentials.getString("fixed_price"),userCredentials.getString("brand"),userCredentials.getString("desc"),userCredentials.getString("sku")));


                    }










                myadapter.notifyDataSetChanged();
                //display response data


            }



            catch (Exception ex) {
    }


        }

        protected void onPostExecute(String  result2){



        }




    }

    // this method convert any stream to string




}
