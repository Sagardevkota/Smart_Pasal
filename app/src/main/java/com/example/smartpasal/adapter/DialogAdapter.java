package com.example.smartpasal.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.model.Checkout;
import com.example.smartpasal.model.Orders;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.MyViewholder> {

    List<Orders> orders = new ArrayList<>();
    Context context;

    public DialogAdapter(List<Orders> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dialog_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        final Orders order = orders.get(position);
        holder.tvProductName.setText(String.valueOf(order.getProductName()));
        String color = order.getProductColor();
        if (color.length() > 2)
            holder.tvColor.setText("Color: " + order.getProductColor());
        else
            holder.tvColor.setText("Color: Not available");
        if (order.getProductSize() > 0)
            holder.tvSize.setText("Size: " + String.valueOf(order.getProductSize()));
        else
            holder.tvSize.setText("Size: Not available");

        holder.tvQty.setText("Qty: " + String.valueOf(order.getQuantity()));

        int markedPrice = order.getPrice();
        int discountedAmount = markedPrice * order.getDiscount() / 100;
        int newPrice = markedPrice - discountedAmount;
        holder.tvDiscountedPrice.setText("Rs. " + newPrice);
        holder.tvPrice.setText("Rs. "+markedPrice);
        holder.tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvDiscount.setText("-"+order.getDiscount()+"%");

        if (markedPrice==newPrice)
            holder.tvPrice.setVisibility(View.GONE);

        try {
            String url = SmartAPI.IMG_BASE_URL+order.getPicturePath();

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
        return orders.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {

        TextView tvProductName, tvDiscountedPrice, tvDiscount;
        TextView tvPrice;
        TextView tvSize;
        TextView tvColor;
        TextView tvQty;
        ImageView ivImg;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProduct_Name);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSize = itemView.findViewById(R.id.tvProductSize);
            tvColor = itemView.findViewById(R.id.tvProductColor);
            tvQty = itemView.findViewById(R.id.tvQty);
            ivImg = itemView.findViewById(R.id.ivImg);
            tvDiscountedPrice = itemView.findViewById(R.id.tvDiscountedPrice);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);

        }
    }

    public Integer getTotal() {
        int price = 0;
        for (Orders c : orders) {
            int markedPrice = c.getPrice();
            int discount = c.getDiscount();
            int discountedAmount = markedPrice * discount / 100;
            Integer newPrice = markedPrice - discountedAmount;
            price += newPrice * c.getQuantity();
        }
        return price;
    }
}
