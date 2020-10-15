package com.example.smartpasal.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;


import com.example.smartpasal.SmartAPI.JsonResponse;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.adapter.ConversationAdapter;
import com.example.smartpasal.databinding.ActivityConversationBinding;
import com.example.smartpasal.model.Conversation;
import com.example.smartpasal.model.ConversationResponse;
import com.example.smartpasal.Session.Session;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationActivity extends AppCompatActivity {

    private ActivityConversationBinding binding;
    ArrayList<ConversationResponse> conversationResponseList=new ArrayList<>();
    ConversationAdapter conversationAdapter;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityConversationBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        session=new Session(ConversationActivity.this);
        getSupportActionBar().setTitle("Q&A");
        Bundle b=getIntent().getExtras();
        Integer productId=b.getInt("product_id");
        getConversation(productId);
        initRecyclerView();
        binding.buSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=binding.etMessage.getText().toString().trim();
                addConversation(message,productId);
            }
        });

    }

    private void getConversation(Integer productId) {

        Call<List<ConversationResponse>> getConversation= SmartAPI.getApiService().getConversations(session.getJWT(),productId);
      getConversation.enqueue(new Callback<List<ConversationResponse>>() {
          @Override
          public void onResponse(Call<List<ConversationResponse>> call, Response<List<ConversationResponse>> response) {
              if (response.isSuccessful()){
                  for (ConversationResponse c:response.body()){
                      conversationResponseList.add(new ConversationResponse(c));
                      conversationAdapter.notifyDataSetChanged();

                  }
                    }
          }

          @Override
          public void onFailure(Call<List<ConversationResponse>> call, Throwable t) {

          }
      });
    }

    private void initRecyclerView() {
        conversationAdapter=new ConversationAdapter(conversationResponseList,getApplicationContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        binding.rvQuestion.setAdapter(conversationAdapter);
        binding.rvQuestion.setLayoutManager(layoutManager);

    }

    private void addConversation(String message,Integer productId){
        Date d = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
        String date= DateFor.format(d);

        Conversation conversation=new Conversation(message,session.getUserId(),date,productId);
        Call<JsonResponse> addConversation=SmartAPI.getApiService().addConversation(session.getJWT(),conversation);
        addConversation.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equalsIgnoreCase("200 OK"))
                    {
                        conversationResponseList.add(new ConversationResponse(conversation.getMessage(),session.getusername(),date));
                       conversationAdapter.notifyDataSetChanged();
                       binding.etMessage.getText().clear();
                    }
                    }

            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });

    }
}