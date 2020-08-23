package com.me.Orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.Prevalent.Prevalent;
import com.me.R;
import com.me.home.Cart_list;

import java.util.ArrayList;

import io.paperdb.Paper;

public class Old_Order_List extends AppCompatActivity {
    DatabaseReference ordersRef;
    private String user_phone;
    private TextView mTextView;
    private String Date_time;
    ArrayList<Cart_list> cart_lists;
    ArrayList<String> key = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old__order__list);
        Paper.init(this);
        user_phone = Paper.book().read(Prevalent.userPhone);
        Date_time = getIntent().getStringExtra("date_time");
        mTextView = (TextView) findViewById(R.id.texttt);
        mTextView.setText(Date_time);

        cart_lists = new ArrayList<>();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user_phone).child(Date_time).child("Cart");

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    key.add(dataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        try{
            String name = cart_lists.get(cart_lists.size()-1).getName();
            Log.d("Cart product name", name);
        }catch (Exception e){}
    }
}