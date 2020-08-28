package com.yukkti_gumla;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.yukkti_gumla.Prevalent.Prevalent;
import com.yukkti_gumla.R;
import com.yukkti_gumla.Register.Otp;

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
        checkconnection();
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


                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(ForgotPassword.this, Otp.class);
                intent.putExtra("phonenumber", phone_number);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });
    }

    private void checkconnection() {
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