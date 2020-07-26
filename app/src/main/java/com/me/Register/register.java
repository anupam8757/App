package com.me.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.Prevalent.Prevalent;
import com.me.R;
import com.me.home.MainActivity;
import com.me.login;

import java.util.HashMap;

import io.paperdb.Paper;

public class register extends AppCompatActivity {

    private EditText phone,pass;
    private ProgressBar progressBar;
    @Override
    protected void onStart() {
        Paper.init(this);
        super.onStart();
        String phone = Paper.book().read(Prevalent.userPhone);
        String password = Paper.book().read(Prevalent.userPassword);
        if (phone != null && password!= null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressBar = findViewById(R.id.register_progressbar);
        progressBar.setVisibility(View.GONE);

        pass = findViewById(R.id.editTextTextPassword);
        phone = findViewById(R.id.editTextPhone);



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAccount();

            }
        });

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
            }
        });
    }

    private void createAccount() {
        String number = phone.getText().toString();
        if (number.isEmpty() || number.length() < 10) {
            phone.setError("Enter valid number");
            phone.requestFocus();
            return;
        }
        String password = pass.getText().toString();
        if (password.isEmpty() || password.length() < 6) {
            pass.setError("Enter strong password");
            pass.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        validatePhoneNumber(number, password);
    }

    private void validatePhoneNumber(final String number, final String password) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.child("Users").child(number).exists()){
                    Intent intent = new Intent(register.this, Otp.class);
                    intent.putExtra("phonenumber", number);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(register.this, login.class);
                    intent.putExtra("phonenumber", number);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}