package com.example.smartpasal.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.ReviewAdapter;
import com.example.smartpasal.databinding.ActivityReviewBinding;
import com.example.smartpasal.model.ReviewResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    private ActivityReviewBinding binding;
    private Session session;
    private ArrayList<ReviewResponse> reviewResponses=new ArrayList<>();
    private ReviewAdapter reviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityReviewBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        session=new Session(getApplicationContext());
        setContentView(view);
        getReviews();
        initRecyclerView();
    }

    private void getReviews() {
        Bundle b=getIntent().getExtras();
        if (b!=null)
        {
            Call<List<ReviewResponse>> getReview= SmartAPI.getApiService().getReviews(session.getJWT(),b.getInt("productId"));
            getReview.enqueue(new Callback<List<ReviewResponse>>() {
                @Override
                public void onResponse(Call<List<ReviewResponse>> call, Response<List<ReviewResponse>> response) {
                    if (response.isSuccessful())
                    {
                        for (ReviewResponse reviewResponse:response.body())
                        {
                            reviewResponses.add(new ReviewResponse(reviewResponse));
                        }
                        reviewAdapter.notifyDataSetChanged();


                    }
                }

                @Override
                public void onFailure(Call<List<ReviewResponse>> call, Throwable t) {

                }
            });
        }

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        reviewAdapter=new ReviewAdapter(reviewResponses,getApplicationContext());
        binding.rvReviews.setLayoutManager(layoutManager);
        binding.rvReviews.setAdapter(reviewAdapter);

    }
}