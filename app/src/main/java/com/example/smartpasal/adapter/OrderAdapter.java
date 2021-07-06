package com.example.smartpasal.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.R;
import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.model.OrderResponse;
import com.example.smartpasal.model.ReviewResponse;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewholder> {

    private ArrayList<OrderResponse> orderResponses = new ArrayList<>();
    private final Activity activity;
    Boolean visible = false;
    private static final String TAG = "ORDER_ADAPTER";


    public OrderAdapter(ArrayList<OrderResponse> orderResponses, Activity activity) {
        this.orderResponses = orderResponses;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        final OrderResponse orderResponse = orderResponses.get(position);
        holder.tvProductName.setText(orderResponse.getProductName());
        holder.tvOrderedId.setText("Order id: " + orderResponse.getOrderId());
        holder.tvOrderedDate.setText("Order date: " + orderResponse.getOrderedDate());
        holder.tvDiscount.setText("Discount: " + orderResponse.getDiscount() + "%");
        holder.tvQty.setText("Qty: " + orderResponse.getQuantity());
        holder.tvStatus.setText(orderResponse.getStatus());
        String status = orderResponse.getStatus();


        switch (status) {
            case "waiting":
                holder.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_blue, 0, 0, 0);
                holder.ratingLayout.setVisibility(View.GONE);
                holder.tvRateProduct.setVisibility(View.GONE);
                break;
            case "dispatched":
                holder.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_yellow, 0, 0, 0);
                holder.ratingLayout.setVisibility(View.GONE);
                holder.tvRateProduct.setVisibility(View.GONE);
                break;
            case "cancelled":
                holder.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_red, 0, 0, 0);
                holder.ratingLayout.setVisibility(View.GONE);
                holder.tvRateProduct.setVisibility(View.GONE);
                break;
            case "completed":
                holder.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_green, 0, 0, 0);
                getOneReview(orderResponse.getProductId(), holder);
                break;

        }


        holder.tvPrice.setText("Rs. " + orderResponse.getPrice());
        holder.tvDeliveredDate.setText("Delivered date: " + orderResponse.getDeliveredDate());
        holder.tvDeliveryAddress.setText("Delivery address: " + orderResponse.getDeliveryAddress());
        String color = orderResponse.getColor();
        Float size = orderResponse.getSize();
        if (color == null) holder.tvColor.setText("Color: No color option available");
        else holder.tvColor.setText("Color: " + color);
        if (size == null) holder.tvSize.setText("Size: No size option available");
        else holder.tvSize.setText("Size:" + size);

        holder.tvFullDetails.setOnClickListener(view -> {
            if (!visible) {
                holder.hiddenLayout.setVisibility(view.VISIBLE);
                visible = true;
                holder.tvFullDetails.setText("Hide details");
                holder.tvFullDetails.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0);
            } else {
                holder.hiddenLayout.setVisibility(View.GONE);
                visible = false;
                holder.tvFullDetails.setText("See full details");
                holder.tvFullDetails.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);

            }

        });

        try {
            String url = SmartAPI.IMG_BASE_URL+orderResponse.getPicturePath();

            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
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
        return orderResponses.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvDate, tvOrderedId, tvOrderedDate,
                tvDiscount, tvQty, tvPrice, tvStatus, tvFullDetails,
                tvDeliveredDate, tvDeliveryAddress, tvColor, tvSize,
                tvEdit, tvRating, tvRateProduct;
        ImageView ivImg;
        LinearLayout hiddenLayout, ratingLayout;
        RatingBar ratingBar;


        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProduct_Name);
            tvOrderedId = itemView.findViewById(R.id.tvOrderedID);
            tvOrderedDate = itemView.findViewById(R.id.tvOrderedDate);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivImg = itemView.findViewById(R.id.ivImg);
            tvFullDetails = itemView.findViewById(R.id.tvFullDetails);
            hiddenLayout = itemView.findViewById(R.id.hiddenLayout);
            tvDeliveredDate = itemView.findViewById(R.id.tvDeliveredDate);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvRating = itemView.findViewById(R.id.tvRatingMessage);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvEdit = itemView.findViewById(R.id.tvEditRating);
            ratingLayout = itemView.findViewById(R.id.ratingLayout);
            tvRateProduct = itemView.findViewById(R.id.tvRateProduct);
            tvDate = itemView.findViewById(R.id.tvDate);

        }


    }

    private void getOneReview(Integer productId, MyViewholder myViewholder) {
        Session session = new Session(activity);
        myViewholder.tvRateProduct.setVisibility(View.VISIBLE);
        myViewholder.tvRateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddReviewDialog(productId, myViewholder);
            }
        });
        SmartAPI.getApiService().getOneReview(session.getJWT(), productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviewResponse -> {
                    if (reviewResponse.getMessage().length() > 0) {
                        myViewholder.tvRateProduct.setVisibility(View.GONE);
                        myViewholder.ratingLayout.setVisibility(View.VISIBLE);
                        myViewholder.tvRating.setText(reviewResponse.getMessage());
                        myViewholder.ratingBar.setRating(Float.valueOf(reviewResponse.getRating()));
                        myViewholder.tvDate.setText(reviewResponse.getDate());
                        myViewholder.tvEdit.setOnClickListener(view -> showEditReviewDialog(myViewholder, productId));


                    } else {
                        myViewholder.tvRateProduct.setVisibility(View.VISIBLE);
                        myViewholder.tvEdit.setVisibility(View.GONE);
                        myViewholder.ratingLayout.setVisibility(View.GONE);
                    }


                }, throwable -> Log.e(TAG, "getOneReview: " + throwable.getMessage()));

    }

    private void showEditReviewDialog(MyViewholder myViewholder, Integer productId) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(activity, R.style.AlertDialog);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.review_edit_dialog, null);
        alertDialogBuilder.setView(dialogView);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        TextInputLayout etReview = dialogView.findViewById(R.id.etReview);
        etReview.getEditText().setText(myViewholder.tvRating.getText());
        ratingBar.setRating(myViewholder.ratingBar.getRating());

        alertDialogBuilder.setPositiveButton("Send", (dialogInterface, i) -> {
            String review = etReview.getEditText().getText().toString().trim();
            String rating = String.valueOf(ratingBar.getRating());
            updateReview(review, rating, productId, myViewholder);

        })
                .create().show();
    }

    private void showAddReviewDialog(Integer productId, MyViewholder myViewholder) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(activity, R.style.AlertDialog);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.review_add_dialog, null);
        alertDialogBuilder.setView(dialogView);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        TextInputLayout etReview = dialogView.findViewById(R.id.etReview);
        alertDialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String review = etReview.getEditText().getText().toString().trim();
                String rating = String.valueOf(ratingBar.getRating());
                addReview(review, rating, productId, myViewholder);
            }
        })
                .create().show();
    }

    private void addReview(String review, String rating, Integer productId, MyViewholder viewholder) {
        Session session = new Session(activity);
        Date d = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy/MM/dd");
        String date = DateFor.format(d);
        ReviewResponse reviews = new ReviewResponse(session.getUserId(), productId, review, rating, date);
        SmartAPI.getApiService().addReview(session.getJWT(), reviews)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            Toasty.success(activity, response.getMessage()).show();
                            viewholder.ratingLayout.setVisibility(View.VISIBLE);
                            viewholder.tvRateProduct.setVisibility(View.GONE);
                            viewholder.tvRating.setText(review);
                            viewholder.ratingBar.setRating(Float.valueOf(rating));
                            viewholder.tvDate.setText(date);
                            viewholder.tvEdit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showEditReviewDialog(viewholder, productId);

                                }
                            });
                        },
                        throwable -> Log.e(TAG, "addReview: " + throwable.getMessage()));

    }


    private void updateReview(String review, String rating, Integer productId, MyViewholder holder) {
        Session session = new Session(activity);
        Date d = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
        String date = DateFor.format(d);
        ReviewResponse reviews = new ReviewResponse(session.getUserId(), productId, review, rating, date);
        SmartAPI.getApiService().updateReview(session.getJWT(), reviews)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            Toasty.success(activity, response.getMessage()).show();
                            holder.tvRating.setText(review);
                            holder.ratingBar.setRating(Float.valueOf(rating));
                            holder.tvDate.setText(date);
                        },
                        throwable -> Log.e(TAG, "updateReview: " + throwable.getMessage()));


    }


}
