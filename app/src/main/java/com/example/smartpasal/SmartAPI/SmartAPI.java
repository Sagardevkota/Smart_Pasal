package com.example.smartpasal.SmartAPI;


import com.example.smartpasal.model.CartResponse;
import com.example.smartpasal.model.Carts;

import com.example.smartpasal.model.ColorAttribute;
import com.example.smartpasal.model.Conversation;
import com.example.smartpasal.model.ConversationResponse;
import com.example.smartpasal.model.Coupons;
import com.example.smartpasal.model.Feedback;
import com.example.smartpasal.model.OrderResponse;
import com.example.smartpasal.model.Orders;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.model.ReviewResponse;
import com.example.smartpasal.model.SizeAttribute;
import com.example.smartpasal.model.User;



import java.util.List;


import io.reactivex.rxjava3.core.Observable;
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


public class SmartAPI {


    public static apiService apiService=null;
//    public static String base_url="http://52.171.61.18:8080";
    public static String base_url="http://10.0.2.2:8081";


    public static apiService getApiService(){
        if (apiService==null){
            HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            OkHttpClient okHttpClient=new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            apiService=retrofit.create(apiService.class);
        }
        return apiService;
    }

   public interface apiService{

        //User Services
      @POST(value = "/api/register")
       Call<JsonResponse> register(@Body User user);

       @POST(value = "/api/login")
       Call<JwtResponse> login(@Body User user);

       @GET(value = "/api/user/id/userName/{userName}")
       Call<JsonResponse> getUserId(@Header("Authorization") String jwt, @Path("userName") String userName);

       @GET(value = "/api/user/{userName}")
       Call<User> getUserDetails(@Header("Authorization") String jwt,@Path("userName")String userName);


       //user Update services
       @PUT(value = "/api/user/{userId}/userName/{newUserName}")
      Call<JsonResponse> updateEmail(@Header("Authorization") String jwt, @Path("userId")Integer userId, @Path("newUserName")String newUserName);

       @PUT(value = "/api/user/{userId}/phone/{newPhone}")
       Call<JsonResponse> updatePhone(@Header("Authorization") String jwt,@Path("userId")Integer userId,@Path("newPhone")String newPhone);

       @PUT(value = "/api/user/{userId}/delivery/{newDelivery}")
       Call<JsonResponse> updateDelivery(@Header("Authorization") String jwt,@Path("userId")Integer userId,@Path("newDelivery")String newDelivery);

       @GET(value = "/api/seller/{sellerId}/name")
       Call<JsonResponse> getSellerName(@Header("Authorization")String jwt,@Path("sellerId")Integer sellerId);


       //Product Services
       @GET(value = "/api/product/{pageNumber}")
       Call<List<ProductItems>> getProducts(@Header("Authorization") String jwt, @Path("pageNumber") int pageNumber);

       @GET(value = "/api/product/id/{id}")
       Call<ProductItems> getOneProduct(@Header("Authorization") String jwt,@Path("id") int id);

       @GET(value = "/api/product/{query}/{queryValue}/{sorting}")
       Call<List<ProductItems>> getProductByCategory(@Header("Authorization") String jwt,@Path("query") String query,@Path("queryValue") String QueryValue,@Path("sorting") String sorting);


       //get product by type and category from expandable list view
       @GET(value = "/api/product/type/{type}/category/{category}/{sorting}")
       Call<List<ProductItems>> getProductByCategoryAndType(@Header("Authorization") String jwt,@Path("type") String query,@Path("category") String QueryValue,@Path("sorting") String sorting);




       @GET(value = "/api/product/query/{query}")
       Call<List<ProductItems>> searchForProduct(@Header("Authorization") String jwt, @Path("query") String Query);

       @GET(value = "/api/color/{productId}")
       Call<List<ColorAttribute>> getColors(@Header("Authorization")String jwt,@Path("productId")Integer productId);

       @GET(value = "/api/size/{productId}")
       Call<List<SizeAttribute>> getSizes(@Header("Authorization")String jwt, @Path("productId")Integer productId);



       //cart Services
       @GET(value = "/api/cart/{userId}")
       Call<List<CartResponse>> getCartList(@Header("Authorization") String jwt, @Path("userId") Integer userId);

       @HTTP(method = "DELETE", path = "/api/cart", hasBody = true)
       Call<JsonResponse> removeFromCart(@Header("Authorization")String jwt, @Body Carts carts);

       @POST(value = "/api/cart")
       Call<JsonResponse> addToCartList(@Header("Authorization") String jwt,@Body Carts carts);

       @GET(value = "/api/cart/count/{userId}")
       Observable<JsonResponse> getBadgeCount(@Header("Authorization")String jwt,@Path("userId")Integer userId);


       //coupon service
       @POST(value = "/api/coupon")
       Call<JsonResponse> checkCoupon(@Header("Authorization") String jwt,@Body Coupons coupons);


       //conversation services
       @GET(value = "/api/conversation/{productId}")
       Call<List<ConversationResponse>> getConversations(@Header("Authorization")String jwt, @Path("productId")Integer productId);

       @POST(value = "/api/conversation")
       Call<JsonResponse> addConversation(@Header("Authorization")String jwt,@Body Conversation conversation);


       //order services
       @GET(value = "/api/order/id/{userId}/status/{status}")
       Call<List<OrderResponse>> getOrders(@Header("Authorization")String jwt, @Path("userId")Integer userId, @Path("status") String status);

       @POST(value = "/api/order")
       Call<JsonResponse> addOrder(@Header("Authorization")String jwt, @Body Orders orders);

    //recommendation services
       @GET(value = "/api/order/nearby/{userId}")
       Call<List<ProductItems>> getNearByOrders(@Header("Authorization")String jwt,@Path("userId") Integer userId);


   //feedback services
       @POST(value = "/api/feedback")
       Call<JsonResponse> addFeedback(@Header("Authorization")String jwt,@Body Feedback feedback);


       //review services
       @GET(value = "/api/reviews/{productId}")
       Call<List<ReviewResponse>> getReviews(@Header("Authorization")String jwt,@Path("productId")Integer productId);

       //review services
       @GET(value = "/api/reviews/{productId}/{userId}")
       Observable <ReviewResponse> getOneReview(@Header("Authorization")String jwt,@Path("productId")Integer productId,@Path("userId")Integer userId);

       @POST(value = "/api/reviews")
       Call<JsonResponse>  addReview(@Header("Authorization") String jwt,@Body ReviewResponse reviews);

       @PUT(value = "/api/reviews")
       Call<JsonResponse>  updateReview(@Header("Authorization")String jwt, @Body ReviewResponse reviews);


   }

}
