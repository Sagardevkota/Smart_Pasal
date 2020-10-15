package com.example.smartpasal.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.R;
import com.example.smartpasal.model.Checkout;
import com.example.smartpasal.model.Orders;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.MyViewholder> {

   ArrayList<Checkout> orders=new ArrayList<>();
    Context context;

    public DialogAdapter(ArrayList<Checkout> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dialog_order_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        final Checkout order=orders.get(position);
        holder.tvProductName.setText(String.valueOf(order.getProductName()));
       String color=order.getProductColor();
        if (order.getProductColor().length()>2)
           holder.tvColor.setText("Color: "+order.getProductColor());
        else
            holder.tvColor.setText("Color: Not available");
        if (order.getProductSize()>0)
            holder.tvSize.setText("Size: "+String.valueOf(order.getProductSize()));
        else
            holder.tvSize.setText("Size: Not available");
        holder.tvPrice.setText("Rs. "+String.valueOf(order.getPrice()));
        holder.tvQty.setText("Qty: "+String.valueOf(order.getQty()));

        try{
            String url=order.getProductImage();

            Picasso.get()
                    .load(url)
                    .fit()
                    .into(holder.ivImg, new Callback() {
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


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder{

        TextView tvProductName;
        TextView tvPrice;
        TextView tvSize;
        TextView tvColor;
        TextView tvQty;
        ImageView ivImg;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tvProductName=itemView.findViewById(R.id.tvProduct_Name);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvSize=itemView.findViewById(R.id.tvProductSize);
            tvColor=itemView.findViewById(R.id.tvProductColor);
            tvQty=itemView.findViewById(R.id.tvQty);
            ivImg=itemView.findViewById(R.id.ivImg);

        }
    }

    public Integer getTotal(){
        Integer price=0;
        for (Checkout c:orders){
            price+=c.getPrice();
        }
        return price;
    }
}
