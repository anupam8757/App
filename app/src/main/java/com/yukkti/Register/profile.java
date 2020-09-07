package com.yukkti.Register;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yukkti.Model.User;
import com.yukkti.Prevalent.Prevalent;
import com.yukkti.R;
import com.yukkti.home.Cart;
import com.yukkti.home.MainActivity;

import java.util.regex.Pattern;

import io.paperdb.Paper;

public class profile extends AppCompatActivity {

    private TextView flag,user_name,user_phone;
    private EditText name,email,locality,district,state,pincode;
    private Button register;
    private static User user = new User();
    private ProgressBar progressBar;
    private String GumlaPin = "835207";
    private DatabaseReference FetchDataRef, UploadDataRef;
    String full_nameDB,Email_DB,phone_db,pass_db,locality_DB,district_DB,state_DB,pincode_DB;//this we will fetch from database

    @Override
    public void onBackPressed() {
        final  String Name= name.getText().toString().trim();
        if(TextUtils.isEmpty(Name))
        {
            name.setError("Please Enter valid Name");
            name.requestFocus();
            return;
        }
        //    int age = Integer.parseInt();
        final String Email = email.getText().toString().trim();
        if(TextUtils.isEmpty(Email) || !validEmail(Email))
        {
            email.setError("Please Enter Valid Email");
            email.requestFocus();
            return;
        }
        final String pin = pincode.getText().toString().trim();
        final String Address= locality.getText().toString().trim();
        if(TextUtils.isEmpty(Address) || locality.length()<6)
        {
            locality.setError("Enter valid address!");
            locality.requestFocus();
            return;
        }

        else if(!Email.isEmpty() && !Name.isEmpty() && !Address.isEmpty() && !pin.isEmpty()){
            Intent i = new Intent(profile.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else {
            Intent i = new Intent(profile.this, profile.class);
            startActivity(i);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInternetconnection();
        setContentView(R.layout.activity_profile);
        Paper.init(this);
        phone_db= Paper.book().read(Prevalent.userPhone);
        Log.d("profile.java",phone_db);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        locality = findViewById(R.id.locality);
        pincode=findViewById(R.id.pincode);

        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.profile_progress_bar);
        progressBar.setVisibility(View.GONE);

//
        user_name=findViewById(R.id.profile_user_name);
        user_phone=findViewById(R.id.profile_user_phone);
        user_phone.setText(phone_db);


        UploadDataRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FetchDataRef = UploadDataRef.child(phone_db);
        FetchDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                full_nameDB=snapshot.child("name").getValue(String.class);
                Email_DB = snapshot.child("email").getValue(String.class);
                locality_DB=snapshot.child("address").getValue(String.class);
                pass_db=snapshot.child("password").getValue(String.class);
                name.setText(full_nameDB);
                email.setText(Email_DB);
                try{
                    String[] arrOfStr = locality_DB.split("#", 2);
                    locality.setText(arrOfStr[0]);
                    pincode.setText(arrOfStr[1]);}catch (Exception e){}
//
                user_name.setText(full_nameDB);
                User user1 = snapshot.getValue(User.class);
                Prevalent.currentOnlineUser = user1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Profile.java","when upload clicks");
                final  String Name= name.getText().toString().trim();
                if(TextUtils.isEmpty(Name))
                {
                    name.setError("Please Enter valid Name");
                    name.requestFocus();
                    return;
                }

                final String Email = email.getText().toString().trim();
                if(TextUtils.isEmpty(Email) || !validEmail(Email))
                {
                    email.setError("Please Enter Valid Email");
                    email.requestFocus();
                    return;
                }
                final String pin = pincode.getText().toString().trim();
                if(checkPin(pin))
                {
                    pincode.setError("Please Enter valid Pincode");
                    pincode.requestFocus();
                    return;
                }

                final String Address= locality.getText().toString().trim();
                if(TextUtils.isEmpty(Address) || locality.length()<6)
                {
                    locality.setError("Enter valid address!");
                    locality.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                user.setName(name.getText().toString().trim());
                user.setEmail(email.getText().toString().trim());
                user.setPhone(phone_db);
                user.setPassword(pass_db);
                user.setAddress(locality.getText().toString().trim()+"#"+pin);

                progressBar.setVisibility(View.GONE);
                Prevalent.currentOnlineUser = user;
                UploadDataRef.child(phone_db).setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getApplicationContext(),"DATA UPDATED SUCCESSFULLY ",Toast.LENGTH_SHORT).show();
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(profile.this, MainActivity.class);
                        startActivity(intent);
                    }
                },100);
            }
        });

        final String userId = getIntent().getStringExtra("userId");


    }

    private void checkInternetconnection() {
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
    /**
     * validate your email address format. Ex-akhi@mani.com
     */
    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    private boolean checkPin(String pin){
        if( pin.isEmpty()){
            return true;
        }
        else if(pin.equals(GumlaPin)){
            return false;
        }
        else{
            // Initializing a new alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Set a title for alert dialog
            // Define the title color to red
            //builder.setTitle(Html.fromHtml("<font color='#ff0000'>Say Hello!</font>"));

            // Another way to change alert dialog title text color

            // Specify the alert dialog title
            String titleText = "SORRY !";

            // Initialize a new foreground color span instance
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

            // Initialize a new spannable string builder instance
            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

            // Apply the text color span
            ssBuilder.setSpan(
                    foregroundColorSpan,
                    0,
                    titleText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            // Set the alert dialog title using spannable string builder
            builder.setTitle(ssBuilder);

            // Show a message on alert dialog
            builder.setMessage(Html.fromHtml("<font color='#000000'><h5>We have not started at your pincode "));

            // Set the positive button
            builder.setPositiveButton("Ok",null);


            // Create the alert dialog
            AlertDialog dialog = builder.create();

            // Finally, display the alert dialog
            dialog.show();
            Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            //Set positive button background
            pbutton.setBackgroundColor(Color.parseColor("#ffffff"));
            //Set positive button text color
            pbutton.setTextColor(Color.parseColor("#1704FF"));
        }
        return true;
    }
}