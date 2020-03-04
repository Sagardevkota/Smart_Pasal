package com.example.smartpasal.view;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.nikartm.support.ImageBadgeView;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout mNavDrawer;
   private SharedPreferences sp;
    GoogleApiClient googleApiClient;
   EditText etSearch;
   TextView tvSneakers;
   ImageBadgeView badgeView;
   BottomNavigationView bottomNavigationView;
    BadgeDrawable badge;
     public static Integer count;

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
        sp=getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
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