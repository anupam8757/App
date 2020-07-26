package com.me.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.R;
import com.me.Model.User;

public class profile extends AppCompatActivity {

    private TextView flag;
    private EditText name,email,phone,address;
    private Button register;
    private User user;

    private DatabaseReference FetchDataRef, UploadDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        flag = findViewById(R.id.userId);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.number);
        address = findViewById(R.id.address);
        register = findViewById(R.id.register);
        final String userId = getIntent().getStringExtra("userId");

        FetchDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        FetchDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String n = snapshot.child("phone").getValue().toString();
                phone.setText(n);
                phone.setEnabled(false);
                n = snapshot.child("email").getValue().toString();
                email.setText(n);
                n = snapshot.child("address").getValue().toString();
                address.setText(n);
                n = snapshot.child("name").getValue().toString();
                name.setText(n);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                UploadDataRef.child("name").setValue(name.getText().toString());
                UploadDataRef.child("email").setValue(email.getText().toString());
                UploadDataRef.child("address").setValue(address.getText().toString());

                flag.setText("Update Completed....");
            }
        });
    }
}