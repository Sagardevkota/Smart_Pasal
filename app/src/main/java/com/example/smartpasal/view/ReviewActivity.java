package com.example.smartpasal.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.smartpasal.R;
import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.ReviewAdapter;
import com.example.smartpasal.databinding.ActivityReviewBinding;
import com.example.smartpasal.model.ReviewResponse;
import java.util.ArrayList;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReviewActivity extends AppCompatActivity {
    private static final String TAG = "REVIEW_ACTIVITY";
    private ActivityReviewBinding binding;
    private Session session;
    private final ArrayList<ReviewResponse> reviewResponseList = new ArrayList<>();
    private ReviewAdapter reviewAdapter;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.fragment_fade_enter, R.anim.slide_out_right);
            return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        session = new Session(getApplicationContext());
        setContentView(view);
        getReviews();
        initRecyclerView();
    }

    private void getReviews() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            SmartAPI.getApiService().getReviews(session.getJWT(), b.getInt("productId"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(reviewResponses -> {
                        reviewResponseList.addAll(reviewResponses);
                        reviewAdapter.notifyItemRangeInserted(0,reviewResponses.size());

                    }, throwable -> Log.e(TAG, "getReviews: " + throwable.getMessage()));

        }

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        reviewAdapter = new ReviewAdapter(reviewResponseList, getApplicationContext());
        binding.rvReviews.setLayoutManager(layoutManager);
        binding.rvReviews.setAdapter(reviewAdapter);

    }
}