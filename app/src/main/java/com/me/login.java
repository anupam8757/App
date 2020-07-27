package com.me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.Model.User;
import com.me.Prevalent.Prevalent;
import com.me.home.MainActivity;

import io.paperdb.Paper;

public class login extends AppCompatActivity {

    private Button loginBtn;
    private TextView forget_password;
    private EditText phone, pass;
    private ProgressBar progressBar;
    private final static String parentDBname = "Users";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);

        loginBtn = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.login_progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        forget_password = findViewById(R.id.forget_password);

        pass = findViewById(R.id.editTextPassword);
        phone = findViewById(R.id.Phone);

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

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    private void validatePhone_Password(final String number, final String password) {

        Paper.book().write(Prevalent.userPhone, number);
        Paper.book().write(Prevalent.userPassword, password);

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDBname).child(number).exists()){
                    User userData = snapshot.child(parentDBname).child(number).getValue(User.class);
                    if(userData.getPhone().equals(number)){
                        if(userData.getPassword().equals(password)){
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(login.this,"Incorrect Password! Please try Again.",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(login.this,"Incorrect Number! Please register first.",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}