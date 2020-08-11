package com.me.Orders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.me.R;
import com.me.home.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {

    String currentDate, currentTime;
    TextView dateTime, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        price = findViewById(R.id.Orderprice);
        dateTime = findViewById(R.id.date_time);

        String total_price = getIntent().getStringExtra("total_price");

        price.setText(total_price);

        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        dateTime.setText(currentDate+"  "+currentTime);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
        startActivity(intent);
    }
}