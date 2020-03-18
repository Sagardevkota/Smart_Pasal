package com.example.smartpasal.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartpasal.R;
import com.example.smartpasal.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    SharedPreferences sp;
    TextView tvEmail;
    TextView tvPhone;
    TextView tvVerified;
    TextView tvDelivery;
    Button buEdit;
    FrameLayout progressBarHolder;
    Animation bounce_animation;
    ImageView bouncing_image;
    View v;


    public Profile() {
        // Required empty public constructor
    }
    public interface IOnBackPressed {

        /**
         * If you return true the back press will not be taken into account, otherwise the activity will act naturally
         * @return true if your processing has priority if not false
         */
        boolean onBackPressed();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v= inflater.inflate(R.layout.fragment_profile, container, false);
        sp=this.getActivity().getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);
        String id= sp.getString("userID","userID");
        String url = "http://idealytik.com/SmartPasalWebServices/UserInfo.php?id="+id;
        new MyAsyncTaskgetNews().execute(url);


        return v;
    }

    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
            progressBarHolder = v.findViewById(R.id.progressBarHolder);
            progressBarHolder.setVisibility(View.VISIBLE);
            bouncing_image=v.findViewById(R.id.bouncing_image);

            bounce_animation= AnimationUtils.loadAnimation(getContext(),R.anim.bounce_animation);

            bouncing_image.setAnimation(bounce_animation);

        }
        @Override
        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds

                urlConnection.setRequestProperty("APIKEY",MainActivity.Smart_api_key);

                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    NewsData = ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);

                    urlConnection.setRequestProperty("APIKEY", MainActivity.Smart_api_key);
                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

            }catch (Exception ex){
                Log.d("Internet error",ex.getMessage());
            }
            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try{
                JSONArray userInfo = new JSONArray(progress[0]);
                for (int i = 0; i < userInfo.length(); i++) {
                    JSONObject userCredentials = userInfo.getJSONObject(i);

                    tvEmail= v.findViewById(R.id.tvEmail);
                    tvPhone= v.findViewById(R.id.tvPhone);
                    buEdit=v.findViewById(R.id.buEdit);
                    tvDelivery=v.findViewById(R.id.tvDelivery);
                    final String email=userCredentials.getString("email");
                  final   String phone=userCredentials.getString("phone");
                  String delivery_address=userCredentials.getString("delivery_address");
                  tvDelivery.setText(delivery_address);

                    tvEmail.setText(email);
                    tvPhone.setText(phone);
                    tvVerified= v.findViewById(R.id.tvVerified);
                    String verified=userCredentials.getString("verified");
                    if (verified.equals("1"))
                    {
                        tvVerified.setText("Yes");

                    }
                    else
                    {
                        tvVerified.setText("No");
                    }

                    buEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle b=new Bundle();
                            b.putString("email",email);
                            b.putString("phone",phone);
                           new fragment_edit_profile().setArguments(b);

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new fragment_edit_profile(), "findThisFragment")
                                    .addToBackStack(null)
                                    .commit();


                        }
                    });


                }

            }


            catch (Exception ex) {
                Log.d("er", ex.getMessage());
            }

        }


        protected void onPostExecute(String  result2){
            progressBarHolder.setVisibility(View.GONE);
            bounce_animation.cancel();



        }




    }

    // this method convert any stream to string
    public static String ConvertInputToStringNoChange(InputStream inputStream) {

        BufferedReader bureader=new BufferedReader( new InputStreamReader(inputStream));
        String line ;
        String linereultcal="";

        try{
            while((line=bureader.readLine())!=null) {

                linereultcal+=line;

            }
            inputStream.close();


        }catch (Exception ex){}

        return linereultcal;
    }


}
