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

import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.view.ProductDetails;
import com.example.smartpasal.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapterItems extends RecyclerView.Adapter<SearchAdapterItems.Myviewholder> {
    public ArrayList<ProductItems> productItems=new ArrayList<>();

    Context context;

    public  SearchAdapterItems(ArrayList<ProductItems> productItems,Context context){
        this.context=context;
        this.productItems=productItems;

    }





    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_lists,parent,false);
        Myviewholder view=new Myviewholder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

        final ProductItems currentItem=productItems.get(position);

        holder.tvProduct_Name.setText(currentItem.getProductName());
        holder.tvMarked_Price.setText("Rs. "+currentItem.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("product_id",currentItem.getProductId());

                intent.putExtra("product_name",currentItem.getProductName() );
                intent.putExtra("price",currentItem.getPrice());

                intent.putExtra("product_photo",currentItem.getPicture_path());
                intent.putExtra("brand",currentItem.getBrand());
                intent.putExtra("sku",currentItem.getSku());
                intent.putExtra("desc",currentItem.getDesc());
                intent.putExtra("rating",currentItem.getRating());
                intent.putExtra("category",currentItem.getCategory());
                intent.putExtra("type",currentItem.getType());
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
    public class Myviewholder extends RecyclerView.ViewHolder   {
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
