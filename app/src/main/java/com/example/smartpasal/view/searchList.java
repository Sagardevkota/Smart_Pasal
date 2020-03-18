package com.example.smartpasal.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.smartpasal.R;
import com.example.smartpasal.adapter.SearchAdapterItems;
import com.example.smartpasal.model.SearchItems;
import com.example.smartpasal.view.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class searchList extends AppCompatActivity {
    ImageView backimg;
    EditText etSearch;
    RecyclerView endless_view;

    ArrayList<SearchItems> listnewsData = new ArrayList<SearchItems>();
    GridLayoutManager layoutManager;
    RecyclerView.Adapter myadapter;
    ProgressBar progressBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        endless_view=findViewById(R.id.endless_view);
        layoutManager=new GridLayoutManager(getApplicationContext(),2);

        endless_view.setLayoutManager(layoutManager);
        myadapter=new SearchAdapterItems(listnewsData,getApplicationContext());
        progressBar=findViewById(R.id.progress_bar);



        endless_view.setHasFixedSize(true);
        endless_view.setAdapter(myadapter);


        backimg = findViewById(R.id.back_img);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        etSearch = findViewById(R.id.etSearch);



        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {





            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){




                    String url="http://idealytik.com/SmartPasalWebServices/SearchLists.php?keyword="+editable.toString();
                    new MyAsyncTaskgetNews1().execute(url);



                }
                else {
                    listnewsData.clear();
                }



            }
        });

    }




    public class MyAsyncTaskgetNews1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
            progressBar.setVisibility(View.VISIBLE);





        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("http.keepAlive", "false");
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds
                urlConnection.setDoOutput(true);

                urlConnection.setRequestProperty("APIKEY",MainActivity.Smart_api_key);


                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    NewsData = ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);
                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

            } catch (Exception ex) {
                Log.d("error", ex.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            listnewsData.clear();


            try {
                JSONArray userInfo = new JSONArray(progress[0]);


                for (int i = 0; i < userInfo.length(); i++) {
                    JSONObject userCredentials = userInfo.getJSONObject(i);


                    listnewsData.add(new SearchItems(userCredentials.getString("name"), userCredentials.getString("picture_path"), userCredentials.getString("product_id"), userCredentials.getString("marked_price"), userCredentials.getString("fixed_price"), userCredentials.getString("brand"), userCredentials.getString("desc"), userCredentials.getString("sku")));


                }


                myadapter.notifyDataSetChanged();
                //display response data
                progressBar.setVisibility(View.GONE);


            } catch (Exception ex) {
            }


        }

        protected void onPostExecute(String result2) {



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
