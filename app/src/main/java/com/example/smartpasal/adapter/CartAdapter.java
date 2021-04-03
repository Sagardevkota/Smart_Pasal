package com.example.smartpasal.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.R;
import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.model.CartResponse;
import com.example.smartpasal.model.Orders;
import com.example.smartpasal.util.CartUtil;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Myviewholder> {

    private final List<CartResponse> productItems;
    private final Context context;
    private final CartUtil cartUtil;
    private final TextView tvTotalPrice;


    public CartAdapter(List<CartResponse> productItems, Context context, CartUtil cartUtil, TextView tvTotalPrice) {
        this.productItems = productItems;
        this.context = context;
        this.cartUtil = cartUtil;
        this.tvTotalPrice = tvTotalPrice;
    }


    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_lists, parent, false);
        return new Myviewholder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        final CartResponse currentItem = productItems.get(position);
        Session session = new Session(context);
        int productId = currentItem.getProductId();
        String productName = currentItem.getProductName();

        String price = currentItem.getPrice();
        String picturePath = currentItem.getPicturePath();
        Integer discount = currentItem.getDiscount();
        Float size = currentItem.getSize();
        String color = currentItem.getColor();

        //first add order to list with initial quantity 1
        Orders orders = Orders.builder()
                .productId(productId)
                .productName(productName)
                .productSize(size)
                .price(Integer.parseInt(price))
                .productColor(color)
                .deliveredDate("Not delivered yet")
                .orderedDate(new Date().toString())
                .picturePath(picturePath)
                .quantity(1)
                .discount(discount)
                .status("Waiting")
                .userId(session.getUserId())
                .deliveryAddress(session.getAddress())
                .build();

        cartUtil.addOrders(orders);


        //set views
        holder.tvProduct_Name.setText(productName);
        holder.tvPrice.setText("Rs. " + price);
        holder.tvStock.setText("Only " + currentItem.getStock() + " item(s) available");
        holder.tvDiscountApplied.setText("Discount Applied of " + discount + "%");


        Integer markedPrice = Integer.valueOf(price);
        Integer discountedAmount = markedPrice * discount / 100;
        int newPrice = markedPrice - discountedAmount;

        holder.tvDiscountedPrice.setText("Rs. " + newPrice);
        holder.tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.numberPicker.setMaxValue(currentItem.getStock());
        if (markedPrice.equals(newPrice))
            holder.tvPrice.setVisibility(View.GONE);
        updateTextView();


        holder.numberPicker.setOnValueChangedListener((NumberPicker picker, int oldVal, int newVal) -> {
                    cartUtil.updateOrders(productId, newVal);
                    updateTextView();
                    int totalPrice = newPrice * newVal;
                    holder.tvDiscountedPrice.setText("Rs. " + totalPrice);

                }

        );


        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {

            if (compoundButton.isChecked()) {
                cartUtil.putItem(productId, holder.getAbsoluteAdapterPosition());
            } else if (!compoundButton.isChecked()) {
                cartUtil.removeItemFromMap(productId);
                cartUtil.removeOrderFromList(productId);
            }

        });

        try {
            String url = SmartAPI.IMG_BASE_URL+currentItem.getPicturePath();

            Picasso.get()
                    .load(url)
                    .fit()
                    .into(holder.ivImg, new Callback() {
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
        private final TextView tvProduct_Name, tvPrice, tvSize, tvDiscountApplied, tvStock, tvDiscountedPrice;
        private final ImageView ivImg;
        private final NumberPicker numberPicker;
        private final CheckBox checkBox;


        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            tvProduct_Name = itemView.findViewById(R.id.tvProduct_Name);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStock = itemView.findViewById(R.id.tvStock);
            ivImg = itemView.findViewById(R.id.ivImg);
            numberPicker = itemView.findViewById(R.id.num_picker);
            checkBox = itemView.findViewById(R.id.checkbox_item);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvDiscountApplied = itemView.findViewById(R.id.tvDiscountApplied);
            tvDiscountedPrice = itemView.findViewById(R.id.tvDiscountedPrice);


        }
    }


    public void updateTextView() {
        tvTotalPrice.setText("Rs. " + cartUtil.getTotalPrice());
    }

    public void removeData(Integer productId) {
        for (int position = 0; position < productItems.size(); ++position) {//loop through position
            if (productItems.get(position).getProductId() == productId) {//find the position which have given product id
                productItems.remove(position);
                cartUtil.removeItemFromMap(productId);
                cartUtil.removeOrderFromList(productId);
                notifyItemRemoved(position);
                break;
            }
        }
        updateTextView();
    }

}
