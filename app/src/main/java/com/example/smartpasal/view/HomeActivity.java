package com.example.smartpasal.view;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartpasal.MyFirebaseMessagingService;
import com.example.smartpasal.adapter.App;
import com.example.smartpasal.adapter.ExpandableListAdapter;
import com.example.smartpasal.fragment.Profile;
import com.example.smartpasal.R;
import com.example.smartpasal.fragment.Settings;
import com.example.smartpasal.fragment.cart;
import com.example.smartpasal.fragment.home;
import com.example.smartpasal.fragment.order;
import com.example.smartpasal.fragment.scan;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import ru.nikartm.support.ImageBadgeView;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout mNavDrawer;
   private SharedPreferences sp;
    GoogleApiClient googleApiClient;
   EditText etSearch;
   TextView tvSneakers;
   ImageBadgeView badgeView;
   BottomNavigationView bottomNavigationView;
   ExpandableListView expandableListView;
    BadgeDrawable badge;
    public  static  TextView app_name;

     Toolbar toolbar;

     public static Integer count;
    private Handler mHandler = new Handler();

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting_menu_settings:
                Intent intent=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_menu_updates:
                Toast.makeText(getApplicationContext(),"Your app is up to date",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_about:
                Intent intent1=new Intent(getApplicationContext(),AboutUs.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);



        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();

        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout);
        mNavDrawer = findViewById(R.id.drawer_layout);

        SharedPreferences notipref=getSharedPreferences("com.example.smartpasal_preferences",Context.MODE_PRIVATE);
        if (notipref.getBoolean("notifications",true)){
            MyFirebaseMessagingService.subscribeTopic("general");
        }





        sp=getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);

        final AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);


        expandableListView=findViewById(R.id.expandable_listview);
        expandableListView.setChildIndicator(null);
        expandableListView.setDividerHeight(2);
        expandableListView.setDivider(getResources().getDrawable(R.color.White));

        final List<String> headings=new ArrayList<String>();
        final List<String> L1=new ArrayList<String>();
        List<String> L2=new ArrayList<String>();
        List<String> L3=new ArrayList<String>();
        List<String> L4=new ArrayList<String>();
        List<String> L5=new ArrayList<String>();
        List<String> L6=new ArrayList<String>();
        final HashMap<String,List<String>> childList=new HashMap<String, List<String>>();
        String heading_items[]=getResources().getStringArray(R.array.heading_titles);
        String l1[]=getResources().getStringArray(R.array.h1_items);

        String l2[]=getResources().getStringArray(R.array.h2_items);

        String l3[]=getResources().getStringArray(R.array.h3_items);
        String l4[]=getResources().getStringArray(R.array.h4_items);
        String l5[]=getResources().getStringArray(R.array.h5_items);
        String l6[]=getResources().getStringArray(R.array.h6_items);



        for (String title:heading_items){
            headings.add(title);
        }
        for (String title:l1){
            L1.add(title);
        }
        for (String title:l2){
            L2.add(title);
        }
        for (String title:l3){
            L3.add(title);
        }
        for (String title:l4){
            L4.add(title);
        }
        for (String title:l5){
            L5.add(title);
        }
        for (String title:l6){
            L6.add(title);
        }
        childList.put(headings.get(0),L1);
        childList.put(headings.get(1),L2);
        childList.put(headings.get(2),L3);
        childList.put(headings.get(3),L4);
        childList.put(headings.get(4),L5);
        childList.put(headings.get(5),L6);
        ExpandableListAdapter expandableListAdapter=new ExpandableListAdapter(getApplicationContext(),getResources(),headings,childList);
        expandableListView.setAdapter(expandableListAdapter);




        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selectedHeading=headings.get(i).toString();
                String selectedItem=(childList.get(selectedHeading).get(i1).toString());

                Toast.makeText(getApplicationContext(),selectedHeading+" is expanded for "+selectedItem,Toast.LENGTH_SHORT).show();


                if (selectedHeading.equalsIgnoreCase("Help And Settings")){

                    switch (selectedItem){
                        case "Account":
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Profile()).commit();


                            break;
                        case "Settings":
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Settings()).commit();
                            break;
                        case "Logout":
                            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(HomeActivity.this,R.style.AlertDialog);
                            alert.setMessage("Are you sure you want to log out?").setTitle("Confirmation").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(@NonNull Status status) {
                                            sp.edit().putBoolean("logged", false).apply();
                                            showProgressDialog("Logging out");

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            hideProgressDialog();
                                        }
                                    });


                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();


                            break;
                    }

                }
                else{
                    Intent intent=new Intent(getApplicationContext(),categorizedActivity.class);
                    intent.putExtra("type",selectedHeading);
                    intent.putExtra("category",selectedItem);

                    startActivity(intent);

                }





                mNavDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });





      toolbar = findViewById(R.id.custom_toolbar);

         Runnable mToastRunnable = new Runnable() {
            @Override
            public void run() {


                Random rnd = new Random();
                int color = Color.argb(200, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                int color1 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                int[] colors = {color,color1};


                //create a new gradient color
                GradientDrawable gd = new GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT, colors);


                gd.setCornerRadius(0f);




               toolbar.setBackgroundDrawable(gd);
               mAppBarLayout.setBackgroundDrawable(gd);
                mHandler.postDelayed(this, 5000);
            }
        };
         mToastRunnable.run();





        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        String url="http://idealytik.com/SmartPasalWebServices/BadgeCount.php?id="+sp.getString("userID","");
        new MyAsyncTaskgetNews().execute(url);




        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home()).commit();



etSearch=findViewById(R.id.etSearch);
etSearch.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(getApplicationContext(), searchList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
});
        mNavDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mNavDrawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mNavDrawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView=findViewById(R.id.navigation_view);
        View headview = navigationView.getHeaderView(0);
        ImageView header_user_image = (ImageView) headview.findViewById(R.id.header_user_image);

        TextView nav_header_user_name=headview.findViewById(R.id.nav_header_user_name);
        nav_header_user_name.setText(sp.getString("email",""));
        try{
            String img_url=sp.getString("user_photo","");

            Picasso.get()
                    .load(img_url)
                    .fit()
                    .into(header_user_image, new Callback() {
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


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Profile()).commit();
                        Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Settings()).commit();
                        Toast.makeText(getApplicationContext(), "Settings selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_logout:
                        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(HomeActivity.this,R.style.AlertDialog);
                        alert.setMessage("Are you sure you want to log out?").setTitle("Confirmation").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        sp.edit().putBoolean("logged", false).apply();
                                        showProgressDialog("Logging out");

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });


                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();



                        break;
                }
                mNavDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        bottomNavigationView=findViewById(R.id.bottom_nav_view);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {






                switch (item.getItemId()){
                    case R.id.bottom_nav_home:

                          getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home()).commit();
                        item.setChecked(true);



                        break;
                    case R.id.bottom_nav_scan:
                         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new scan()).commit();
                        item.setChecked(true);


                          break;
                    case R.id.bottom_nav_cart:

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new cart()).commit();
                        item.setChecked(true);



                        break;
                    case R.id.bottom_nav_order:
                           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new order()).commit();
                        item.setChecked(true);



                        break;
                }

                return false;
            }
        });

    }


    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder alert=new MaterialAlertDialogBuilder(this,R.style.AlertDialog);
        alert.setMessage("Are you sure you want to exit?").setTitle("Confirmation").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();


    }


    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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

    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works

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
                urlConnection.setRequestProperty("http.keepAlive", "false");
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
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
                JSONObject json= new JSONObject(progress[0]);
                //display response data

               count=Integer.valueOf(json.getString("numrows"));
                BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(2);
                badge = bottomNavigationView.getOrCreateBadge(itemView.getId());
                if (count==0){

                    badge.setVisible(false);
                }
                else {
                    badge.setNumber(count);
                    badge.setVisible(true);
                }

            }


            catch (Exception ex) {
                Log.d("er", ex.getMessage());
            }

        }


        protected void onPostExecute(String  result2){


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