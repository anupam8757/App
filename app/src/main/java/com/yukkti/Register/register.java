package com.yukkti.Register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yukkti.Prevalent.Prevalent;
import com.yukkti.R;
import com.yukkti.home.MainActivity;

import java.util.Objects;

import io.paperdb.Paper;

public class register extends AppCompatActivity {

    private EditText phone;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.register_progressbar);
        progressBar.setVisibility(View.GONE);
        phone = findViewById(R.id.editTextPhone);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user !=null){
            Paper.book().write(Prevalent.userPhone, Objects.requireNonNull(user.getPhoneNumber().substring(3,13)));
            startActivity(new Intent(register.this, MainActivity.class));
            finish();
        }

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
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

        progressBar.setVisibility(View.VISIBLE);
        validatePhoneNumber(number);
    }

    private void validatePhoneNumber(final String number) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(register.this, Otp.class);
            intent.putExtra("phonenumber", number);
            startActivity(intent);
            finish();
        }else{
            Paper.book().write(Prevalent.userPhone, Objects.requireNonNull(user.getPhoneNumber().substring(3,13)));
            startActivity(new Intent(register.this, MainActivity.class));
            finish();
        }

    }


}