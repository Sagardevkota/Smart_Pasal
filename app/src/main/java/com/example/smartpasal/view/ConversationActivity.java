package com.example.smartpasal.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.smartpasal.R;
import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.ConversationAdapter;
import com.example.smartpasal.databinding.ActivityConversationBinding;
import com.example.smartpasal.model.Conversation;
import com.example.smartpasal.model.ConversationResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ConversationActivity extends AppCompatActivity {

    private ActivityConversationBinding binding;
    private static final String TAG = "CONVERSATION_ACTIVITY";
    ArrayList<ConversationResponse> conversationResponseList = new ArrayList<>();
    ConversationAdapter conversationAdapter;
    private Session session;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.fragment_fade_enter, R.anim.slide_out_right);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConversationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        session = new Session(ConversationActivity.this);
        getSupportActionBar().setTitle("Q&A");
        Bundle b = getIntent().getExtras();
        Integer productId = b.getInt("product_id");
        getConversation(productId);
        initRecyclerView();
        binding.buSend.setOnClickListener(view1 -> {
            String message = binding.etMessage.getText().toString().trim();
            addConversation(message, productId);
        });

    }

    private void getConversation(Integer productId) {
        SmartAPI.getApiService().getConversations(session.getJWT(), productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responses -> {
                    int position = 0;
                    for (ConversationResponse c : responses) {
                        conversationResponseList.add(new ConversationResponse(c));
                        conversationAdapter.notifyItemInserted(position);
                        position++;
                    }
                });

    }

    private void initRecyclerView() {
        conversationAdapter = new ConversationAdapter(conversationResponseList, getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.rvQuestion.setAdapter(conversationAdapter);
        binding.rvQuestion.setLayoutManager(layoutManager);

    }

    private void addConversation(String message, Integer productId) {
        Date d = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
        String date = DateFor.format(d);

        Conversation conversation = new Conversation(message, session.getUserId(), date, productId);
        SmartAPI.getApiService()
                .addConversation(session.getJWT(), conversation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            if (response.getStatus().equalsIgnoreCase("200 OK")) {
                                conversationResponseList.add(new ConversationResponse(conversation.getMessage(), session.getusername(), date));
                                conversationAdapter.notifyItemInserted(conversationResponseList.size());
                                binding.etMessage.getText().clear();
                            }
                        },
                        throwable -> Log.e(TAG, "addConversation: " + throwable.getMessage()));


    }
}