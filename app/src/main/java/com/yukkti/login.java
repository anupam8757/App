package com.yukkti;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.yukkti.Admin.AdminCategoryActivity;
import com.yukkti.Model.User;
import com.yukkti.Prevalent.Prevalent;
import com.yukkti.Register.profile;
import com.yukkti.home.MainActivity;

import io.paperdb.Paper;

public class login extends AppCompatActivity {

    private Button loginBtn;
    private TextView forget_password,error_text;
    private EditText phone, pass;
    private ProgressBar progressBar;
    private ImageView fb, instagram;
    private final static String parentDBname = "Users";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkConnection();
        Paper.init(this);

        loginBtn = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.login_progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        fb = findViewById(R.id.Facebook);
        instagram = findViewById(R.id.Instagram);

        forget_password = findViewById(R.id.forget_password);

        pass = findViewById(R.id.editTextPassword);
        phone = findViewById(R.id.Phone);
        error_text=findViewById(R.id.error_text);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/shubhamkumarbala/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/yukkti_home_delivery?r=nametag");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phone.getText().toString();
                String password = pass.getText().toString();
                if (number.isEmpty() || number.length()<10) {
                    phone.setError("Enter valid number");
                    phone.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    pass.setError("Enter password");
                    pass.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                validatePhone_Password(number,password);
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                error_text.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {
//                error_text.setVisibility(View.GONE);

            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                error_text.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });
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

    private void validatePhone_Password(final String number, final String password) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String adminPass = "";
                try{
                adminPass = snapshot.child("Admin").child(number).child("password").getValue().toString();
                }
                catch (Exception e){}
                if(snapshot.child("Admin").child(number).exists() && adminPass.equals(password)){
                    Paper.book().write(Prevalent.userPhone, password);
                    Paper.book().write(Prevalent.userPassword, number);
                        progressBar.setVisibility(View.GONE);
                        error_text.setVisibility(View.GONE);
                        Intent intent = new Intent(login.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                else if(snapshot.child(parentDBname).child(number).exists()){
                    User userData = snapshot.child(parentDBname).child(number).getValue(User.class);
                    if(userData.getPhone().equals(number)){
                        if(userData.getPassword().equals(password)){
                            Paper.book().write(Prevalent.userPhone, password);
                            Paper.book().write(Prevalent.userPassword, number);
                            progressBar.setVisibility(View.GONE);
                            Prevalent.currentOnlineUser = userData;
                            ProcessPhoenix.triggerRebirth(login.this,new Intent(login.this, profile.class));
                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
//                            Toast.makeText(login.this,"Incorrect Password! Please try Again.",Toast.LENGTH_SHORT).show();
                            error_text.setVisibility(View.VISIBLE);
                            error_text.setTextColor(Color.RED);
                            error_text.setText("Incorrect Password! Please try Again");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
                else{
                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(login.this,"Incorrect Number! Please register first.",Toast.LENGTH_SHORT).show();
                    error_text.setVisibility(View.VISIBLE);
                    error_text.setTextColor(Color.RED);
                    error_text.setText("Incorrect Number! Please register first");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}