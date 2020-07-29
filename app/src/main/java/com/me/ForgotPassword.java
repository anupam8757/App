package com.me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.me.Prevalent.Prevalent;
import com.me.Register.Otp;
import com.me.Register.register;

import io.paperdb.Paper;

public class ForgotPassword extends AppCompatActivity {

    private EditText phone, pass;
    private ProgressBar progressBar;
    private Button button;

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
        setContentView(R.layout.activity_forgot_password);

        phone = findViewById(R.id.forgot_password_phone);
        pass = findViewById(R.id.forgot_password_password);

        progressBar = findViewById(R.id.forgot_password_progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        button = findViewById(R.id.forgot_password_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = phone.getText().toString();
                String password = pass.getText().toString();

                if (phone_number.isEmpty() || phone_number.length()<10) {
                    phone.setError("Enter valid number");
                    phone.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    pass.setError("Enter password");
                    pass.requestFocus();
                    return;
                }
                Paper.book().write(Prevalent.userPhone, phone_number);
                Paper.book().write(Prevalent.userPassword, password);

                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(ForgotPassword.this, Otp.class);
                intent.putExtra("phonenumber", phone_number);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });
    }
}