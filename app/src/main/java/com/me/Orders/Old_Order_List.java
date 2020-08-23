package com.me.Orders;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.me.R;

public class Old_Order_List extends AppCompatActivity {

    private TextView mTextView;
    private String Date_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old__order__list);

        mTextView = (TextView) findViewById(R.id.texttt);
        Date_time = getIntent().getStringExtra("date_time");
        mTextView.setText(Date_time);
    }
}