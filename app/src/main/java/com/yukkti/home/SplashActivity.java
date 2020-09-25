package com.yukkti.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yukkti.Model.User;
import com.yukkti.Prevalent.Prevalent;
import com.yukkti.R;
import com.yukkti.Register.register;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkConnection();
        Paper.init(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            final String phone =  user.getPhoneNumber().substring(3,13);
            Log.d("SplashActivity"," "+phone);
            Log.d("SplashActivity"," "+Paper.book().read("User_Name"));
            Log.d("SplashActivity"," "+Paper.book().read("User_Email"));
            if(phone != null){
                DatabaseReference user_data= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

                user_data.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        String name = user.getName();
                        Paper.book().write("User_Name",name);
                        String email = user.getEmail();
                        Paper.book().write("User_Email",email);
                        Prevalent.currentOnlineUser = user;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
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

    private void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =  connectivityManager.getActiveNetworkInfo();
        if(networkInfo== null || !networkInfo.isConnected() || !networkInfo.isAvailable())
        {
            Dialog dialog= new Dialog(this);
            dialog.setContentView(R.layout.alert_dailog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            Button button = dialog.findViewById(R.id.internet_lost);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            dialog.show();
        }
    }
}