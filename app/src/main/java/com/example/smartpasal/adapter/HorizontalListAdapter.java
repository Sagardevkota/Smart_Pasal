package com.example.smartpasal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.view.ProductDetails;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.Myviewholder> {

    public ArrayList<ProductItems> productItems;
    Context context;


    public HorizontalListAdapter(ArrayList<ProductItems> productItems,Context context) {
        this.productItems= productItems;
        this.context=context;
    }



    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_horizontal_list, parent, false);
            return new Myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        final ProductItems currentItem=productItems.get(position);
        String product_name=currentItem.getProductName();
        String marked_price=currentItem.getPrice();
        Integer discount=currentItem.getDiscount();

        holder.tvProduct_Name.setText(product_name);
        if (currentItem.getDiscount()>0)
        {
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvDiscount.setText("-"+String.valueOf(discount)+"%");
            Integer markedPrice=Integer.valueOf(marked_price);
            Integer discountedAmount=markedPrice*discount/100;
            Integer newPrice=markedPrice-discountedAmount;
            holder.tvFixed_Price.setText("Rs. "+newPrice);
            holder.tvMarked_Price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvMarked_Price.setText("Rs. "+marked_price);

        }

        else
        {
            holder.tvDiscount.setVisibility(View.GONE);
            holder.tvFixed_Price.setVisibility(View.GONE);
            holder.tvMarked_Price.setText("Rs. "+marked_price);
            holder.tvMarked_Price.setPaintFlags(0);
            holder.tvMarked_Price.setTextColor(Color.parseColor("#ff5252"));
            holder.tvMarked_Price.setTypeface(Typeface.DEFAULT_BOLD);
        }



        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, ProductDetails.class);
            intent.putExtra("product",currentItem);

            context.startActivity(intent);


        });


        try{
            String url= SmartAPI.IMG_BASE_URL+currentItem.getPicturePath();

            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
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
        return productItems.size();
    }
    public static class Myviewholder extends RecyclerView.ViewHolder {
        TextView tvProduct_Name;
        TextView tvFixed_Price;
        TextView tvMarked_Price;
        ImageView ivImg;
        TextView tvDiscount;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);

            tvProduct_Name=itemView.findViewById(R.id.tvProduct_Name);
            tvFixed_Price=itemView.findViewById(R.id.tvFixed_Price);
            tvMarked_Price=itemView.findViewById(R.id.tvMarked_Price);
            tvDiscount=itemView.findViewById(R.id.tvDiscount);
            tvMarked_Price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            ivImg=itemView.findViewById(R.id.ivImg);
        }
    }

}