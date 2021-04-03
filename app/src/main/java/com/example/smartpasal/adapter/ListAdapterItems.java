package com.example.smartpasal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpasal.R;
import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.databinding.LayoutProductListsBinding;
import com.example.smartpasal.model.Carts;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.view.ProductDetails;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListAdapterItems extends RecyclerView.Adapter<ListAdapterItems.Myviewholder> {


    public ArrayList<ProductItems> productItems;
    Context context;

    public ListAdapterItems(ArrayList<ProductItems> productItems, Context context) {
        this.productItems = productItems;
        this.context = context;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Myviewholder(LayoutProductListsBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        final ProductItems currentItem = productItems.get(position);
        String product_name = currentItem.getProductName();
        String marked_price = currentItem.getPrice();
        Integer discount = currentItem.getDiscount();

        holder.binding.tvProductName.setText(product_name);

        if (!currentItem.getRating().equalsIgnoreCase("null"))
        holder.binding.ratingBar.setRating(Float.parseFloat(currentItem.getRating()));

        holder.binding.textViewOptions.setOnClickListener(view -> {
            //creating a popup menu
            PopupMenu popup = new PopupMenu(context, holder.binding.textViewOptions);
            //inflating menu from xml resource
            if (currentItem.getStock() == 0) {
                popup.inflate(R.menu.popup_item_menu_one);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.popup_hate:
                            return true;

                        default:
                            return false;
                    }
                });

            } else {
                popup.inflate(R.menu.popup_item_menu_both);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.popup_add_to_cart:
                            String price = String.valueOf(currentItem.getPrice());
                            addToCart(currentItem.getProductId(), price);
                            return true;
                        case R.id.popup_hate:
                            removeItem(position);
                            return true;
                        default:
                            return false;
                    }
                });


            }

            //displaying the popup
            popup.show();
        });


        holder.binding.tvDiscount.setText("-" + String.valueOf(discount) + "%");
        if (discount > 0) {
            holder.binding.tvDiscount.setVisibility(View.VISIBLE);
            Integer markedPrice = Integer.valueOf(marked_price);
            Integer discountedAmount = markedPrice * discount / 100;
            Integer newPrice = markedPrice - discountedAmount;
            holder.binding.tvDiscountedPrice.setVisibility(View.VISIBLE);
            holder.binding.tvDiscountedPrice.setText("Rs." + String.valueOf(newPrice));
            holder.binding.tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.binding.tvPrice.setText("Rs. " + String.valueOf(markedPrice));
            holder.binding.tvPrice.setTextColor(Color.BLACK);
        } else {
            holder.binding.tvDiscount.setVisibility(View.GONE);
            holder.binding.tvDiscountedPrice.setVisibility(View.GONE);
            holder.binding.tvPrice.setText("Rs. " + marked_price);
            holder.binding.tvPrice.setTextColor(Color.parseColor("#f44336"));
            holder.binding.tvPrice.setTypeface(Typeface.DEFAULT_BOLD);
            holder.binding.tvPrice.setPaintFlags(0);
        }

        if (currentItem.getStock() == 0) {
            holder.binding.tvIsStock.setVisibility(View.VISIBLE);
            holder.binding.ivImg.setImageAlpha(250);
        } else
            holder.binding.tvIsStock.setVisibility(View.GONE);


        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductDetails.class);
            intent.putExtra("product",currentItem);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);


        });


        try {
            String url = SmartAPI.IMG_BASE_URL+currentItem.getPicturePath();

            Picasso.get()
                    .load(url)
                    .fit()
                    .into(holder.binding.ivImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Load", "Successfull");
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
        private LayoutProductListsBinding binding;


        public Myviewholder(LayoutProductListsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }

    public void removeItem(Integer position) {
        productItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productItems.size());

    }

    private void addToCart(Integer product_id, String newPrice) {
        Session session = new Session(context);
        Carts carts = new Carts(session.getUserId(), product_id, newPrice, "", 0f);
        SmartAPI.getApiService().addToCartList(session.getJWT(), carts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    String status = response.getStatus();
                    String message = response.getMessage();
                    if (status.equalsIgnoreCase("200 OK") && message.equalsIgnoreCase("Added to cart"))
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    if (status.equalsIgnoreCase("401 Conflict") && message.equalsIgnoreCase("Item is already in cart"))
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                });

    }


}