package com.example.smartpasal.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;


import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.EndlessRecyclerViewScrollListener;
import com.example.smartpasal.adapter.HorizontalListAdapter;
import com.example.smartpasal.adapter.ListAdapterItems;
import com.example.smartpasal.adapter.SliderAdapterExample;
import com.example.smartpasal.databinding.FragmentHomeBinding;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.model.SliderItems;

import com.example.smartpasal.Session.Session;

import com.example.smartpasal.view.HomeActivity;
import com.example.smartpasal.view.categorizedActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


import java.util.ArrayList;

import java.util.List;
import java.util.Random;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {
    private FragmentHomeBinding binding;
    private Session session;
    Context context;

    private EndlessRecyclerViewScrollListener scrollListener;
    ArrayList<ProductItems> listnewsData = new ArrayList<>();
    ArrayList<ProductItems> productData=new ArrayList<>();


    View v;
    ListAdapterItems myadapter;
    HorizontalListAdapter myadapter1;
    GradientDrawable gd;


    public int page_number = 1;
    public  boolean isLoaded = false;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        session=new Session(context);
    }

    public home() {
        // Required empty public constructor

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(getLayoutInflater());
        v=binding.getRoot();




        isLoaded=false;

        SliderAdapterExample adapter = new SliderAdapterExample(getContext());

        binding.imageSlider.setSliderAdapter(adapter);
        adapter.addItem(new SliderItems("first_image",
                "https://shoppingrechargeoffers.com/wp-content/uploads/2019/06/Fashion-Wardrobe-Sale-Amazon.png",
                "2"));
        adapter.addItem(new SliderItems("second_image",
                "https://c8.alamy.com/comp/2AKGT2Y/electronics-and-devices-promotional-sale-banner-with-full-shopping-cart-technology-and-online-shopping-concept-2AKGT2Y.jpg",
                "2"));

        adapter.addItem(new SliderItems("third_image",
                "https://i.pinimg.com/600x315/d9/0f/f9/d90ff988ec7db9e7d50d166cc670360c.jpg",
                "2"));


        binding.imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
        binding.imageSlider.startAutoCycle();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);

        binding.endlessView.setLayoutManager(layoutManager);
        myadapter = new ListAdapterItems(listnewsData, getContext());
        myadapter1=new HorizontalListAdapter(productData,getContext());
        binding.rvMostSelling.setLayoutManager(linearLayoutManager);
        binding.endlessView.setHasFixedSize(true);
        binding.endlessView.setAdapter(myadapter);
        binding.rvMostSelling.setAdapter(myadapter1);


        Handler mHandler=new Handler();


        Runnable mToastRunnable = new Runnable() {
            @Override
            public void run() {


                Random rnd = new Random();
                int color = Color.argb(153, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                int color1 = Color.argb(155, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                int[] colors={color,color1};
                //create a new gradient color

                gd = new GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT, colors);
                gd.setCornerRadius(0f);


                binding.dvHome.setBackground(gd);



                mHandler.postDelayed(this, 5000);
            }
        };
        mToastRunnable.run();





        Intent intent = new Intent(context, categorizedActivity.class);
        binding.tvSneakers.setOnClickListener(view -> {
            intent.putExtra("category", "Shoes");
            startActivity(intent);

        });
       binding.tvBag.setOnClickListener(view -> {
           intent.putExtra("category", "Bags");
           startActivity(intent);
       });
       binding.tvJackets.setOnClickListener(view -> {
           intent.putExtra("category", "Jackets");
           startActivity(intent);
       });
       binding.tvWatch.setOnClickListener(view -> {
           intent.putExtra("category", "Watches");
           startActivity(intent);
       });

        binding.tvPhones.setOnClickListener(view -> {
            intent.putExtra("category", "Cell Phones And Accessories");
            startActivity(intent);
        });

        binding.tvSunGlasses.setOnClickListener(view -> {
            intent.putExtra("category", "Sunglasses");
            startActivity(intent);
        });

       binding.tvLaptops.setOnClickListener((View view) -> {
            intent.putExtra("category", "Computers And Accessories");
            startActivity(intent);
        });
        binding.tvTv.setOnClickListener((View view) -> {
            intent.putExtra("category", "Television And Video");
            startActivity(intent);
        });

        getMostSellingProducts();

        getRecommendedProducts(page_number);

        binding.endlessView.setNestedScrollingEnabled(false);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d("page",String.valueOf(page));

                fetchData(page);

            }
        };
        // Adds the scroll listener to RecyclerView
        binding.endlessView.addOnScrollListener(scrollListener);


//        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                View view = (View) mScrollView.getChildAt(mScrollView.getChildCount() - 1);
//
//                int diff = (view.getBottom() - (mScrollView.getHeight() + mScrollView
//                        .getScrollY()));
//
//                if (diff == 0) {
//
//                    fetchData();
//
//                }
//
//            }
//        });



        return v;
    }


    private void fetchData(int page) {
        if(!isLoaded){
            page++;

            binding.progressAnimationView.setVisibility(View.VISIBLE);
            getRecommendedProducts(page);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.progressAnimationView.setVisibility(View.GONE);
                }
            },3000);


        }



    }

    public void getMostSellingProducts(){

        Call<List<ProductItems>> getNearByProduct=SmartAPI.getApiService().getNearByOrders(session.getJWT(),session.getUserId());
        getNearByProduct.enqueue(new Callback<List<ProductItems>>() {
            @Override
            public void onResponse(Call<List<ProductItems>> call, Response<List<ProductItems>> response) {
                if (response.isSuccessful()){
                    for (ProductItems p:response.body()){

                        productData.add(new ProductItems(p));

                    }
                    myadapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ProductItems>> call, Throwable t) {

            }
        });

    }

    public void getRecommendedProducts(int page_number) {
        if (session.getJWT() != null) {
            Call<List<ProductItems>> getProduct = SmartAPI.getApiService().getProducts(session.getJWT(), page_number);
                    getProduct.enqueue(new Callback<List<ProductItems>>() {
                        @Override
                        public void onResponse(Call<List<ProductItems>> call, Response<List<ProductItems>> response) {
                            if (!response.isSuccessful())
                                Log.d("msg", "unsuccessfull");
                            else {
                                for (ProductItems p : response.body()) {
                                    Log.d("products", p.toString());
                                    listnewsData.add(new ProductItems(p));


                                }

                                myadapter.notifyDataSetChanged();

                            }


                        }

                        @Override
                        public void onFailure(Call<List<ProductItems>> call, Throwable t) {


                            isLoaded=true;
                            Log.e("error", t.getMessage());
                        }
                    });

                }


            }












}













