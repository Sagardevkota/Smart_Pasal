package com.example.smartpasal.adapter;

import android.content.Context;

import android.graphics.Paint;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.JsonResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.fragment.home;
import com.example.smartpasal.model.Coupons;
import com.example.smartpasal.model.ProductItems;

import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Myviewholder> {


    public ArrayList<ProductItems> productItems;
    Context context;
    ArrayList<HashMap<String,String>> numCheckboxes=new ArrayList<>();
    TextView tvTotalPrice;



    public CartAdapter(ArrayList<ProductItems> productItems, Context context, TextView tvTotalPrice) {
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

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        final ProductItems currentItem=productItems.get(position);

        holder.tvProduct_Name.setText(currentItem.productName);
        holder.tvMarked_Price.setText("Rs. "+currentItem.marked_price);

        holder.tvFixed_Price.setText("Rs. "+currentItem.fixed_price);
        tvTotalPrice.setText("Rs. "+String.valueOf(getTotal()));

        holder.numberPicker.setOnValueChangedListener((NumberPicker picker, int oldVal, int newVal)->
        {
                Integer TotalPrice = newVal * Integer.valueOf(currentItem.fixed_price);
                updateList(position,currentItem.productName,currentItem.picture_path,String.valueOf(currentItem.productId),currentItem.marked_price,TotalPrice);
                holder.tvFixed_Price.setText("Rs. "+String.valueOf(TotalPrice));
                tvTotalPrice.setText("Rs. "+String.valueOf(getTotal()));

            }
        );

        holder.buApply.setEnabled(false);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()) {

                    HashMap<String, String> keyV = new HashMap<>();
                    keyV.put("Position", String.valueOf(position));
                    keyV.put("Id", String.valueOf(currentItem.productId));



                    numCheckboxes.add(keyV);



                }
                else
                   numCheckboxes.clear();

            }
        });

        holder.etCoupon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(holder.etCoupon.getText().toString().isEmpty())
                   holder. buApply.setEnabled(false);
                else
                {
                   holder.buApply.setEnabled(true);
                }

            }
        });


        holder.buApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String coupon_code=holder.etCoupon.getText().toString();
                applyCoupon(coupon_code,currentItem.getProductId(),holder);
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
        NumberPicker numberPicker;
        EditText etCoupon;
        Button buApply;
        Context context;
        CheckBox checkBox;
        TextView tvCoupon;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);


            tvProduct_Name=itemView.findViewById(R.id.tvProduct_Name);

            tvFixed_Price=itemView.findViewById(R.id.tvFixed_Price);

            tvMarked_Price=itemView.findViewById(R.id.tvMarked_Price);

            tvMarked_Price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvCoupon=itemView.findViewById(R.id.tvCoupon);


            ivImg=itemView.findViewById(R.id.ivImg);
            numberPicker=itemView.findViewById(R.id.num_picker);
            etCoupon=itemView.findViewById(R.id.etCoupon);
            buApply=itemView.findViewById(R.id.buApply);
            checkBox=itemView.findViewById(R.id.checkbox_item);


        }
    }

    public void updateList(Integer position, String tvName, String picture_path ,String user_id,String marked_price,Integer amount){


        productItems.set(position,new ProductItems(tvName,picture_path,Integer.valueOf(user_id),marked_price,String.valueOf(amount)));

    }

    public Integer getTotal(){


        Integer totalPrice=0;


        for (int i = 0; i < productItems.size(); i++) {

            totalPrice+=(Integer.valueOf(productItems.get(i).fixed_price));
        }
        return totalPrice;

    }

    public ArrayList<HashMap<String,String>> getNumCheckBoxes(){

        return numCheckboxes;

    }

    public void applyCoupon(String coupon_code,Integer product_id,Myviewholder myviewholder){
        Coupons coupons=new Coupons(product_id,coupon_code);
       Call<JsonResponse> checkCoupon= SmartAPI.getApiService().checkCoupon(home.jwt,coupons);

       checkCoupon.enqueue(new retrofit2.Callback<JsonResponse>() {
           @Override
           public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
               if (!response.isSuccessful())
                   Log.d("unsuccess","unsuccess");
               else{
                   String status=response.body().getStatus();
                           String message=response.body().getMessage();
                           if (status.equalsIgnoreCase("200 OK")){
                               Integer discount=Integer.valueOf(message);

                               myviewholder.tvCoupon.setText("Discount applied of " + discount +"%");
                               myviewholder.tvCoupon.setVisibility(View.VISIBLE);

                           }
                           else
                           {
                               myviewholder.tvCoupon.setText(message);
                               myviewholder.tvCoupon.setVisibility(View.VISIBLE);
                           }
               }
           }

           @Override
           public void onFailure(Call<JsonResponse> call, Throwable t) {

           }
       });



    }


}
