package com.example.smartpasal;

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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapterItems extends RecyclerView.Adapter<ListAdapterItems.Myviewholder> {


    public ArrayList<ProductItems> productItems;
    Context context;


    public ListAdapterItems(ArrayList<ProductItems> productItems,Context context) {
        this.productItems=productItems;
        this.context=context;
    }



    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_lists, parent, false);
        Myviewholder view = new Myviewholder(v);
        return view;

    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        final ProductItems currentItem=productItems.get(position);

        holder.tvProduct_Name.setText(currentItem.tvName);
        holder.tvMarked_Price.setText("Rs. "+currentItem.marked_price);

        holder.tvFixed_Price.setText("Rs. "+currentItem.fixed_price);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ProductDetails.class);

                intent.putExtra("product_name",currentItem.tvName );
                intent.putExtra("fixed_price",currentItem.fixed_price);
                intent.putExtra("marked_price",currentItem.marked_price);
                intent.putExtra("product_photo",currentItem.picture_path);
                intent.putExtra("brand",currentItem.brand);
                intent.putExtra("sku",currentItem.sku);
                intent.putExtra("desc",currentItem.desc);
                context.startActivity(intent);


            }
        });







        try{
            String url=currentItem.getPicture_path();

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
        return productItems.size();
    }
    public static class Myviewholder extends RecyclerView.ViewHolder {
        TextView tvProduct_Name;
        TextView tvFixed_Price;
        TextView tvMarked_Price;
        ImageView ivImg;
        Context context;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);


            tvProduct_Name=( TextView)itemView.findViewById(R.id.tvProduct_Name);

            tvFixed_Price=( TextView)itemView.findViewById(R.id.tvFixed_Price);

            tvMarked_Price=( TextView)itemView.findViewById(R.id.tvMarked_Price);

            tvMarked_Price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);


            ivImg=(ImageView)itemView.findViewById(R.id.ivImg);


        }
    }

}