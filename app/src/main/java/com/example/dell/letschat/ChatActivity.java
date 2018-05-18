package com.example.dell.letschat;

import android.app.AlertDialog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private  String mChatUser;
    private Toolbar mChatToolbar;

    private DatabaseReference mRootRef;

    private String mCurrentUserId;
    private FirebaseAuth mAuth;
    private EditText edt;
    private ImageButton send_btn;
    private RecyclerView mMessagesList;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatToolbar=(Toolbar)findViewById(R.id.chat_app_bar);
        edt=(EditText)findViewById(R.id.chat_message_view);
        send_btn=(ImageButton)findViewById(R.id.chat_send_btn);

        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Let's Chat");




        mRootRef= FirebaseDatabase.getInstance().getReference();

        mAuth=FirebaseAuth.getInstance();
        mCurrentUserId=mAuth.getCurrentUser().getUid();
        mChatUser=getIntent().getStringExtra("user_id");
        String userName=getIntent().getStringExtra("user_name");
        getSupportActionBar().setTitle(userName);


        mAdapter= new MessageAdapter(messagesList);
        mMessagesList=(RecyclerView)findViewById(R.id.messages_list);
        mLinearLayout=new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdapter);
        loadMessages();



        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();

            }
        });
    }

    private void loadMessages()
    {



        mRootRef.child("messages").child(mCurrentUserId).child(mChatUser).addChildEventListener(new ChildEventListener() {

            @Override

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message= dataSnapshot.getValue(Messages.class);
                messagesList.add(message);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void sendMessage()
    {
        String message=edt.getText().toString();
        if(!TextUtils.isEmpty(message))
        {
            String current_user_ref="messages/"+mCurrentUserId+"/"+mChatUser;
            String chat_user_ref="messages/"+mChatUser+"/"+mCurrentUserId;
            DatabaseReference user_message_push=mRootRef.child("messages").child(mCurrentUserId)
                    .child(mChatUser).push();
            String push_id=user_message_push.getKey();
            Map messageMap=new HashMap();
            messageMap.put("message",message);
            messageMap.put("from",mCurrentUserId);

            Map messageUserMap=new HashMap();
            messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
            messageUserMap.put(chat_user_ref+"/"+push_id,messageMap);

            edt.setText("");
            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if(databaseError!=null)
                    {

                    }
                }
            });


        }
    }
}
