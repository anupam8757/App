package com.yukkti_gumla.Register;

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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.yukkti_gumla.Prevalent.Prevalent;
import com.yukkti_gumla.R;
import com.yukkti_gumla.home.MainActivity;
import com.yukkti_gumla.Model.User;

import java.util.HashMap;
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Otp.this,register.class);
        startActivity(intent);
        finish();
    }

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

        Log.d("Otp.java",phonenumber+" "+pass);

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
                else if(code.length() == 6){
                    verifyCode(code);
                }
                else{
                    editText.setError("Enter correct code.........");
                    editText.requestFocus();
                }
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
                            mAuth.signOut();
                            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child("Users").child(phonenumber).exists()) {
                                        User userData = snapshot.child("Users").child(phonenumber).getValue(User.class);
                                        Prevalent.currentOnlineUser = userData;
                                        Paper.book().write(Prevalent.userPhone, phonenumber);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            Paper.book().write(Prevalent.userPhone, phonenumber);
                            Paper.book().write(Prevalent.userPassword, phonenumber);

                            HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("phone",phonenumber);
                            userdataMap.put("password",pass);
                            userdataMap.put("name"," ");
                            userdataMap.put("address"," ");
                            userdataMap.put("email"," ");
                            rootRef.child("Users").child(phonenumber).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isComplete()){
                                                progressBar.setVisibility(View.GONE);
                                                ProcessPhoenix.triggerRebirth(Otp.this,new Intent(Otp.this, profile.class));
                                                Intent intent = new Intent(Otp.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else{
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Otp.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                                intent.addCategory(Intent.CATEGORY_HOME);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                        } else {
                            String message = "Something is wrong, we will fix it soon...";
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                                progressBar.setVisibility(View.GONE);
                                Paper.delete(Prevalent.userPhone);
                                Paper.delete(Prevalent.userPassword);
                                Prevalent.currentOnlineUser = null;
                            }
                            Toast.makeText(Otp.this,message,Toast.LENGTH_SHORT).show();
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
            Paper.clear(
                    Otp.this
            );
            Paper.delete(Prevalent.userPhone);
            Paper.delete(Prevalent.userPassword);
            Prevalent.currentOnlineUser = null;
            progressBar.setVisibility(View.GONE);
            Toast.makeText(Otp.this, e.getMessage(),Toast.LENGTH_LONG).show();
            startActivity(new Intent(Otp.this, register.class));
            finish();
        }
    };

}