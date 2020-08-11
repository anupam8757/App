package com.me.Orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.me.home.Catagories;
import com.me.home.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OrderActivity extends AppCompatActivity {

    private String currentDate, currentTime;
    private TextView dateTime, price;
    private RecyclerView orderRecyclerView;
    List<Order_Details> order_details;
    DatabaseReference ordersRef;
    OrderAdapter orderAdapter;
    static String user_phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Paper.init(OrderActivity.this);

        user_phone = Paper.book().read(Prevalent.userPhone);

        Log.d("..............."," "+user_phone);
        orderRecyclerView = findViewById(R.id.order_recyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        order_details = new ArrayList<>();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user_phone);

        price = findViewById(R.id.Orderprice);
        dateTime = findViewById(R.id.date_time);

        String total_price = getIntent().getStringExtra("total_price");

        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        price.setText(total_price);
        dateTime.setText(currentDate+"  "+currentTime);

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Order_Details data = dataSnapshot.getValue(Order_Details.class);
                    order_details.add(data);
                }
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