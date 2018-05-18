package com.example.dell.letschat;
import java.util.List;

import android.app.Notification;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by DELL on 11/12/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> mMessageList;

    private FirebaseAuth mAuth;
    public MessageAdapter(List<Messages> mMessageList)
    {
        this.mMessageList=mMessageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout,parent,false);
        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText;
        public MessageViewHolder(View view){
            super(view);
            messageText=(TextView)view.findViewById(R.id.message_text_layout);

        }
    }
    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {


        mAuth = FirebaseAuth.getInstance();
        String current_user_id = mAuth.getCurrentUser().getUid();
        Messages c = mMessageList.get(position);
        String from_user = c.getFrom();
         if (from_user.equals(current_user_id)) {

          holder.messageText.setBackgroundColor(Color.WHITE);
          holder.messageText.setTextColor(Color.BLACK);

        } else
        {

            holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.WHITE);
        }
        holder.messageText.setText(c.getMessage());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
