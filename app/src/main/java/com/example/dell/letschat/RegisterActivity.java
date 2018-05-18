package com.example.dell.letschat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText mDisplayName;
    private DatabaseReference mUserdatabase;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private Button mCreateBtn;

    private Toolbar mToolbar;

    private DatabaseReference mDatabase;
    private ProgressDialog mRegProgress;
    private FirebaseAuth mAuth;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mToolbar=(Toolbar)findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgress=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();


        mDisplayName=(TextInputEditText)findViewById(R.id.reg_display_name);
        mEmail=(TextInputEditText) findViewById(R.id.reg_email);
        mPassword=(TextInputEditText) findViewById(R.id.reg_password);
        mCreateBtn=(Button)findViewById(R.id.reg_create_button);

        mUserdatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name =mDisplayName.getText().toString();
                String email= mEmail.getText().toString();
                String password= mPassword.getText().toString();
                if(!TextUtils.isEmpty(display_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) )
                {
                    mRegProgress.setTitle("Registering..");
                    mRegProgress.setMessage("Wait a bit");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(display_name,email,password);
                }


            }
        });
    }

   private void register_user(final String display_name, String email, String password)
   {
       mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful())
               {

                   FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                   String uid=current_user.getUid();
                   mDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                   HashMap<String, String> userMap=new HashMap<>();
                   userMap.put("name",display_name);
                   userMap.put("image","default");
                   userMap.put("thumb_image","default");
                   mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful())
                          {
                              mRegProgress.dismiss();
                              String current_user_id=mAuth.getCurrentUser().getUid();
                              String token= FirebaseInstanceId.getInstance().getToken();

                              mUserdatabase.child(current_user_id).child("device_token").setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void aVoid) {
                                      Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
                                      mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                      startActivity(mainIntent);
                                      finish();
                                  }
                              });
                          }
                       }
                   });


               }
               else {
                   mRegProgress.hide();
                   Toast.makeText(RegisterActivity.this, "Cannot Sign in.Check the Details", Toast.LENGTH_LONG).show();
               }
           }
       });
    }
}
