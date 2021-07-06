package com.example.smartpasal.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.smartpasal.R;
import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.ConversationAdapter;
import com.example.smartpasal.adapter.ListAdapterItems;
import com.example.smartpasal.adapter.ReviewAdapter;
import com.example.smartpasal.databinding.ActivityProductDetailsBinding;
import com.example.smartpasal.model.Carts;
import com.example.smartpasal.model.ConversationResponse;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.model.ReviewResponse;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.nikartm.support.ImageBadgeView;

public class ProductDetails extends AppCompatActivity {
    private ActivityProductDetailsBinding binding;
    private Session session;
    private ConversationAdapter conversationAdapter;
    private final ArrayList<ProductItems> productItemsList = new ArrayList<>();
    private static final String TAG = "PRODUCT_DETAILS";
    private final ArrayList<ReviewResponse> reviewResponseList = new ArrayList<>();
    private ReviewAdapter reviewAdapter;
    private final ArrayList<ConversationResponse> conversationResponseList = new ArrayList<>();
    private String selectedColor = "";
    private Float selectedSize = 0f;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.fragment_fade_enter, R.anim.slide_out_right);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem menuItem = menu.findItem(R.id.cart);
        ImageBadgeView badgeView = menuItem.getActionView().findViewById(R.id.ibv_icon);
        badgeView.setBadgeValue(session.getBadgeCount());
        badgeView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), cartActivity.class);
            startActivity(intent);
        });

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        session = new Session(ProductDetails.this);

        Toolbar toolbar = findViewById(R.id.transparent_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle b = getIntent().getExtras();
        ProductItems productItems = b.getParcelable("product");
        getConversations(productItems.getProductId());
        getSimilarProducts(productItems.getCategory(), productItems.getType(),productItems.getProductId());
        initSimilarRecyclerView();
        initConversationRecyclerView();
        getReviews(productItems.getProductId());
        initReviewRecyclerView();
        setViews();
        binding.tvMoreReview.setOnClickListener(view1 -> {
            Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
            intent.putExtra("productId", productItems.getProductId());
            startActivity(intent);
        });
        getSellerName(productItems.getSeller_id());
        getSupportActionBar().setTitle(productItems.getProductName());

    }

    private void initSimilarRecyclerView() {
        ListAdapterItems listAdapter = new ListAdapterItems(productItemsList, getApplicationContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        binding.rvSimilarProducts.setAdapter(listAdapter);
        binding.rvSimilarProducts.setLayoutManager(gridLayoutManager);
    }

    private void getSimilarProducts(String category, String type, int productId) {

        SmartAPI.getApiService().getProductByCategoryAndType(session.getJWT(), type, category, "Popularity")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productItems -> {
                            int count = 0;
                            for (ProductItems p : productItems) {
                                if (p.getProductId() != productId && count < 5)
                                    productItemsList.add(p);
                                count++;
                            }
                        },
                        throwable -> Log.e(TAG, "getSimilarProducts: " + throwable.getMessage()));


    }

    private void initReviewRecyclerView() {
        reviewAdapter = new ReviewAdapter(reviewResponseList, getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.rvReviews.setAdapter(reviewAdapter);
        binding.rvReviews.setLayoutManager(layoutManager);

    }

    private void getSellerName(int seller_id) {
        SmartAPI.getApiService().getSellerName(session.getJWT(), seller_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    String status = response.getStatus();
                    String message = response.getMessage();
                        binding.tvSellerName.setText("Sold By \n \b " + message);
                }, throwable -> {
                    Log.e(TAG, "getSellerName: " + throwable.getMessage());
                });

    }



    private void setViews() {
        Bundle b = getIntent().getExtras();
        ProductItems productItems = b.getParcelable("product");
        binding.tvAskQuestions.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
            intent.putExtra("product_id", productItems.getProductId());
            startActivity(intent);
        });

        int discount = productItems.getDiscount();
        String Price = productItems.getPrice();
        String rating = productItems.getRating();
        int stock = productItems.getStock();
        binding.tvProductName.setText(productItems.getProductName());
        binding.tvBrand.setText(productItems.getBrand());
        binding.tvSku.setText(productItems.getSku());
        binding.tvDesc.setText(productItems.getDesc());
        binding.ratingBar.setRating(Float.valueOf(rating));


        Integer newPrice = 0;
        if (discount > 0) {
            binding.tvDiscount.setText("Discount: " + discount+ "%");
            Integer price = Integer.valueOf(Price);
            Integer discountedAmount = price * discount / 100;
            newPrice = price - discountedAmount;
            binding.tvDiscountedPrice.setVisibility(View.VISIBLE);
            binding.tvDiscountedPrice.setText("Rs." + newPrice);
            binding.tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            binding.tvPrice.setText("Rs. " + Price);
        } else {
            binding.tvDiscount.setTextColor(Color.BLACK);
            binding.tvDiscountedPrice.setVisibility(View.GONE);
            binding.tvPrice.setText("Rs. " + Price);
            binding.tvPrice.setTextColor(Color.RED);
            binding.tvPrice.setPaintFlags(0);
            newPrice = Integer.valueOf(Price);
            binding.tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
        }

        binding.tvStock.setText("In stock: " + stock);
        if (stock == 0) {
            binding.tvIsStock.setVisibility(View.VISIBLE);
            binding.ivProductImage.setImageAlpha(240);
            binding.buCart.setEnabled(false);
            binding.buBuy.setEnabled(false);
        }

        try {
            String url = SmartAPI.IMG_BASE_URL+productItems.getPicturePath();
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(binding.ivProductImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Load", "Successful");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Load", e.getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }

        List<String> colors = productItems.getColors();
        List<String> sizes = productItems.getSizes();

        if (sizes.size()!=0){
            sizes.stream().distinct().forEach(size -> {
                Chip chip =
                        (Chip) getLayoutInflater()
                                .inflate(R.layout.single_chip_layout, binding.sizeChipGroup, false);

                chip.setText(size);
                binding.sizeChipGroup.addView(chip);
            });

            binding.sizeChipGroup.check(binding.sizeChipGroup.getChildAt(0).getId());

            selectedSize = Float.valueOf(sizes.get(0)); //at first first size is the selected one
            //handle check change on chips
            binding.sizeChipGroup.setOnCheckedChangeListener((chipGroup, id) -> {
                Chip chip = chipGroup.findViewById(id);

                if (chip != null) {
                    selectedSize = Float.valueOf(chip.getText().toString());
                    Toasty.info(this, "You selected " + selectedSize + " size",
                            Toasty.LENGTH_SHORT)
                            .show();
                }

            });
        }

        if (colors.size()!=0){
            colors.stream().distinct().forEach(color -> {
                Chip chip =
                        (Chip) getLayoutInflater()
                                .inflate(R.layout.single_chip_layout, binding.colorChipGroup, false);
                chip.setText(color);
                chip.setCheckable(true);
                binding.colorChipGroup.addView(chip);
            });

            //always select first child
            binding.colorChipGroup.check(binding.colorChipGroup.getChildAt(0).getId());

            selectedColor = colors.get(0);//at first first color is the selected one


            //handle check change on chips
            binding.colorChipGroup.setOnCheckedChangeListener((chipGroup, id) -> {
                Chip chip = chipGroup.findViewById(id);


                if (chip != null) {
                    selectedColor = chip.getText().toString();
                    Toasty.info(this, "You selected " + selectedColor + " color",
                            Toasty.LENGTH_SHORT)
                            .show();

                }


            });
        }




    }

    private void initConversationRecyclerView() {
        conversationAdapter = new ConversationAdapter(conversationResponseList, getApplicationContext());
        binding.rvQA.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rvQA.setAdapter(conversationAdapter);
    }


    private void getConversations(int product_id) {
        SmartAPI.getApiService().getConversations(session.getJWT(), product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.size() > 3) {
                        for (int i = 0; i < 2; i++) {
                            {
                                ConversationResponse c = response.get(i);
                                conversationResponseList.add(new ConversationResponse(c));
                                conversationAdapter.notifyItemInserted(i);
                            }

                        }
                    } else {
                        for (int i = 0; i < response.size(); i++) {
                            {
                                ConversationResponse c = response.get(i);
                                conversationResponseList.add(new ConversationResponse(c));
                                conversationAdapter.notifyItemInserted(i);
                            }
                        }
                    }
                },throwable -> Log.e(TAG, "getConversations: "+throwable.getMessage() ));

    }


    public void buAdd(View view) {
        Bundle b = getIntent().getExtras();
        ProductItems productItems = b.getParcelable("product");
        int product_id = productItems.getProductId();
        String newprice = productItems.getPrice();
        String color = selectedColor;
        Float size = selectedSize;
        Carts carts = new Carts(session.getUserId(), product_id, newprice, color, size);

        SmartAPI.getApiService().addToCartList(session.getJWT(), carts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    String status = response.getStatus();
                    String message = response.getMessage();
                    if (status.equalsIgnoreCase("200 OK") && message.equalsIgnoreCase("Added to cart")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        goToCartActivity();
                    }
                    if (status.equalsIgnoreCase("401 Conflict") && message.equalsIgnoreCase("Item is already in cart"))
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }, throwable -> {
                });
    }



    private void goToCartActivity() {
        Intent intent = new Intent(getApplicationContext(), cartActivity.class);
        intent.putExtra("UserID", session.getUserId());
        startActivity(intent);
    }

    private void getReviews(int productId) {

            SmartAPI.getApiService().getReviews(session.getJWT(),productId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(reviewResponses -> {
                        if (reviewResponses.size() > 3) {
                            for (int i = 0; i < 2; i++) {
                                {
                                    ReviewResponse c = reviewResponses.get(i);
                                    reviewResponseList.add(new ReviewResponse(c));
                                    reviewAdapter.notifyItemInserted(i);
                                }

                            }
                        } else {
                            for (int i = 0; i < reviewResponses.size(); i++) {
                                {
                                    ReviewResponse c = reviewResponses.get(i);
                                    reviewResponseList.add(new ReviewResponse(c));
                                    reviewAdapter.notifyItemInserted(i);
                                }
                            }
                        }
                    });



    }


}
