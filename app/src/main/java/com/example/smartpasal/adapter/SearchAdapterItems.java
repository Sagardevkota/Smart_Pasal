package com.example.smartpasal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.view.ProductDetails;
import com.example.smartpasal.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapterItems extends RecyclerView.Adapter<SearchAdapterItems.Myviewholder> {
    public ArrayList<ProductItems> productItems = new ArrayList<>();

    Context context;

    public SearchAdapterItems(ArrayList<ProductItems> productItems, Context context) {
        this.context = context;
        this.productItems = productItems;

    }


    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_lists, parent, false);
        return new Myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

        final ProductItems currentItem = productItems.get(position);
        holder.tvProduct_Name.setText(currentItem.getProductName());
        holder.tvMarked_Price.setText("Rs. " + currentItem.getPrice());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductDetails.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("product",currentItem);
            context.startActivity(intent);

        });


        try {
            String url = SmartAPI.IMG_BASE_URL+currentItem.getPicturePath();

            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(holder.ivImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Load", "Successful");

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Load", e.getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }


    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }

    public static class Myviewholder extends RecyclerView.ViewHolder {
        private final TextView tvProduct_Name;
        private TextView tvFixed_Price;
        private final TextView tvMarked_Price;
        private final ImageView ivImg;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            tvProduct_Name =  itemView.findViewById(R.id.tvProduct_Name);
            tvFixed_Price = itemView.findViewById(R.id.tvFixed_Price);
            tvMarked_Price = itemView.findViewById(R.id.tvMarked_Price);
            tvMarked_Price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            ivImg = itemView.findViewById(R.id.ivImg);
        }
    }


}
