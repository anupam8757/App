package com.me.Orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.Prevalent.Prevalent;
import com.me.R;
import com.me.home.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.paperdb.Paper;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView orderRecyclerView;
    List<Order_Details> order_details;
    DatabaseReference ordersRef;
    OrderAdapter orderAdapter;
    static String user_phone = "";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Paper.init(OrderActivity.this);

        user_phone = Paper.book().read(Prevalent.userPhone);
        toolbar = findViewById(R.id.order_toolbar);
        setSupportActionBar(toolbar);

        Log.d("..............."," "+user_phone);
        orderRecyclerView = findViewById(R.id.order_recyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        order_details = new ArrayList<>();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user_phone);


        String total_price = getIntent().getStringExtra("total_price");

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Order_Details data = dataSnapshot.getValue(Order_Details.class);
                    order_details.add(data);
                }
                Collections.reverse(order_details);
                orderAdapter = new OrderAdapter(order_details);
                orderRecyclerView.setAdapter(orderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
        startActivity(intent);
    }
}