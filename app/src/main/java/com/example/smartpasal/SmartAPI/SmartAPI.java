package com.example.smartpasal.SmartAPI;


import com.example.smartpasal.model.CartResponse;
import com.example.smartpasal.model.Carts;

import com.example.smartpasal.model.Conversation;
import com.example.smartpasal.model.ConversationResponse;
import com.example.smartpasal.model.Coupons;
import com.example.smartpasal.model.Feedback;
import com.example.smartpasal.model.OrderResponse;
import com.example.smartpasal.model.Orders;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.model.ReviewResponse;
import com.example.smartpasal.model.User;


import java.util.List;


import io.reactivex.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;

import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public class SmartAPI {


    public static apiService apiService = null;
    //    public static String base_url="http://52.171.61.18:8080";
    public static String BASE_URL = "http://10.0.2.2:8081";
//    public static final String BASE_URL = "http://157.55.181.67:8080";//newest
//    public static final String BASE_URL = "http://23.101.181.211:8080";
    public static final String IMG_BASE_URL = "https://bese2016smartstore.blob.core.windows.net/bese2016blob/";


    public static apiService getApiService() {
        if (apiService == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            apiService = retrofit.create(apiService.class);
        }
        return apiService;
    }

    public interface apiService {

        //User Services
        @POST(value = "/api/register")
        Observable<JsonResponse> register(@Body User user);

        @POST(value = "/api/login")
        Observable<JwtResponse> login(@Body User user);

        @GET(value = "/api/user/id/userName/{userName}")
        Observable<JsonResponse> getUserId(@Header("Authorization") String jwt, @Path("userName") String userName);

        @GET(value = "/api/user/details")
        Observable<User> getUserDetails(@Header("Authorization") String jwt);


        //user Update services
        @PUT(value = "/api/user/userName/{newUserName}")
        Observable<JsonResponse> updateEmail(@Header("Authorization") String jwt, @Path("newUserName") String newUserName);

        @PUT(value = "/api/user/phone/{newPhone}")
        Observable<JsonResponse> updatePhone(@Header("Authorization") String jwt, @Path("newPhone") String newPhone);

        @PUT(value = "/api/user/delivery/{newDelivery}")
        Observable<JsonResponse> updateDelivery(@Header("Authorization") String jwt, @Path("newDelivery") String newDelivery);

        @GET(value = "/api/user/userName/{userId}")
        Observable<JsonResponse> getSellerName(@Header("Authorization") String jwt, @Path("userId") Integer sellerId);


        //Product Services
        @GET(value = "/api/products/{pageNumber}")
        Single<List<ProductItems>> getProducts(@Header("Authorization") String jwt, @Path("pageNumber") int pageNumber);

        @GET(value = "/api/products/hot-deals/{pageNumber}")
        Observable<List<ProductItems>> getHotDeals(@Header("Authorization") String jwt, @Path("pageNumber") int pageNumber);

        @GET(value = "/api/products/id/{id}")
        Call<ProductItems> getOneProduct(@Header("Authorization") String jwt, @Path("id") int id);

        @GET(value = "/api/products/{query}/{queryValue}/{sorting}")
        Observable<List<ProductItems>> getProductByCategory(@Header("Authorization") String jwt, @Path("query") String query, @Path("queryValue") String QueryValue, @Path("sorting") String sorting);


        //get product by type and category from expandable list view
        @GET(value = "/api/products/type/{type}/category/{category}/{sorting}")
        Observable<List<ProductItems>> getProductByCategoryAndType(@Header("Authorization") String jwt, @Path("type") String query, @Path("category") String QueryValue, @Path("sorting") String sorting);


        @GET(value = "/api/products/query/{query}")
        Observable<List<ProductItems>> searchForProduct(@Header("Authorization") String jwt, @Path("query") String Query);


        //cart Services
        @GET(value = "/api/carts")
        Observable<List<CartResponse>> getCartList(@Header("Authorization") String jwt);

        @HTTP(method = "DELETE", path = "/api/carts", hasBody = true)
        Observable<JsonResponse> removeFromCart(@Header("Authorization") String jwt, @Body Carts carts);

        @POST(value = "/api/carts")
        Observable<JsonResponse> addToCartList(@Header("Authorization") String jwt, @Body Carts carts);

        @GET(value = "/api/carts/count")
        Observable<JsonResponse> getBadgeCount(@Header("Authorization") String jwt);


        //coupon service
        @POST(value = "/api/coupon")
        Observable<JsonResponse> checkCoupon(@Header("Authorization") String jwt, @Body Coupons coupons);


        //conversation services
        @GET(value = "/api/user/conversations/{productId}")
        Observable<List<ConversationResponse>> getConversations(@Header("Authorization") String jwt, @Path("productId") Integer productId);

        @POST(value = "/api/user/conversations")
        Observable<JsonResponse> addConversation(@Header("Authorization") String jwt, @Body Conversation conversation);


        //order services
        @GET(value = "/api/orders/status/{status}")
        Observable<List<OrderResponse>> getOrders(@Header("Authorization") String jwt, @Path("status") String status);

        @POST(value = "/api/orders")
        Observable<JsonResponse> addOrder(@Header("Authorization") String jwt, @Body Orders orders);

        //recommendation services
        @GET(value = "/api/user/orders/nearby")
        Observable<List<ProductItems>> getNearByOrders(@Header("Authorization") String jwt);


        //feedback services
        @POST(value = "/api/feedbacks")
        Observable<JsonResponse> addFeedback(@Header("Authorization") String jwt, @Body Feedback feedback);


        //review services
        @GET(value = "/api/reviews/{productId}")
        Observable<List<ReviewResponse>> getReviews(@Header("Authorization") String jwt, @Path("productId") Integer productId);

        //review services
        @GET(value = "/api/reviews/{productId}")
        Observable<ReviewResponse> getOneReview(@Header("Authorization") String jwt, @Path("productId") Integer productId);

        @POST(value = "/api/reviews")
        Observable<JsonResponse> addReview(@Header("Authorization") String jwt, @Body ReviewResponse reviews);

        @PUT(value = "/api/reviews")
        Observable<JsonResponse> updateReview(@Header("Authorization") String jwt, @Body ReviewResponse reviews);


        @PUT(value = "/api/user")
        Observable<JsonResponse> updateAccount(@Header("Authorization") String jwt,@Body User user);

        @PUT(value = "/api/user/change-password")
        Observable<JsonResponse> updatePassword(@Header("Authorization")String jwt,
                                                @Query("currentPassword") String currentPassword,
                                                @Query("newPassword")String newPassword);
    }

}
