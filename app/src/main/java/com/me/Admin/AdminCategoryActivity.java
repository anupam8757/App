package com.me.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.R;
import com.me.home.MainActivity;

public class AdminCategoryActivity extends AppCompatActivity {

    private TextView grocery, vegetable, fruits, other,beverages;
    private TextView snacks_packed_food, beauty,cleaning,kitchen_garden_pets, non_veg;
    private TextView baby_care, bakery, restaurants;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminCategoryActivity.this, MainActivity.class));
        finish();
    }

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
                finish();
            }
        });
        vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","vegetable");
                startActivity(intent);
                finish();
            }
        });
        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","fruits");
                startActivity(intent);
                finish();
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","other");
                startActivity(intent);
                finish();
            }
        });
        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","beverages");
                startActivity(intent);
                finish();
            }
        });

        snacks_packed_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","snacks_and_packed_food");
                startActivity(intent);
                finish();
            }
        });
        beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","beauty_and_hygiene");
                startActivity(intent);
                finish();
            }
        });
        kitchen_garden_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","kitchen_garden_pets");
                startActivity(intent);
                finish();
            }
        });
        cleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","cleaning_and_household");
                startActivity(intent);
                finish();
            }
        });
        non_veg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","non_veg");
                startActivity(intent);
                finish();
            }
        });

        baby_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","baby_care");
                startActivity(intent);
                finish();
            }
        });
        restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","restaurants_food_and_cuisines");
                startActivity(intent);
                finish();
            }
        });
        bakery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,Admin_Add_New_Product_Activity.class);
                intent.putExtra("category","bakery_cakes_dairy");
                startActivity(intent);
                finish();
            }
        });
    }
}