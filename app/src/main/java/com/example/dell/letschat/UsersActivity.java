package com.example.dell.letschat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

       mToolbar=(Toolbar)findViewById(R.id.users_appBar);
       setSupportActionBar(mToolbar);
       getSupportActionBar().setTitle("All Users");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");



        mUsersList=(RecyclerView)findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(UsersActivity.this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mUsersDatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder usersviewHolder, Users users, int position) {

                usersviewHolder.setName(users.getName());

                usersviewHolder.setUserImage(users.getThumb_image(),getApplicationContext());

                final String user_id=getRef(position).getKey();
                usersviewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent=new Intent(UsersActivity.this,ProfileActivity.class);

                        profileIntent.putExtra("user_id",user_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };
        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView =itemView;
        }

       public void setName(String name){
            TextView userNameView= (TextView)mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

       }

       public void setUserImage(String thumb_image, Context context)
       {
           CircleImageView userImageView=(CircleImageView)mView.findViewById(R.id.user_single_image);
           Picasso.with(context).load(thumb_image).placeholder(R.drawable.pic).into(userImageView);
       }
    }

}
