package com.example.smartpasal.view;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import android.view.View;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.JsonResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.CartAdapter;
import com.example.smartpasal.adapter.DialogAdapter;
import com.example.smartpasal.model.CartResponse;
import com.example.smartpasal.model.Carts;
import com.example.smartpasal.model.Checkout;
import com.example.smartpasal.model.Orders;
import com.example.smartpasal.Session.Session;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class cartActivity extends AppCompatActivity {

    CartAdapter myadapter;
    ArrayList<CartResponse> listnewsData = new ArrayList<>();
    RecyclerView lvlist;
    private Session session;

    TextView tvEmptyCart;
    SharedPreferences sp;
    TextView tvTotalPrice;
    Integer position = 0;
    ArrayList<Checkout> checkoutList=new ArrayList<>();
    private String deliveryAddress="";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                if (myadapter.getNumCheckBoxes().size() == 0)
                    Toast.makeText(getApplicationContext(), "Please select any item to delete", Toast.LENGTH_SHORT).show();
                else {
                    Log.d("dataArray", myadapter.getNumCheckBoxes().toString());

                    MaterialAlertDialogBuilder alertDialogBuilder=new MaterialAlertDialogBuilder(this,R.style.AlertDialog);
                           alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int count) {

                                    for (int i = 0; i < myadapter.getNumCheckBoxes().size(); i++) {
                                        String productId = myadapter.getNumCheckBoxes().get(i).get("Id");
                                        position = Integer.parseInt(myadapter.getNumCheckBoxes().get(i).get("Position"));
                                        Log.d("dataArray", "item is " + productId + " at " + String.valueOf(position));
                                        showProgressDialog("Removing from cart");
                                        removeFromCart(productId);
                                        hideProgressDialog();
                                    }



                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();

                                }
                            })
                            .setTitle("Confirmation")
                            .setMessage("Do you want to delete item(s)")
                            .create().show();





                }

        }
        return super.onOptionsItemSelected(item);
    }



    public void removeFromCart(String productId) {

        Carts cart = new Carts(session.getUserId(), Integer.valueOf(productId));
        Call<JsonResponse> removeFromCart = SmartAPI.getApiService().removeFromCart(session.getJWT(), cart);
        removeFromCart.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (!response.isSuccessful())
                    Log.d("unsuccussful", "unsuccessful");
                else {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("200 OK") && message.equalsIgnoreCase("Item is removed from cart")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        myadapter.removeItem(position);
                        Intent intent = getIntent();
                        overridePendingTransition(0, 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);


                    }

                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });

    }


    public void removeFromCart(Context context,Session session,String productId) {
        Carts cart = new Carts(session.getUserId(), Integer.valueOf(productId));
        Call<JsonResponse> removeFromCart = SmartAPI.getApiService().removeFromCart(session.getJWT(), cart);
        removeFromCart.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (!response.isSuccessful())
                    Log.d("unsuccussful", "unsuccessful");
                else {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("200 OK") && message.equalsIgnoreCase("Item is removed from cart")) {
                        Intent intent = new Intent(context,cartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);


                    }

                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        session=new Session(cartActivity.this);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        Bundle b = getIntent().getExtras();
        sp = getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);


        //Cart listview
        lvlist = findViewById(R.id.LVNews);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        myadapter = new CartAdapter(listnewsData, cartActivity.this, tvTotalPrice);
        lvlist.setAdapter(myadapter);
        lvlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //get Cart List


        getCartItems();


        Button buCheckOut = findViewById(R.id.buCheckOut);


        buCheckOut.setOnClickListener((View v) ->
        {

            if (myadapter.getNumCheckBoxes().size() == 0) {
                Toasty.error(getApplicationContext(), "No item selected").show();
            } else {
                checkoutList.clear();
                Integer price = 0;
                for (int i = 0; i < myadapter.getNumCheckBoxes().size(); i++) {
                    String productId = myadapter.getNumCheckBoxes().get(i).get("Id");
                    position = Integer.parseInt(myadapter.getNumCheckBoxes().get(i).get("Position"));
                    Checkout orders=myadapter.getOrders().get(position);
                    checkoutList.add(new Checkout(
                            orders.getProductName(),
                            orders.getProductId(),
                            orders.getProductColor(),
                            orders.getProductSize(),
                            orders.getPrice(),
                            orders.getQty(),
                           orders.getProductImage()
                    ));

                    String order = orders.toString();
                    price += myadapter.getOrders().get(position).getPrice();
                    Log.d("ItemSelected", "Items selected are" + productId + " " + order);


                }
                initDialogView();
//                Log.d("orders",myadapter.getOrders().toString());
//          Log.d("totalprice",String.valueOf(myadapter.getTotal()));
                Toast.makeText(getApplicationContext(), String.valueOf(price), Toast.LENGTH_SHORT).show();


            }


        });

    }




    public void getCartItems() {
        listnewsData.clear();
        Integer userId = session.getUserId();
        Call<List<CartResponse>> cartList = SmartAPI.getApiService().getCartList(session.getJWT(), userId);
        cartList.enqueue(new Callback<List<CartResponse>>() {
            @Override
            public void onResponse(Call<List<CartResponse>> call, Response<List<CartResponse>> response) {
                if (!response.isSuccessful())
                    Log.d("unsuccesful", "unsuccessful");
                else {
                    if (response.body().size() == 0)
                    {  tvEmptyCart.setVisibility(View.VISIBLE);
                    showContinueShoppingDialog();}
                    for (CartResponse c : response.body()) {
                        Log.d("products", c.toString());
                        listnewsData.add(new CartResponse(c));
                        myadapter.notifyDataSetChanged();
                    }
                    hideProgressDialog();

                }

            }

            @Override
            public void onFailure(Call<List<CartResponse>> call, Throwable t) {
                Log.d("Error", t.getMessage());

            }
        });
    }

    private void showContinueShoppingDialog() {
        MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(this,R.style.AlertDialog);
        builder.setTitle("Continue");
        builder.setMessage("There are no items in this cart");
        builder.setPositiveButton("Continue Shopping", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent=new Intent(cartActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });
        builder.create().show();
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

    private void initDialogView() {

       MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this,R.style.AlertDialog);
       LayoutInflater inflater = this.getLayoutInflater();
        View dialogView= inflater.inflate(R.layout.order_dialog_context, null);
        dialogBuilder.setView(dialogView);
        RecyclerView rvDialog=dialogView.findViewById(R.id.rvDialog);
       TextView tvTotal=dialogView.findViewById(R.id.tvTotal);
       TextView tvSubTotal=dialogView.findViewById(R.id.SubTotal);
       TextView tvDeliveryAddrs=dialogView.findViewById(R.id.tvDeliveryAddress);
       Session session=new Session(cartActivity.this);
       tvDeliveryAddrs.setText(session.getAddress());
       tvSubTotal.setText("Subtotal"+"("+String.valueOf(myadapter.getItemCount()+" items)"));


     dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialogInterface, int i) {
             dialogInterface.dismiss();

         }
     });


     dialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialogInterface, int i) {
             showPaymentOptionDialog();
         }
     });

        DialogAdapter adapter=new DialogAdapter(checkoutList,getApplicationContext());
        rvDialog.setLayoutManager(new LinearLayoutManager(cartActivity.this));
        tvTotal.setText("Rs. "+ adapter.getTotal());
        rvDialog.setAdapter(adapter);
        dialogBuilder.create().show();


    }

    private void showPaymentOptionDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this,R.style.AlertDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView= inflater.inflate(R.layout.dialog_payment_option, null);
        dialogBuilder.setView(dialogView);
        EditText etDeliveryAddrs=dialogView.findViewById(R.id.etDelivery);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deliveryAddress=etDeliveryAddrs.getText().toString();
                showProgressDialog("Placing Order");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        placeOrder();
                        hideProgressDialog();
                        createFinishingDialog();
                    }

                }, 5000);


            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
        ;

        dialogBuilder.create().show();

    }

    private void createFinishingDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this,R.style.AlertDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView= inflater.inflate(R.layout.placed_order_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createClearCartDialog();


            }
        });

        dialogBuilder.create().show();



    }

    private void createClearCartDialog() {

        for (Checkout c:checkoutList){
            removeFromCart(String.valueOf(c.getProductId()));
        }
        listnewsData.clear();
        myadapter.notifyDataSetChanged();



    }

    private void placeOrder() {
        for (Checkout checkout:checkoutList){
            postOrder(checkout);
        }
    }

    private void postOrder(Checkout checkout) {
        Date d = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy/MM/dd");
        String date= DateFor.format(d);
         if (deliveryAddress.length()==0)
            deliveryAddress=session.getAddress();

        Orders orders=new Orders(
                checkout.getProductId(),
                session.getUserId(),
                checkout.getProductColor(),
                checkout.getProductSize(),
                checkout.getPrice(),
                checkout.getQty(),
                date,
                "Not delivered yet",
                deliveryAddress,
                "waiting"

        );

        Call<JsonResponse> addOrder=SmartAPI.getApiService().addOrder(session.getJWT(),orders);
        addOrder.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getStatus().equalsIgnoreCase("200 OK"))

                        Toasty.success(getApplicationContext(),"placed order").show();
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });



    }
}
