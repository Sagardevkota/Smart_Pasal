package com.example.smartpasal.view;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;
import ru.nikartm.support.ImageBadgeView;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.JsonResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.ConversationAdapter;
import com.example.smartpasal.adapter.ListAdapterItems;
import com.example.smartpasal.adapter.ReviewAdapter;
import com.example.smartpasal.databinding.ActivityProductDetailsBinding;
import com.example.smartpasal.model.Carts;
import com.example.smartpasal.model.ColorAttribute;
import com.example.smartpasal.model.ConversationResponse;
import com.example.smartpasal.model.Coupons;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.model.ReviewResponse;
import com.example.smartpasal.model.SizeAttribute;

import com.example.smartpasal.Session.Session;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductDetails extends AppCompatActivity {
    ActivityProductDetailsBinding binding;
    private Session session;

    ArrayList<String> colorArraylist=new ArrayList<>();
    ArrayList<String> sizeList=new ArrayList<>();
   ConversationAdapter conversationAdapter;
   private ListAdapterItems listAdapter;
   private ArrayList<ProductItems> productItemsArrayList=new ArrayList<>();

   private ArrayList<ReviewResponse> reviewResponses=new ArrayList<>();

   private ReviewAdapter reviewAdapter;

    ArrayList<ConversationResponse> conversations=new ArrayList<>();
    String color="";
    Float size=0f;

    Integer newPrice=0;
    Integer PriceAfterCoupon=0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem menuItem = menu.findItem(R.id.cart);
        ImageBadgeView badgeView = menuItem.getActionView().findViewById(R.id.ibv_icon);
        badgeView.setBadgeValue(session.getBadgeCount());
        badgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), cartActivity.class);
                startActivity(intent);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProductDetailsBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        setContentView(view);
        session=new Session(ProductDetails.this);

        Toolbar toolbar = findViewById(R.id.transparent_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle b=getIntent().getExtras();
        getColors(b.getInt("product_id"));
        getSizes(b.getInt("product_id"));
        getConversations(b.getInt("product_id"));
        getSimilarProducts(b.getString("category"),b.getString("type"),b.getInt("product_id"));
        initSimilarRecyclerView();
        initConversationRecyclerView();
        getReviews();
        initReviewRecyclerView();
        setViews();
        binding.tvMoreReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ReviewActivity.class);
                intent.putExtra("productId",b.getInt("product_id"));
                startActivity(intent);
            }
        });
        getSellerName(b.getInt("seller_id"));
        getSupportActionBar().setTitle(b.getString("product_name","Product"));

    }

    private void initSimilarRecyclerView() {
        listAdapter=new ListAdapterItems(productItemsArrayList,getApplicationContext());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2);
        binding.rvSimilarProducts.setAdapter(listAdapter);
        binding.rvSimilarProducts.setLayoutManager(gridLayoutManager);
    }

    private void getSimilarProducts(String category, String type,Integer productId) {

     SmartAPI.getApiService().getProductByCategoryAndType(session.getJWT(),type,category,"Popularity")
             .enqueue(new retrofit2.Callback<List<ProductItems>>() {
                 @Override
                 public void onResponse(Call<List<ProductItems>> call, Response<List<ProductItems>> response) {
                     if (response.isSuccessful())
                     {
                         int count=0;
                         for (ProductItems p:response.body())
                         {
                             if (p.getProductId()!=productId&&count<5)
                                 productItemsArrayList.add(new ProductItems(p));

                             count++;
                         }
                     }
                 }

                 @Override
                 public void onFailure(Call<List<ProductItems>> call, Throwable t) {

                 }
             });





    }

    private void initReviewRecyclerView() {
        reviewAdapter=new ReviewAdapter(reviewResponses,getApplicationContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        binding.rvReviews.setAdapter(reviewAdapter);
        binding.rvReviews.setLayoutManager(layoutManager);

    }

    private void getSellerName(int seller_id) {
        Call<JsonResponse> getSellerId=SmartAPI.getApiService().getSellerName(session.getJWT(),seller_id);
        getSellerId.enqueue(new retrofit2.Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()){
                    String status=response.body().getStatus();
                    String message=response.body().getMessage();
                    if (status.equalsIgnoreCase("200 Ok"))
                        binding.tvSellerName.setText("Sold By \n \b "+ message);
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });
    }

    private void initSizeSpinner() {
        if (sizeList.size()==0)
            binding.spSize.setVisibility(View.GONE);
        else
        {
            binding.spSize.setVisibility(View.VISIBLE);
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,sizeList);
            binding.spSize.setAdapter(arrayAdapter);
            binding.spSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    size=Float.valueOf(sizeList.get(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }


    }

    private void initColorSpinner() {
        if (colorArraylist.size()==0)
            binding.spColor.setVisibility(View.GONE);
        else
        {
            binding.spColor.setVisibility(View.VISIBLE);
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, colorArraylist);
            binding.spColor.setAdapter(arrayAdapter);
            binding.spColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    color=colorArraylist.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

    }

    private void setViews() {
        Bundle b=getIntent().getExtras();
        binding.tvAskQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ConversationActivity.class);
                intent.putExtra("product_id",b.getInt("product_id"));
                startActivity(intent);
            }
        });

        Integer discount=b.getInt("discount");
        String Price=b.getString("price");
        String rating=b.getString("rating");
        binding.tvProductName.setText(b.getString("product_name"));
        binding.tvBrand.setText(b.getString("brand"));
        binding.tvSku.setText(b.getString("sku"));
        binding.tvDesc.setText(b.getString("desc"));
        binding.ratingBar.setRating(Float.valueOf(rating));
        binding.etCoupon.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) { if (editable.length()>0)binding.buApply.setEnabled(true); }});
            binding.buApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String coupon_code=binding.etCoupon.getEditText().getText().toString().trim();
                applyCoupon(coupon_code,b.getInt("product_id"));
            }
        });


        if (discount>0)
        {
            binding.tvDiscount.setText("Discount: "+ discount +"%");
            Integer price=Integer.valueOf(Price);
            Integer discountedAmount=price*discount/100;
            newPrice=price-discountedAmount;
            binding.tvDiscountedPrice.setVisibility(View.VISIBLE);
            binding.tvDiscountedPrice.setText("Rs." +String.valueOf(newPrice));
            binding.tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            binding.tvPrice.setText("Rs. "+Price);
        }
        else
        {binding.tvDiscount.setTextColor(Color.BLACK);
            binding.tvDiscountedPrice.setVisibility(View.GONE);
            binding.tvPrice.setText("Rs. "+Price);
            binding.tvPrice.setTextColor(Color.RED);
            binding.tvPrice.setPaintFlags(0);
            newPrice=Integer.valueOf(Price);
            binding.tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP ,24f);
        }

        binding.tvStock.setText("In stock: "+b.getInt("stock"));
        if (b.getInt("stock")==0)
        {
            binding.tvIsStock.setVisibility(View.VISIBLE);
            binding.ivProductImage.setImageAlpha(240);
            binding.buCart.setEnabled(false);
            binding.buBuy.setEnabled(false);
        }

        try{
            String url=b.getString("product_photo");
            Picasso.get()
                    .load(url)
                    .fit()
                    .into(binding.ivProductImage, new Callback() {
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

    private void initConversationRecyclerView() {
        conversationAdapter=new ConversationAdapter(conversations,getApplicationContext());
        binding.rvQA.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rvQA.setAdapter(conversationAdapter);
    }

    private void applyCoupon(String coupon_code, int product_id) {
        Coupons coupons=new Coupons(product_id,coupon_code);
        Call<JsonResponse> checkCoupon= SmartAPI.getApiService().checkCoupon(session.getJWT(),coupons);
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
                        Integer CouponedAmount=newPrice*discount/100;
                        PriceAfterCoupon=newPrice-CouponedAmount;
                        binding.tvPrice.setTextColor(Color.BLACK);
                        binding.tvCouponPrice.setText("Rs."+ String.valueOf(PriceAfterCoupon));
                        binding.tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        binding.tvDiscountedPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        binding.tvCouponPrice.setVisibility(View.VISIBLE);
                        binding.tvIsCoupon.setText("Discount applied of "+message+" %");
                    }
                    else
                    {
                        binding.tvIsCoupon.setVisibility(View.VISIBLE);
                        binding.tvIsCoupon.setText("Invalid coupon code");
                        binding.tvCouponPrice.setVisibility(View.GONE);
                        PriceAfterCoupon=newPrice;
                        binding.tvPrice.setTextColor(Color.parseColor("#ff5252"));
                        binding.tvPrice.setPaintFlags(0);
                    }



                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });


    }

    private void getConversations(int product_id) {
        Call<List<ConversationResponse>> getConversation=SmartAPI.getApiService().getConversations(session.getJWT(),product_id);
        getConversation.enqueue(new retrofit2.Callback<List<ConversationResponse>>() {
            @Override
            public void onResponse(Call<List<ConversationResponse>> call, Response<List<ConversationResponse>> response) {
                if (response.isSuccessful())
                {
                    if (response.body().size()>3)
                    {
                        for (int i=0;i<2;i++){
                            {
                                ConversationResponse c= response.body().get(i);
                                conversations.add(new ConversationResponse(c));
                                conversationAdapter.notifyDataSetChanged();
                            }

                    }
                    }
                    else {
                        for (int i = 0; i < response.body().size(); i++) {
                            {
                                ConversationResponse c = response.body().get(i);
                                conversations.add(new ConversationResponse(c));
                                conversationAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ConversationResponse>> call, Throwable t) {

            }
        });
    }


    public void buAdd(View view) {
        Bundle b=getIntent().getExtras();
        Integer product_id=b.getInt("product_id",0);

        Integer finalPrice=0;
        if (PriceAfterCoupon>0)
            finalPrice=PriceAfterCoupon;
        else
            finalPrice=newPrice;

        String newprice=String.valueOf(finalPrice);
        Carts carts=new Carts(session.getUserId(),product_id,newprice,color,size);

        Call<JsonResponse> addToCartList= SmartAPI.getApiService().addToCartList(session.getJWT(),carts);
        addToCartList.enqueue(new retrofit2.Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (!response.isSuccessful())
                    Log.d("","");
                else
                {
                    String status=response.body().getStatus();
                    String message=response.body().getMessage();
                    if (status.equalsIgnoreCase("200 OK")&&message.equalsIgnoreCase("Added to cart"))
                    { Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        goToCartActivity();
                    }
                    if (status.equalsIgnoreCase("401 Conflict")&&message.equalsIgnoreCase("Item is already in cart"))
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });


    }

    private void getColors(Integer productId){
        Call<List<ColorAttribute>> getcolor=SmartAPI.getApiService().getColors(session.getJWT(),productId);
        getcolor.enqueue(new retrofit2.Callback<List<ColorAttribute>>() {
            @Override
            public void onResponse(Call<List<ColorAttribute>> call, Response<List<ColorAttribute>> response) {
                if (!response.isSuccessful())
                    Log.d("","");
                else
                {
                    if (response.body().size()==0)
                       binding.tvIsColor.setText("No color option available for this product");
                        else
                    for (ColorAttribute c:response.body()){
                        colorArraylist.add(c.getColor());
                        initColorSpinner();


                    }
                    Log.d("color",response.body().toString());
                }

            }

            @Override
            public void onFailure(Call<List<ColorAttribute>> call, Throwable t) {

            }
        });

    }

    private void getSizes(Integer productId){
        Call<List<SizeAttribute>> getSize=SmartAPI.getApiService().getSizes(session.getJWT(),productId);
        getSize.enqueue(new retrofit2.Callback<List<SizeAttribute>>() {
            @Override
            public void onResponse(Call<List<SizeAttribute>> call, Response<List<SizeAttribute>> response) {
                if (!response.isSuccessful())
                    Log.d("","");
                else
                {
                    if (response.body().size()==0)
                       binding.tvIsSize.setText("No size option available for this product");
                    Log.d("size",response.body().toString());
                    Float [] abc=new Float[response.body().size()];
                    int i=0;
                    for (SizeAttribute s:response.body()){
                        abc[i]=s.getSize();
                        i++;
                    }
                  Arrays.sort(abc);
                    for (Float a:abc){
                        sizeList.add(String.valueOf(a));
                    }
                    initSizeSpinner();
                }
            }

            @Override
            public void onFailure(Call<List<SizeAttribute>> call, Throwable t) {

            }
        });

    }




    private void goToCartActivity() {
        Bundle b=getIntent().getExtras();
        Intent intent=new Intent(getApplicationContext(),cartActivity.class);
        intent.putExtra("UserID",b.getString("UserID") );
        startActivity(intent);
    }

    private void getReviews() {
        Bundle b=getIntent().getExtras();
        if (b!=null)
        {
            Call<List<ReviewResponse>> getReview= SmartAPI.getApiService().getReviews(session.getJWT(),b.getInt("product_id"));
            getReview.enqueue(new retrofit2.Callback<List<ReviewResponse>>() {
                @Override
                public void onResponse(Call<List<ReviewResponse>> call, Response<List<ReviewResponse>> response) {
                    if (response.isSuccessful())
                    {
                        if (response.body().size()>3)
                        {
                            for (int i=0;i<2;i++){
                                {
                                    ReviewResponse c= response.body().get(i);
                                    reviewResponses.add(new ReviewResponse(c));
                                    reviewAdapter.notifyDataSetChanged();
                                }

                            }
                        }
                        else {
                            for (int i = 0; i < response.body().size(); i++) {
                                {
                                    ReviewResponse c = response.body().get(i);
                                    reviewResponses.add(new ReviewResponse(c));
                                    reviewAdapter.notifyDataSetChanged();
                                }
                            }
                        }


                    }
                }

                @Override
                public void onFailure(Call<List<ReviewResponse>> call, Throwable t) {

                }
            });
        }

    }


    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this,R.style.AlertDialog);
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
