package com.me.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.Model.User;
import com.me.Prevalent.Prevalent;
import com.me.R;
import com.me.Register.register;
import com.me.login;

import java.util.Objects;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Paper.init(this);
        final String phone = ""+Paper.book().read(Prevalent.userPhone);
        Log.d("SplashActivity"," "+phone);
        if(phone != null){
            DatabaseReference user_data= FirebaseDatabase.getInstance().getReference();

            user_data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.child("Users").child(phone).getValue(User.class);
                    Prevalent.currentOnlineUser = user;
                    Log.d("SplashActivity"," "+user.getName());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        new Handler().postDelayed(new Runnable() {  // Changes activity after 2 seconds
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, register.class);
                startActivity(i);
                finish();
            }
        }, 3000);



    }
}