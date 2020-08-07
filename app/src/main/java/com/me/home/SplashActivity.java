package com.me.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.me.R;
import com.me.Register.register;
import com.me.login;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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