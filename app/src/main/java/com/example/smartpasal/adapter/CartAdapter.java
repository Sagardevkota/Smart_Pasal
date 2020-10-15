package com.example.smartpasal.adapter;

import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.R;

import com.example.smartpasal.Session.Session;
import com.example.smartpasal.model.CartResponse;
import com.example.smartpasal.model.Checkout;


import com.example.smartpasal.view.cartActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Myviewholder> {


    public ArrayList<CartResponse> productItems;
    Context context;
    ArrayList<HashMap<String,String>> numCheckboxes=new ArrayList<>();
    TextView tvTotalPrice;
    ArrayList<Checkout> orders=new ArrayList<>();



    public CartAdapter(ArrayList<CartResponse> productItems, Context context, TextView tvTotalPrice) {
        this.productItems=productItems;
        this.context=context;
        this.tvTotalPrice=tvTotalPrice;
    }



    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_lists, parent, false);
        Myviewholder view = new Myviewholder(v);
        return view;

    }

    private void setOrders(CartResponse c){
        orders.add(
                new Checkout(
                        c.getProductName(),
                        c.getProductId(),
                        c.getColor(),
                        c.getSize(),
                        Integer.valueOf(c.getPrice()),
                       1,
                        c.getPicture_path()

                )
        );

    }

    public void updateOrders(Integer position,CartResponse c,Integer totaPrice,Integer qty){
        orders.set(position,new Checkout(
                c.getProductName(),
                c.getProductId(),
                c.getColor(),
                c.getSize(),
                totaPrice,
                qty,
                c.getPicture_path()
        ));
    }

    public ArrayList<Checkout> getOrders(){
        return  orders;
    }

    private void updateList(Integer position,CartResponse c){
        productItems.set(position,c);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        final CartResponse currentItem=productItems.get(position);
        String product_name=currentItem.getProductName();
        String price=currentItem.getPrice();
        Integer discount=currentItem.getDiscount();
        holder.tvProduct_Name.setText(product_name);
        String size=String.valueOf(currentItem.getSize());
        String color=currentItem.getColor();
//        if (size.equalsIgnoreCase("0.0"))
//            holder.tvSize.setVisibility(View.GONE);
//        else  holder.tvSize.setText("Size:  "+size);


        holder.tvPrice.setText("Rs. "+price);
        tvTotalPrice.setText("Rs. "+String.valueOf(getTotal()));

        CartResponse orderCart=new CartResponse(
                currentItem.getProductId(),
                currentItem.getProductName(),
                currentItem.getDesc(),
                String.valueOf(currentItem.getPrice()),
                currentItem.getCategory(),
                currentItem.getBrand(),
                currentItem.getSku(),
                currentItem.getType(),
                currentItem.getPicture_path(),
                currentItem.getDiscount(),
                currentItem.getStock(),
                currentItem.getColor(),
                currentItem.getSize()

        );

        holder.tvStock.setText("Only "+currentItem.getStock()+" item(s) available");
        holder.numberPicker.setMaxValue(currentItem.getStock());
        if (currentItem.getStock()==0)
        {
            Session session=new Session(context);
            holder.checkBox.setEnabled(false);
            holder.tvStock.setText("Sorry this product is out of stock");
            MaterialAlertDialogBuilder alertDialogBuilder=new MaterialAlertDialogBuilder(context,R.style.AlertDialog);
            alertDialogBuilder.setTitle("Confirmation")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toasty.success(context,String.valueOf(currentItem.getProductName())).show();
                            new cartActivity().removeFromCart(context,session,String.valueOf(currentItem.getProductId()));

                        }
                    })
                    .setMessage("There are item(s) in your cart that is not in stock for now.So we are removing the item")

                    .setCancelable(false)
                    .create()

                    .show();


        }



        setOrders(orderCart);






        holder.numberPicker.setOnValueChangedListener((NumberPicker picker, int oldVal, int newVal)->
        {
            Integer prices=Integer.valueOf(price);
                Integer TotalPrice = newVal * prices;
                CartResponse cartResponse=new CartResponse(
                        currentItem.getProductId(),
                        currentItem.getProductName(),
                        currentItem.getDesc(),
                        String.valueOf(TotalPrice),
                        currentItem.getCategory(),
                        currentItem.getBrand(),
                        currentItem.getSku(),
                        currentItem.getType(),
                        currentItem.getPicture_path(),
                        currentItem.getDiscount(),
                        currentItem.getStock(),
                        currentItem.getColor(),
                        currentItem.getSize()

                );
                updateList(position,cartResponse);
                updateOrders(position,cartResponse,TotalPrice,newVal);

                tvTotalPrice.setText("Rs. "+String.valueOf(getTotal()));
                holder.tvPrice.setText("Rs. "+String.valueOf(TotalPrice));

            }
        );


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()) {
                    HashMap<String, String> keyV = new HashMap<>();
                    keyV.put("Position", String.valueOf(position));
                    keyV.put("Id", String.valueOf(currentItem.getProductId()));
                    numCheckboxes.add(keyV);
                }
                else if(!compoundButton.isChecked()) {
                        numCheckboxes.remove(numCheckboxes.size()-1);

                }

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
        TextView tvProduct_Name,tvPrice,tvSize,tvDiscount,tvStock;
        ImageView ivImg;
        NumberPicker numberPicker;
        EditText etCoupon;
        Button buApply;
        CheckBox checkBox;
        LinearLayout itemLayout;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            tvProduct_Name=itemView.findViewById(R.id.tvProduct_Name);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvStock=itemView.findViewById(R.id.tvStock);
            ivImg=itemView.findViewById(R.id.ivImg);
            numberPicker=itemView.findViewById(R.id.num_picker);
            etCoupon=itemView.findViewById(R.id.etCoupon);
            buApply=itemView.findViewById(R.id.buApply);
            checkBox=itemView.findViewById(R.id.checkbox_item);
            tvSize=itemView.findViewById(R.id.tvSize);
            tvDiscount=itemView.findViewById(R.id.tvDiscount);
        }
    }


    public Integer getTotal(){

        Integer totalPrice=0;
        for (int i = 0; i < productItems.size(); i++) {
            totalPrice+=(Integer.parseInt(productItems.get(i).getPrice()));
        }
        return totalPrice;

    }

    public ArrayList<HashMap<String,String>> getNumCheckBoxes(){
        return numCheckboxes;
    }

    public void removeItem(Integer position){
        productItems.remove(position);
        notifyItemRangeChanged(position,productItems.size());
    }







}
