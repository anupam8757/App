package com.me.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private TextView grocery, vegetable, fruits, other,beverages;
    private TextView snacks_packed_food, beauty,cleaning,kitchen_garden_pets, non_veg;
    private TextView baby_care, bakery, restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        grocery = findViewById(R.id.grocery);
        vegetable = findViewById(R.id.vegetables);
        fruits = findViewById(R.id.fruits);
        beverages = findViewById(R.id.beverages);
        other = findViewById(R.id.other);

        snacks_packed_food = findViewById(R.id.snacks_packed_food);
        beauty = findViewById(R.id.beauty);
        cleaning = findViewById(R.id.cleaning);
        kitchen_garden_pets = findViewById(R.id.kgp);
        non_veg = findViewById(R.id.non_veg);

        baby_care = findViewById(R.id.baby_care);
        bakery = findViewById(R.id.bcd);
        restaurants = findViewById(R.id.rfc);

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
        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","beverages");
                startActivity(intent);
            }
        });

        snacks_packed_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","snacks_and_packed_food");
                startActivity(intent);
            }
        });
        beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","beauty_and_hygiene");
                startActivity(intent);
            }
        });
        kitchen_garden_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","kitchen_garden_pets");
                startActivity(intent);
            }
        });
        cleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","cleaning_and_household");
                startActivity(intent);
            }
        });
        non_veg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","non_veg");
                startActivity(intent);
            }
        });

        baby_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","baby_care");
                startActivity(intent);
            }
        });
        restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","restaurants_food_and_cuisines");
                startActivity(intent);
            }
        });
        bakery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","bakery_cakes_dairy");
                startActivity(intent);
            }
        });
    }
}