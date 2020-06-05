package com.example.smartpasal.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;


import android.os.Bundle;

import androidx.annotation.NonNull;
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


import com.airbnb.lottie.LottieAnimationView;

import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.ListAdapterItems;
import com.example.smartpasal.adapter.SliderAdapterExample;
import com.example.smartpasal.model.CategoryAdapterItems;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.R;
import com.example.smartpasal.model.SliderItems;
import com.example.smartpasal.view.HomeActivity;

import com.example.smartpasal.view.LoginActivity;
import com.example.smartpasal.view.categorizedActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    ArrayList<ProductItems> listnewsData = new ArrayList<>();
    NestedScrollView mScrollView;
    GridView lvlist;
    FrameLayout progressBarHolder;
    ImageView bouncing_image;
    Animation bounce_animation;
    View v;
    GridLayoutManager layoutManager;
    RecyclerView.Adapter myadapter;
    public static  String jwt;



    public int page_number=1;

    public static boolean isLoaded=false;


    private List<SliderItems> mSliderItems = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public home() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_home, container, false);
        SliderView sliderView = v.findViewById(R.id.imageSlider);
        sp=getActivity().getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);



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


        Intent intent=new Intent(context,categorizedActivity.class);
        tvSneakers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Shoes");
                startActivity(intent);

            }
        });
        tvBags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Bags");
                startActivity(intent);
            }
        });
        tvJackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Jackets");
                startActivity(intent);
            }
        });
        tvWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });
        tvBags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Bags");
                startActivity(intent);
            }
        });
        tvPhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Cell Phones And Accessories");
                startActivity(intent);
            }
        });

        tvSunglasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Sunglasses");
                startActivity(intent);
            }
        });

        tvLaptops.setOnClickListener((View view)->{
            intent.putExtra("category","Computers And Accessories");
            startActivity(intent);
        });
        tvTVs.setOnClickListener((View view)->{
            intent.putExtra("category","Television And Video");
            startActivity(intent);
        });


        getProducts(page_number);
        endless_view.setNestedScrollingEnabled(false);


        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View)mScrollView.getChildAt(mScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (mScrollView.getHeight() + mScrollView
                        .getScrollY()));

                if (diff == 0) {

                    fetchData();

                }

            }
        });




        return v;
    }


    private void fetchData() {
        final LottieAnimationView progressview=v.findViewById(R.id.progress_animation_view);

          progressview.setVisibility(View.VISIBLE);

         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {

                 page_number++;
           getProducts(page_number);

              progressview.setVisibility(View.INVISIBLE);
             }
         },5000);



    }

    public void getProducts(int page_number) {


        if (sp!=null){

            if (sp.contains("jwt")){

                String token=sp.getString("jwt","");
                String[] split_string = token.split("\\.");
                String payload = split_string[1];
                String body=new String(Base64.getDecoder().decode(payload)) ;
                Log.d("jwtPayload",body);
                HashMap<String,String> map = new Gson().fromJson(body, new TypeToken<HashMap<String, String>>(){}.getType());
                String userName=map.get("sub");
                String exp=map.get("exp");
                String iat=map.get("iat");
                if (Instant.now().getEpochSecond() >= Long.valueOf(exp) ) {
                    Log.d("expired","true");
                    Toasty.error(context,"Session expired").show();
                    goToLoginActivity();
                }
                else{
                    sp.edit().putString("userName",userName).apply();
                    jwt="Bearer "+sp.getString("jwt","");

                    Call<List<ProductItems>> getProduct= SmartAPI.getApiService().getProducts(jwt,page_number);
                    getProduct.enqueue(new Callback<List<ProductItems>>() {
                        @Override
                        public void onResponse(Call<List<ProductItems>> call, Response<List<ProductItems>> response) {
                            if (!response.isSuccessful())
                                Log.d("msg","unsuccessfull");
                            else{
                                for (ProductItems p:response.body()){
                                    Log.d("products",p.toString());
                                    listnewsData.add(new ProductItems(p));
                                    myadapter.notifyDataSetChanged();
                                }
                            }






                        }

                        @Override
                        public void onFailure(Call<List<ProductItems>> call, Throwable t) {
                            isLoaded=true;

                            Log.e("error",t.getMessage());
                        }
                    });

                }




            }
            else
            {
                Log.d("error","no jwt");
                goToLoginActivity();
            }




        }
        else goToLoginActivity();



        }

      private  void  goToLoginActivity(){

        sp.edit().clear().apply();
        Intent intent=new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

        }




}













