package com.example.smartpasal.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.EndlessRecyclerViewScrollListener;
import com.example.smartpasal.adapter.HorizontalListAdapter;
import com.example.smartpasal.adapter.ListAdapterItems;
import com.example.smartpasal.adapter.SliderAdapterExample;
import com.example.smartpasal.databinding.FragmentHomeBinding;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.model.SliderItems;
import com.example.smartpasal.view.categorizedActivity;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;


/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {
    private FragmentHomeBinding binding;
    private Session session;
    private static final String TAG = "HOME";
    private Context context;

    private final ArrayList<ProductItems> recommendedList = new ArrayList<>();
    private final ArrayList<ProductItems> mostSellingList = new ArrayList<>();
    private final ArrayList<ProductItems> hotDealsList = new ArrayList<>();
    private ListAdapterItems recommendedListAdapter;
    private HorizontalListAdapter mostSellingListAdapter, hotDealsListAdapter;
    private GradientDrawable gd;
    public int page_number = 1;
    private int hdealPage = 1;
    private int mSpPage = 1;
    public boolean isLoading = false;
    private int totalCurrentItems = 0;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        session = new Session(context);
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
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();


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

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager mostSellingLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager hotDealsLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        binding.endlessView.setLayoutManager(staggeredGridLayoutManager);
        recommendedListAdapter = new ListAdapterItems(recommendedList, getContext());
        binding.endlessView.setItemAnimator(new SlideInRightAnimator());
        mostSellingListAdapter = new HorizontalListAdapter(mostSellingList, getContext());
        hotDealsListAdapter = new HorizontalListAdapter(hotDealsList, getContext());

        binding.rvMostSelling.setLayoutManager(mostSellingLayoutManager);
        binding.rvHotDeals.setLayoutManager(hotDealsLayoutManager);
        binding.endlessView.setAdapter(recommendedListAdapter);
        binding.rvMostSelling.setAdapter(mostSellingListAdapter);
        binding.rvHotDeals.setAdapter(hotDealsListAdapter);


        Handler mHandler = new Handler();


        Runnable mToastRunnable = new Runnable() {
            @Override
            public void run() {


                Random rnd = new Random();
                int color = Color.argb(153, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                int color1 = Color.argb(155, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                int[] colors = {color, color1};
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

        getMostSellingProducts(mSpPage);

        binding.progressAnimationView.setVisibility(View.VISIBLE);
        getRecommendedProducts(page_number);

        binding.endlessView.setNestedScrollingEnabled(false);

        binding.endlessView.setItemAnimator(new LandingAnimator());
        binding.endlessView.getItemAnimator().setAddDuration(600);

        fetchData(1);

        // Triggered only when new data needs to be appended to the list
        // Add whatever code is needed to append new items to the bottom of the list
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d(TAG, "onLoadMore: "+page);
                fetchData(page);

            }
        };



        fetchHotDeals(hdealPage);

        // Adds the scroll listener to RecyclerView
        binding.endlessView.addOnScrollListener(scrollListener);

        binding.IvHShowMore.setOnClickListener(v1 -> {
            hdealPage++;
            fetchHotDeals(hdealPage);
        });


        binding.IvSpShowMore.setOnClickListener(v1 -> {
            mSpPage++;
            getMostSellingProducts(mSpPage);
        });


        return v;
    }

    private void fetchHotDeals(int page) {
        binding.hProgressBar.setVisibility(View.VISIBLE);
        binding.IvHShowMore.setVisibility(View.GONE);
        SmartAPI.getApiService().getHotDeals(session.getJWT(), page)
                .subscribeOn(Schedulers.io())
                .delay(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productItems -> {
                            int previousItems = hotDealsList.size(); //calculate how many items are there in our list
                            int totalCurrentItems = previousItems + productItems.size(); // add current items + new incoming items
                            hotDealsList.addAll(productItems);
                            binding.hProgressBar.setVisibility(View.GONE);
                            binding.IvHShowMore.setVisibility(View.VISIBLE);
                            hotDealsListAdapter.notifyItemRangeInserted(previousItems, totalCurrentItems);

                        }, throwable -> {
                            Log.e(TAG, "getHotDeals has error: " + throwable.getMessage());
                            binding.hProgressBar.setVisibility(View.GONE);
                            binding.IvHShowMore.setVisibility(View.VISIBLE);
                        }
                );


    }


    private void fetchData(int page) {
        binding.progressAnimationView.setVisibility(View.VISIBLE);
        getRecommendedProducts(page);

    }


    public void getMostSellingProducts(int page) {

        binding.spProgressBar.setVisibility(View.VISIBLE);
        binding.IvSpShowMore.setVisibility(View.GONE);
        SmartAPI.getApiService().getNearByOrders(session.getJWT())
                .subscribeOn(Schedulers.io())
                .delay(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productItems -> {
                            mostSellingList.addAll(productItems);
                            mostSellingListAdapter.notifyItemRangeInserted(0, productItems.size());
                            binding.spProgressBar.setVisibility(View.GONE);
                            binding.IvSpShowMore.setVisibility(View.VISIBLE);

                        }, throwable -> {
                            Log.e(TAG, "getMostSellingProducts has error: " + throwable.getMessage());
                            binding.spProgressBar.setVisibility(View.GONE);
                            binding.IvSpShowMore.setVisibility(View.VISIBLE);
                        }
                );


    }

    public void getRecommendedProducts(int page_number) {

        SmartAPI.getApiService().getProducts(session.getJWT(), page_number)
                .subscribeOn(Schedulers.io())
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productItems -> {
                    int previousItems = recommendedList.size(); //calculate how many items are there in our list
                    totalCurrentItems = previousItems + productItems.size(); // add current items + new incoming items
                    recommendedList.addAll(productItems);
                    recommendedListAdapter.notifyItemRangeInserted(previousItems, totalCurrentItems);
                    isLoading = false;

                }, throwable -> {
                    Log.e(TAG, "getRecommendedProducts: " + throwable.getMessage());
                    isLoading = false;
                    binding.progressAnimationView.setVisibility(View.GONE);
                });


    }


}













