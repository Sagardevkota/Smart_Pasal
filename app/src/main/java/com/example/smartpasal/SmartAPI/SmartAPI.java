package com.example.smartpasal.SmartAPI;

import com.example.smartpasal.model.Carts;

import com.example.smartpasal.model.Coupons;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.model.User;



import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;

import retrofit2.http.POST;
import retrofit2.http.Path;


public class SmartAPI {


    public static apiService apiService=null;
    public static String base_url="http://10.0.2.2:9090";

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
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            apiService=retrofit.create(apiService.class);
        }
        return apiService;
    }

   public interface apiService{

        //User Services
      @POST(value = "/register")
       Call<JsonResponse> register(@Body User user);

       @POST(value = "/login")
       Call<JwtResponse> login(@Body User user);

       @GET(value = "/getUserId/{userName}")
       Call<JsonResponse> getUserId(@Header("Authorization") String jwt, @Path("userName") String userName);

       //Product Services
       @GET(value = "/getProducts/{pageNumber}")
       Call<List<ProductItems>> getProducts(@Header("Authorization") String jwt, @Path("pageNumber") int pageNumber);

       @GET(value = "/getProducts/id/{id}")
       Call<ProductItems> getOneProduct(@Header("Authorization") String jwt,@Path("id") int id);

       @GET(value = "/getProducts/{query}/{queryValue}/{sorting}")
       Call<List<ProductItems>> getProductByCategory(@Header("Authorization") String jwt,@Path("query") String query,@Path("queryValue") String QueryValue,@Path("sorting") String sorting);

       @GET(value = "/getProducts/query/{query}")
       Call<List<ProductItems>> searchForProduct(@Header("Authorization") String jwt, @Path("query") String Query);



       //cart Services
       @GET(value = "/getCartList/{userId}")
       Call<List<ProductItems>> getCartList(@Header("Authorization") String jwt,@Path("userId") Integer userId);

       @HTTP(method = "DELETE", path = "/removeFromCart", hasBody = true)
       Call<JsonResponse> removeFromCart(@Header("Authorization")String jwt, @Body Carts carts);

       @POST(value = "/addToCartList")
       Call<JsonResponse> addToCartList(@Header("Authorization") String jwt,@Body Carts carts);


       //coupon service
       @POST(value = "/checkCoupon")
       Call<JsonResponse> checkCoupon(@Header("Authorization") String jwt,@Body Coupons coupons);
   }

}
