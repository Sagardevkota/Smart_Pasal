package com.example.smartpasal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.R;
import com.example.smartpasal.model.ConversationResponse;
import com.example.smartpasal.model.ReviewResponse;

import java.util.ArrayList;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewholder> {

    private ArrayList<ReviewResponse> reviewResponses;
    private Context context;

    public ReviewAdapter(ArrayList<ReviewResponse> reviewResponses, Context context) {
        this.reviewResponses = reviewResponses;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reviews_and_rating,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        final ReviewResponse reviewResponse=reviewResponses.get(position);
        holder.tvMessage.setText(reviewResponse.getMessage());
        holder.tvAsker.setText(reviewResponse.getUser_name());
        holder.tvDate.setText(reviewResponse.getDate());
        holder.ratingBar.setRating(Float.valueOf(reviewResponse.getRating()));

    }

    @Override
    public int getItemCount() {
        return reviewResponses.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {
        TextView tvMessage,tvAsker,tvDate;
        RatingBar ratingBar;

    public MyViewholder(@NonNull View itemView) {
        super(itemView);
        tvMessage=itemView.findViewById(R.id.tvMessage);
        tvAsker=itemView.findViewById(R.id.tvUser);
        tvDate=itemView.findViewById(R.id.tvAskedDate);
        ratingBar=itemView.findViewById(R.id.ratingBar);
    }
}
}
