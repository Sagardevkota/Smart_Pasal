package com.example.smartpasal;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class categorizedActivity extends AppCompatActivity {
    GridView LVNews;
    MyCustomAdapter myadapter;
    ArrayList<CategoryAdapterItems> listnewsData = new ArrayList<CategoryAdapterItems>();
    GridView lvlist;
    TextView category;
    List<String> arr;
    Bundle b;
    FrameLayout progressBarHolder;
    Animation bounce_animation;
    ImageView bouncing_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorized);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        final Bundle b = getIntent().getExtras();
           //Cart listview
        lvlist = (GridView) findViewById(R.id.LVNews);
        myadapter = new MyCustomAdapter(listnewsData);
        lvlist.setAdapter(myadapter);

        Spinner spSort=(Spinner)findViewById(R.id.spSort);
        arr=new ArrayList<String>();
        arr.add("Popularity");
        arr.add("Price low to high");
        arr.add("Price high to low");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arr);
        spSort.setAdapter(arrayAdapter);
        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arr.get(position).equals("Popularity"))
                {

                    String url= "http://idealytik.com/SmartPasalWebServices/CategoryLists.php?category=" +b.getString("category", "")+"&sorting=Popularity";

                    new MyAsyncTaskgetNews().execute(url);
                    Toast.makeText(getApplicationContext(),"Popularity",Toast.LENGTH_LONG).show();
                    listnewsData.clear();
                    myadapter.notifyDataSetChanged();
                }
                if (arr.get(position).equals("Price low to high"))
                {

                    String url= "http://idealytik.com/SmartPasalWebServices/CategoryLists.php?category=" +b.getString("category", "")+"&sorting=Price low to high";
                    new MyAsyncTaskgetNews().execute(url);
                    Toast.makeText(getApplicationContext(),"Price low to high",Toast.LENGTH_LONG).show();
                    listnewsData.clear();
                    myadapter.notifyDataSetChanged();
                }
                if (arr.get(position).equals("Price high to low"))
                {

                    String url= "http://idealytik.com/SmartPasalWebServices/CategoryLists.php?category=" +b.getString("category", "")+"&sorting=Price high to low";
                    new MyAsyncTaskgetNews().execute(url);

                    Toast.makeText(getApplicationContext(),"Price high to low",Toast.LENGTH_LONG).show();
                    listnewsData.clear();
                    myadapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }

    private class MyCustomAdapter extends BaseAdapter {


        public ArrayList<CategoryAdapterItems> listnewsDataAdpater;

        public MyCustomAdapter(ArrayList<CategoryAdapterItems> listnewsDataAdpater) {
            this.listnewsDataAdpater = listnewsDataAdpater;

        }



        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = getLayoutInflater();
            final View myView = mInflater.inflate(R.layout.layout_category_lists, null);


            final CategoryAdapterItems s = listnewsDataAdpater.get(position);
            TextView tvProduct_Name = (TextView) myView.findViewById(R.id.tvProduct_Name);
            tvProduct_Name.setText(s.tvName);
            TextView tvFixed_Price = (TextView) myView.findViewById(R.id.tvFixed_Price);
            tvFixed_Price.setText("Rs. " + s.fixed_price);
            TextView tvMarked_Price = (TextView) myView.findViewById(R.id.tvMarked_Price);
            tvMarked_Price.setText("Rs. " + s.marked_price);

            tvMarked_Price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            category=findViewById(R.id.tvCategory);
            category.setText(s.category);


            ImageView ivImg = (ImageView) myView.findViewById(R.id.ivImg);
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),ProductDetails.class);
                    intent.putExtra("product_id",s.user_id);
                    intent.putExtra("product_name",s.tvName);
                    intent.putExtra("product_photo",s.picture_path);
                    intent.putExtra("fixed_price",s.fixed_price);
                    intent.putExtra("marked_price",s.marked_price);
                    intent.putExtra("brand",s.brand);
                    intent.putExtra("desc",s.desc);
                    intent.putExtra("sku",s.sku);

                    startActivity(intent);

                }
            });


            try {
                String url = s.picture_path;

                Picasso.get()
                        .load(url)
                        .fit()
                        .into(ivImg, new Callback() {
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


            return myView;
        }


    }





    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
            progressBarHolder = findViewById(R.id.progressBarHolder);
            progressBarHolder.setVisibility(View.VISIBLE);
            bouncing_image=findViewById(R.id.bouncing_image);

            bounce_animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce_animation);

            bouncing_image.setAnimation(bounce_animation);
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
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds
                urlConnection.setRequestProperty("http.keepAlive", "false");

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


            try {



                //display response data
                JSONArray userInfo = new JSONArray(progress[0]);
                for (int i = 0; i < userInfo.length(); i++) {
                    JSONObject userCredentials = userInfo.getJSONObject(i);

                    listnewsData.add(new CategoryAdapterItems(userCredentials.getString("name"), userCredentials.getString("picture_path"), userCredentials.getString("product_id"), userCredentials.getString("marked_price"), userCredentials.getString("fixed_price"),userCredentials.getString("brand"), userCredentials.getString("desc"), userCredentials.getString("sku"),userCredentials.getString("category")));


                }


                myadapter.notifyDataSetChanged();
                //display response data

            } catch (Exception ex) {
                Log.d("error is", ex.getMessage());
            }

        }


        protected void onPostExecute(String result2) {
progressBarHolder.setVisibility(View.GONE);
bounce_animation.cancel();

        }


    }

    // this method convert any stream to string
    public static String ConvertInputToStringNoChange(InputStream inputStream) {

        BufferedReader bureader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String linereultcal = "";

        try {
            while ((line = bureader.readLine()) != null) {

                linereultcal += line;

            }
            inputStream.close();


        } catch (Exception ex) {
        }

        return linereultcal;
    }



}
