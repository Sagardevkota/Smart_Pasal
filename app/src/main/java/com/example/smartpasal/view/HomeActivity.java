package com.example.smartpasal.view;

import androidx.annotation.NonNull;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.databinding.ActivityHomeBinding;
import com.example.smartpasal.repository.UserRepository;
import com.example.smartpasal.service.MyFirebaseMessagingService;
import com.example.smartpasal.adapter.ExpandableListAdapter;
import com.example.smartpasal.fragment.Profile;
import com.example.smartpasal.R;
import com.example.smartpasal.fragment.home;
import com.example.smartpasal.fragment.order;
import com.example.smartpasal.fragment.scan;

import com.example.smartpasal.Session.Session;

import com.example.smartpasal.viewmodel.HomeViewModel;
import com.google.android.material.badge.BadgeDrawable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HOME_ACTIVITY";
    private DrawerLayout mNavDrawer;
    private Session session;
    private BottomNavigationView bottomNavigationView;
    private BadgeDrawable badge;
    private HomeViewModel homeViewModel;

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_menu_settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_menu_updates:
                Toast.makeText(getApplicationContext(), "Your app is up to date", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_about:
                Intent intent1 = new Intent(getApplicationContext(), AboutUs.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserDetails();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(HomeActivity.this);

        com.example.smartpasal.databinding.ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.nav_drawer_layout);

         homeViewModel = new ViewModelProvider(this)
                .get(HomeViewModel.class);

        if (!new UserRepository(session, getApplicationContext()).checkIfLoggedIn()){
            Toasty.error(getApplicationContext(),"Session expired").show();
            goToLoginActivity();
        }

        mNavDrawer = findViewById(R.id.drawer_layout);
        SharedPreferences notipref = getSharedPreferences("com.example.smartpasal_preferences", Context.MODE_PRIVATE);
        if (notipref.getBoolean("notifications", true)) {
            MyFirebaseMessagingService.subscribeTopic("general");
        }

        ExpandableListView expandableListView1 = findViewById(R.id.expandable_listview);
        expandableListView1.setChildIndicator(null);
        expandableListView1.setDividerHeight(2);
        expandableListView1.setDivider(getResources().getDrawable(R.color.White));

        final List<String> headings = new ArrayList<String>();
        final List<String> L1 = new ArrayList<String>();
        List<String> L2 = new ArrayList<String>();
        List<String> L3 = new ArrayList<String>();
        List<String> L4 = new ArrayList<String>();
        List<String> L5 = new ArrayList<String>();
        List<String> L6 = new ArrayList<String>();
        final HashMap<String, List<String>> childList = new HashMap<String, List<String>>();
        String heading_items[] = getResources().getStringArray(R.array.heading_titles);
        String l1[] = getResources().getStringArray(R.array.h1_items);

        String l2[] = getResources().getStringArray(R.array.h2_items);

        String l3[] = getResources().getStringArray(R.array.h3_items);
        String l4[] = getResources().getStringArray(R.array.h4_items);
        String l5[] = getResources().getStringArray(R.array.h5_items);
        String l6[] = getResources().getStringArray(R.array.h6_items);


        Collections.addAll(headings, heading_items);
        Collections.addAll(L1, l1);
        Collections.addAll(L2, l2);
        Collections.addAll(L3, l3);
        Collections.addAll(L4, l4);
        Collections.addAll(L5, l5);
        Collections.addAll(L6, l6);
        childList.put(headings.get(0), L1);
        childList.put(headings.get(1), L2);
        childList.put(headings.get(2), L3);
        childList.put(headings.get(3), L4);
        childList.put(headings.get(4), L5);
        childList.put(headings.get(5), L6);

        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(getApplicationContext(), getResources(), headings, childList);
        expandableListView1.setAdapter(expandableListAdapter);


        expandableListView1.setOnChildClickListener((expandableListView, view, i, i1, l) -> {
            String selectedHeading = headings.get(i).toString();
            String selectedItem = (childList.get(selectedHeading).get(i1).toString());

            Toast.makeText(getApplicationContext(), selectedHeading + " is expanded for " + selectedItem, Toast.LENGTH_SHORT).show();

            if (selectedHeading.equalsIgnoreCase("Help And Settings")) {

                switch (selectedItem) {
                    case "Account":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile()).commit();
                        break;
                    case "Logout":
                        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(HomeActivity.this, R.style.AlertDialog);
                        alert.setMessage("Are you sure you want to log out?").setTitle("Confirmation").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                session.destroy();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();


                        break;
                }

            } else {
                Intent intent = new Intent(getApplicationContext(), categorizedActivity.class);
                intent.putExtra("type", selectedHeading);
                intent.putExtra("category", selectedItem);
                startActivity(intent);
                overridePendingTransition(R.anim.left2right, R.anim.right2left);

            }


            mNavDrawer.closeDrawer(GravityCompat.START);
            return false;
        });


        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        findViewById(R.id.etSearch).setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), searchList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new home()).commit();


        mNavDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mNavDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mNavDrawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headview = navigationView.getHeaderView(0);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);


        TextView nav_header_user_name = headview.findViewById(R.id.nav_header_user_name);
        nav_header_user_name.setText(session.getusername());


        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile()).commit();
                    Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                    break;


                case R.id.nav_logout:
                    MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(HomeActivity.this, R.style.AlertDialog);
                    alert.setMessage("Are you sure you want to log out?").setTitle("Confirmation").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showProgressDialog("Logging out");

                            session.destroy();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            hideProgressDialog();

                            finish();

                            startActivity(intent);


                        }
                    }).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();


                    break;
            }
            mNavDrawer.closeDrawer(GravityCompat.START);
            return false;
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new home()).commit();
                    item.setChecked(true);
                    break;

                case R.id.bottom_nav_scan:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new scan()).commit();
                    item.setChecked(true);
                    break;

                case R.id.bottom_nav_cart:
                    Intent intent = new Intent(getApplicationContext(), cartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;

                case R.id.bottom_nav_order:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new order()).commit();
                    item.setChecked(true);
                    break;

            }

            return false;
        });


    }


    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(this, R.style.AlertDialog);
        alert.setMessage("Are you sure you want to exit?").setTitle("Confirmation").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
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

    public void updateCartCount(){
        badge = bottomNavigationView.getOrCreateBadge(R.id.bottom_nav_cart);
        int cartCount = session.getBadgeCount();
        Log.i(TAG, "updateCartCount: "+cartCount);

        if (cartCount > 0) {
            badge.setVisible(true);
            badge.setNumber(cartCount);
        } else {
            badge.setVisible(false);
        }

    }


    private void getUserDetails() {
        SmartAPI.getApiService().getUserDetails(session.getJWT())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (user != null) {
                        badge = bottomNavigationView.getOrCreateBadge(R.id.bottom_nav_cart);
                        session.setUsername(user.getUserName());
                        session.setAddress(user.getDeliveryAddress());
                        session.setBadgeCount(user.getCartCount());
                        session.setUserId(user.getId());
                        updateCartCount();

                    }

                }, throwable -> {
                    Log.e(TAG, "getUserDetails: "+throwable.getMessage() );
                });


    }

    private void goToLoginActivity() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}