package com.example.smartpasal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.R;
import com.example.smartpasal.Session.Session;
import com.example.smartpasal.model.Conversation;
import com.example.smartpasal.model.ConversationResponse;

import java.util.ArrayList;


public class ConversationAdapter  extends RecyclerView.Adapter<ConversationAdapter.MyViewholder> {

    private ArrayList<ConversationResponse> conversations;
    private Context context;

    public ConversationAdapter(ArrayList<ConversationResponse> conversations, Context context) {
        this.conversations = conversations;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_q_and_a,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        final ConversationResponse conversation=conversations.get(position);
        Session session=new Session(context);
        holder.tvMessage.setText(conversation.getMessage());
        holder.tvAsker.setText(conversation.getUser_name());
        if (conversation.getUser_name().equalsIgnoreCase(session.getusername()))
            holder.linearLayout.setGravity(Gravity.RIGHT);
        else
        {
            holder.tvMessage.setBackgroundColor(Color.parseColor("#F8F8F8"));
            holder.tvMessage.setTextColor(Color.BLACK);
        }




        holder.tvDate.setText(conversation.getDate());
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {
        TextView tvMessage,tvAsker,tvDate;
        LinearLayout linearLayout;



    public MyViewholder(@NonNull View itemView) {
        super(itemView);
        tvMessage=itemView.findViewById(R.id.tvMessage);
        tvAsker=itemView.findViewById(R.id.tvUser);
        tvDate=itemView.findViewById(R.id.tvAskedDate);
        linearLayout=itemView.findViewById(R.id.linearLayout);

    }
}
}
