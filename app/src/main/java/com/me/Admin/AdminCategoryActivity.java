package com.me.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.me.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView grocery, vegetable, fruits, other;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        grocery = findViewById(R.id.grocery);
        vegetable = findViewById(R.id.vegetables);
        fruits = findViewById(R.id.fruits);
        other = findViewById(R.id.other);

        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","grocery");
                startActivity(intent);
            }
        });
        vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","vegetable");
                startActivity(intent);
            }
        });
        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","fruits");
                startActivity(intent);
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","other");
                startActivity(intent);
            }
        });
    }
}