package com.example.smartpasal.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.R;
import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.CartAdapter;
import com.example.smartpasal.adapter.DialogAdapter;
import com.example.smartpasal.model.OrderWrapper;
import com.example.smartpasal.databinding.ActivityCartBinding;
import com.example.smartpasal.model.CartResponse;
import com.example.smartpasal.model.Carts;
import com.example.smartpasal.model.Orders;
import com.example.smartpasal.util.CartUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;


public class cartActivity extends AppCompatActivity {

    private static final String TAG = "CART_ACTIVITY";
    private ActivityCartBinding cartBinding;
    private Session session;
    private CartAdapter cartAdapter;
    private final ArrayList<CartResponse> cartList = new ArrayList<>();
    private final CartUtil cartUtil = new CartUtil();
    private final List<Orders> checkoutList = new ArrayList<>();
    private DialogAdapter dialogAdapter;

    private int grandTotalPrice = 0;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.fragment_fade_enter, R.anim.slide_out_right);
            return super.onOptionsItemSelected(item);
        }
        if (item.getItemId() == R.id.menu_delete) {
            Log.i(TAG, "onOptionsItemSelected: " + cartUtil.getItemPositionMap().toString());
            if (cartUtil.getItemPositionMap().size() == 0) {
                Toasty.warning(getApplicationContext(), "No item selected to delete").show();
                return super.onOptionsItemSelected(item);
            }
            Log.i(TAG, "onOptionsItemSelected: " + cartUtil.getItemPositionMap());

            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
            alertDialogBuilder.setMessage("Do you want to remove the item from cart?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        for (Map.Entry<Integer, Integer> entry : cartUtil.getItemPositionMap().entrySet()) {
                            int productId = entry.getKey();
                            int position = entry.getValue();
                            removeFromCart(productId);
                        }


                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show();


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartBinding = ActivityCartBinding.inflate(getLayoutInflater());
        View view = cartBinding.getRoot();
        setContentView(view);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        session = new Session(getApplicationContext());

        //Cart listview
        initRecyclerView();

        //get Cart List
        getCartItems();

        //listener
        cartBinding.buContinueShopping.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        cartBinding.buCheckOut.setOnClickListener(v -> createCheckOutDialog());

    }

    private void createCheckOutDialog() {
        //no item checked
        if (cartUtil.getItemPositionMap().size() == 0) {
            Toasty.warning(getApplicationContext(), "Nothing selected to checkout").show();
            return;
        }
        initCheckOutDialogView();

    }

    private void initCheckOutDialogView() {
        checkoutList.clear();
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.order_dialog_context);
        dialog.create();

        RecyclerView rvDialog = dialog.findViewById(R.id.rvDialog);
        TextView tvTotal = dialog.findViewById(R.id.tvTotal);
        TextView tvSubTotal = dialog.findViewById(R.id.SubTotal);
        TextView tvDeliveryAddrs = dialog.findViewById(R.id.tvDeliveryAddress);
        TextView tvDiscountApplied = dialog.findViewById(R.id.tvDiscountApplied);
        Button buCancel = dialog.findViewById(R.id.buCancel);
        Button buContinue = dialog.findViewById(R.id.buContinue);
        TextInputLayout etCoupon = dialog.findViewById(R.id.etCoupon);
        Button buApply = dialog.findViewById(R.id.buApply);
        Session session = new Session(cartActivity.this);
        tvDeliveryAddrs.setText(session.getAddress());

        buCancel.setOnClickListener(v -> dialog.dismiss());
        buContinue.setOnClickListener(v -> showPaymentOptionDialog());


        Log.i(TAG, "initCheckOutDialogView: "+cartUtil.getOrderList());
        checkoutList.addAll(cartUtil.getOrderList());

        etCoupon.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) buApply.setEnabled(true);
            }
        });

        dialogAdapter = new DialogAdapter(checkoutList, getApplicationContext());


        rvDialog.setLayoutManager(new LinearLayoutManager(cartActivity.this));
        grandTotalPrice = dialogAdapter.getTotal();
        buApply.setOnClickListener(view -> {
            String coupon_code = etCoupon.getEditText().getText().toString().trim();
            Integer discount = applyCoupon(coupon_code);
            Integer totalPrice = dialogAdapter.getTotal();
            Integer CouponedAmount = totalPrice * discount / 100;
            int PriceAfterCoupon = totalPrice - CouponedAmount;
            tvTotal.setText("Rs. " + PriceAfterCoupon);
            if (discount != 0) {
                tvDiscountApplied.setVisibility(View.VISIBLE);
                tvDiscountApplied.setText(discount + "% Discount applied after promo code");
                grandTotalPrice = PriceAfterCoupon;
            } else tvDiscountApplied.setText("Invalid Coupon Code");

        });


        tvTotal.setText("Rs. " + grandTotalPrice);
        tvSubTotal.setText("SubTotal(" + checkoutList.size() + " items)");
        rvDialog.setAdapter(dialogAdapter);
        dialog.create();
        dialog.show();

    }

    private void showPaymentOptionDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.dialog_payment_option);
        dialog.create();
        EditText etDeliveryAddress = dialog.findViewById(R.id.etDelivery);
        etDeliveryAddress.setText(session.getAddress());
        dialog.findViewById(R.id.buCancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.findViewById(R.id.buContinue).setOnClickListener(v -> {

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("Checkout?")
                    .setMessage("Please check everything before pressing Yes")
                    .setPositiveButton("Yes", (dialog12, which) -> postOrders(etDeliveryAddress.getText().toString()))
                    .setNegativeButton("No", (dialog1, which) -> dialog1.dismiss())
                    .show();

        });
        dialog.show();

    }

    private void postOrders(String deliveryAddress) {
        showProgressDialog("Checking Out");

        int total = dialogAdapter.getTotal();
        int discount = total - grandTotalPrice;
        int discountPerItem = discount / checkoutList.size();

        checkoutList.stream().forEach(orders -> {
            String pattern = "MM-dd-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
            String date = simpleDateFormat.format(new Date());
            orders.setOrderedDate(date);//set today's date
            orders.setDeliveryAddress(deliveryAddress);//if user selects delivery address different
            int finalPrice = orders.getPrice() - discountPerItem;
            orders.setPrice(finalPrice); //discount for every item from overall discount

            //clear the items from cart
            removeFromCart(orders.getProductId());

        });

        OrderWrapper orderWrapper = new OrderWrapper();
        orderWrapper.setOrders(checkoutList);

        SmartAPI.getApiService().checkOut(session.getJWT(),orderWrapper)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(4, TimeUnit.MINUTES)
                .subscribe(response -> {
                    if (response.getStatus().equalsIgnoreCase("200 OK"))
                    {
                        Toasty.success(getApplicationContext(),response.getMessage(),Toasty.LENGTH_SHORT).show();
                        hideProgressDialog();
                        showFinishingDialog();
                    }
                },throwable -> Log.e(TAG, "postOrders: "+throwable.getMessage() ));


    }

    private void showFinishingDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.placed_order_dialog);
        dialog.create();
        dialog.findViewById(R.id.tvContinueShopping).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        dialog.show();

    }


    private int applyCoupon(String coupon_code) {

        if (coupon_code.equalsIgnoreCase("GCES2016"))
            return 15;
        else
            return 0;
//        SmartAPI.getApiService().checkCoupon(session.getJWT(), coupons)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//
//                    String status = response.getStatus();
//                    String message = response.getMessage();
//                    if (status.equalsIgnoreCase("200 OK")) {
//                        Integer discount = Integer.valueOf(message);
//                        Integer CouponedAmount = newPrice * discount / 100;
//                        PriceAfterCoupon = newPrice - CouponedAmount;
//                        binding.tvPrice.setTextColor(Color.BLACK);
//                        binding.tvCouponPrice.setText("Rs." + String.valueOf(PriceAfterCoupon));
//                        binding.tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                        binding.tvDiscountedPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                        binding.tvCouponPrice.setVisibility(View.VISIBLE);
//                        binding.tvIsCoupon.setText("Discount applied of " + message + " %");
//                    } else {
//                        binding.tvIsCoupon.setVisibility(View.VISIBLE);
//                        binding.tvIsCoupon.setText("Invalid coupon code");
//                        binding.tvCouponPrice.setVisibility(View.GONE);
//                        PriceAfterCoupon = newPrice;
//                        binding.tvPrice.setTextColor(Color.parseColor("#ff5252"));
//                        binding.tvPrice.setPaintFlags(0);
//                    }
//
//                }, throwable -> Log.e(TAG, "applyCoupon: " + throwable.getMessage()));


    }

    private void initRecyclerView() {
        cartAdapter = new CartAdapter(cartList, cartActivity.this, cartUtil, cartBinding.tvTotalPrice);
        cartBinding.rvCart.setAdapter(cartAdapter);
        cartBinding.rvCart.setItemAnimator(new SlideInRightAnimator());
        cartBinding.rvCart.getItemAnimator().setAddDuration(300);
        cartBinding.rvCart.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }


    public void getCartItems() {
        cartList.clear();
        SmartAPI.getApiService().getCartList(session.getJWT())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartResponses -> {
                            if (cartResponses.size() == 0) {
                                cartBinding.noItemLinearLayout.setVisibility(View.VISIBLE);
                                return;
                            }
                            for (CartResponse cartResponse : cartResponses) {
                                if (cartResponse.getStock() != 0) //check if the cart item has stock
                                    cartList.add(cartResponse);
                                else {
                                    removeFromCart(cartResponse.getProductId());  //if item is not in stock remove from cart
                                    Snackbar
                                            .make(cartBinding.rootLayout, "Some of the items were removed due to out of stock ", Snackbar.LENGTH_INDEFINITE)
                                            .setAction("OK", view -> {
                                            }).show();

                                }

                            }
                            cartAdapter.notifyItemRangeInserted(0, cartResponses.size());
                            session.setBadgeCount(cartResponses.size());
                            hideProgressDialog();
                        },
                        throwable -> Log.e(TAG, "getCartItems: " + throwable.getMessage()));

    }


    public void removeFromCart(int productId) {

        Carts cart = new Carts(session.getUserId(), productId);
        SmartAPI.getApiService().removeFromCart(session.getJWT(), cart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            String status = response.getStatus();
                            String message = response.getMessage();
                            if (status.equalsIgnoreCase("200 OK") && message.equalsIgnoreCase("Item is removed from cart")) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                cartAdapter.removeData(productId);
                                if (cartAdapter.getItemCount() == 0)
                                    cartBinding.noItemLinearLayout.setVisibility(View.VISIBLE);
                            }
                        },
                        throwable -> Log.e(TAG, "removeFromCart: " + throwable.getMessage()));


    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.AlertDialog);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


}
