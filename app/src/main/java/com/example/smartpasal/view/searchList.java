package com.example.smartpasal.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.smartpasal.R;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.SearchAdapterItems;
import com.example.smartpasal.databinding.ActivitySearchListBinding;
import com.example.smartpasal.fragment.SearchFragment;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.Session.Session;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class searchList extends AppCompatActivity {

    private static final String TAG = "SEARCHLIST";
    private ActivitySearchListBinding binding;
    private Session session;
    private List<String> productNames = new ArrayList<>();
    private ArrayAdapter<String> adapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        session = new Session(searchList.this);
        initKeywordChipGroup();


        binding.backImg.setOnClickListener(view1 -> {
            finish();
            overridePendingTransition(R.anim.fragment_fade_enter, R.anim.slide_out_right);
        });



        binding.actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().isEmpty()) {
                    SmartAPI.getApiService()
                            .searchForProduct(session.getJWT(), editable.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(productItems -> {

                                productNames = productItems.stream()
                                                .map(ProductItems::getProductName)
                                                .collect(Collectors.toList());

                                adapter = new ArrayAdapter<>(getApplicationContext(),
                                                R.layout.search_list_item, R.id.text_view_list_item, productNames);
                                        binding.actv.setAdapter(adapter);
                                        Log.i(TAG, "afterTextChanged: "+productNames);

                                    },
                                    throwable -> Log.e(TAG, "afterTextChanged: " + throwable.getMessage()));

                }
            }
        });

        binding.actv.setOnItemClickListener((parent, view12, position, id) ->
                goToSearchFragment(adapter.getItem(position)));

        binding.ivSearch.setOnClickListener(v ->
                goToSearchFragment(binding.actv.getEditableText().toString()));

    }


    private void initKeywordChipGroup() {
        List<String > searchKeywordList = Arrays.asList("Laptops","SmartPhones","Watches","Games","Jackets","Books");

            searchKeywordList.stream().distinct().forEach(keyword -> {
                Chip chip =
                        (Chip) getLayoutInflater()
                                .inflate(R.layout.single_chip_layout, binding.keywordChipGroup, false);
                chip.setText(keyword);
                chip.setCheckable(true);
                binding.keywordChipGroup.addView(chip);

            });
            binding.keywordChipGroup.setOnCheckedChangeListener((chipGroup, id) -> {
            Chip chip = chipGroup.findViewById(id);

            if (chip != null) {
                Toasty.info(this, "You selected " + chip.getText().toString(),
                        Toasty.LENGTH_SHORT)
                        .show();
                goToSearchFragment(chip.getText().toString());

            }

        });




    }

    private void goToSearchFragment(String searchKeyword) {
        if (searchKeyword.length()==0)
            return;
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("search_keyword",searchKeyword);
        searchFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,searchFragment)
                .commit();
    }


}
