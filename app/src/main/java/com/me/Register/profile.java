package com.me.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.Prevalent.Prevalent;
import com.me.R;
import com.me.Model.User;

import io.paperdb.Paper;

public class profile extends AppCompatActivity {


    private TextView flag;
    private EditText name,email,phone,address;
    private Button register;
    private User user;
    private ImageView imageView;
    // long maxid=0;
    private DatabaseReference FetchDataRef, UploadDataRef;
    String full_nameDB,Email_DB,Address_DB,phone_db,pass_db;//this we will fetch from database
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        flag = findViewById(R.id.userId);
        Paper.init(this);
        phone_db= Paper.book().read(Prevalent.userPhone);
       // pass_db=Paper.book().read(Prevalent.userPassword);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.number);
        phone.setText(phone_db);
        phone.setEnabled(false);
        address = findViewById(R.id.address);
        register = findViewById(R.id.register);
        user = new User();
        FetchDataRef=FirebaseDatabase.getInstance().getReference().child("Users").child(phone_db);
        FetchDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                   full_nameDB=snapshot.child("name").getValue(String.class);
                   Email_DB = snapshot.child("email").getValue(String.class);
                   Address_DB=snapshot.child("address").getValue(String.class);
                   pass_db=snapshot.child("password").getValue(String.class);
                    Log.d("snapshor", "onDataChange: "+full_nameDB);
                    Log.d("snapshor", "onDataChange: "+Email_DB);
                    Log.d("snapshor", "onDataChange: "+Address_DB);
                    Log.d("snapshor", "onDataChange: "+phone_db);
                   name.setText(full_nameDB);
                   email.setText(Email_DB);
                   address.setText(Address_DB);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        UploadDataRef=FirebaseDatabase.getInstance().getReference().child("Users");
         UploadDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final  String Name= name.getText().toString().trim();
                if(TextUtils.isEmpty(Name))
                {
                    name.setError("Please Enter valid Name");
                    name.requestFocus();
                    return;
                }
                //    int age = Integer.parseInt();
                final String Email = email.getText().toString().trim();
                if(TextUtils.isEmpty(Email))
                {
                    email.setError("Please Enter Valid Email");
                    email.requestFocus();

                    return;
                }


                final String Address= address.getText().toString().trim();
                if(TextUtils.isEmpty(Address))
                {
                    address.setError("Address Can't be Empty ");
                    address.requestFocus();
                    //  Name.setText("Name");
                    return;
                }
                user.setName(name.getText().toString().trim());
                user.setEmail(email.getText().toString().trim());
                user.setPhone(phone_db);
                user.setPassword(pass_db);
                user.setAddress(address.getText().toString().trim());
                UploadDataRef.child(phone_db).setValue(user);
                Toast.makeText(getApplicationContext(),"DATA UPDATED SUCCESSFULLY ",Toast.LENGTH_LONG).show();
            }
        });

        final String userId = getIntent().getStringExtra("userId");


    }

}