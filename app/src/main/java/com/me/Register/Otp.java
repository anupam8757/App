package com.me.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.me.Prevalent.Prevalent;
import com.me.R;
import com.me.home.MainActivity;
import com.me.Model.User;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class Otp extends AppCompatActivity {

    private String verificationId, userId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private TextView tv;
    private String phonenumber, pass;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Paper.init(this);

        mAuth = FirebaseAuth.getInstance();

        tv = findViewById(R.id.pno);
        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextNumber);

        phonenumber = getIntent().getStringExtra("phonenumber");
        pass = getIntent().getStringExtra("password");
        tv.setText(phonenumber);
        sendVerificationCode(phonenumber);

        findViewById(R.id.Otpbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editText.getText().toString().trim();
                if(code.isEmpty() || code.length()<6){
                    editText.setError("Enter Code.........");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        progressBar.setVisibility(View.VISIBLE);
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userId = mAuth.getUid();

                            Paper.book().write(Prevalent.userPhone, phonenumber);
                            Paper.book().write(Prevalent.userPassword, pass);

                            HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("phone",phonenumber);
                            userdataMap.put("password",pass);
                            userdataMap.put("name","no value");
                            userdataMap.put("address","no value");
                            userdataMap.put("email","no value");
                            rootRef.child("Users").child(phonenumber).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isComplete()){
                                                progressBar.setVisibility(View.GONE);
                                                Intent intent = new Intent(Otp.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                progressBar.setVisibility(View.GONE);
                                                return;
                                            }
                                        }
                                    });
                        } else {
                            try{
                            Toast.makeText(Otp.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }catch (Exception e){
                                Log.d("Otp"," "+e.getMessage());}
                        }
                    }
                });
    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" +number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;

        }

        //auto verification
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Otp.this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };
}