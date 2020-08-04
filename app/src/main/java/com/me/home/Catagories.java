package com.me.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.R;
import com.me.cart.Cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Catagories extends AppCompatActivity implements Cat_Adapter.OnItemClickListener {
    private Toolbar  cat_toolbar;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference productrefence;
    private DatabaseReference product_child;
    private RecyclerView cat_recyclerView;
    private List<Cat_list> cat_lists;
    private Cat_Adapter cat_Adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagories);


        cat_toolbar=findViewById(R.id.cat_toolbar);
        setSupportActionBar(cat_toolbar);
//        adding the title which we will get from the main activity
        String cat_name = getIntent().getStringExtra("cat_name");
        Objects.requireNonNull(getSupportActionBar()).setTitle(cat_name);

//        display the back button on the activity to go back to home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        get the reference of the cat_recylerView

        cat_recyclerView=findViewById(R.id.cat_recyclerView);
        cat_recyclerView.setHasFixedSize(true);
        cat_lists=new  ArrayList<>();

//        the no of column in the layout is 2
        int no_of_column=2;
        cat_recyclerView.setLayoutManager(new GridLayoutManager(this,no_of_column));


//        implement the database
        firebaseDatabase=FirebaseDatabase.getInstance();
        productrefence=firebaseDatabase.getReference("Products");

        switch (cat_name){

            case "VEGETABLE":
                product_child=productrefence.child("vegetable");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            case "FRUITS":
                product_child=productrefence.child("fruits");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            case "GROCERIES":
                product_child=productrefence.child("grocery");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            case "BAKERY,CAKES & DAIRY":
                product_child=productrefence.child("bakery_cakes_dairy");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            case "RESTAURANT FOODS & CUISINES":
                product_child=productrefence.child("restaurants_food_and_cuisines");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            case "BEVERAGES":
                product_child=productrefence.child("beverages");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;

            case "SNACKS & PACKED FOOD":
                product_child=productrefence.child("snacks_and_packed_food");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            case "BEAUTY & HYGIENE":
                product_child=productrefence.child("beauty_and_hygiene");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            case "CLEANING & HOUSEHOLD":
                product_child=productrefence.child("cleaning_and_household");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            case "KITCHEN,GARDEN & PETS":
                product_child=productrefence.child("kitchen_garden_pets");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            case "EGG,MEAT & FISH":
                product_child=productrefence.child("non_veg");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            case "BABY CARE":
                product_child=productrefence.child("baby_care");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            default:
                product_child=productrefence.child("other");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;

        }

    }
// this is method which will fetch the data from the server according to its child
    private void fetchdata(DatabaseReference product_child) {
        product_child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot :snapshot.getChildren()){
                    Cat_list cat_list=postSnapshot.getValue(Cat_list.class);
//                    adding the item to the list which we get from the data base
                    cat_lists.add(cat_list);
                    Log.d("fetched",cat_list.getName());
                }
                cat_Adapter=new Cat_Adapter(Catagories.this,cat_lists);
                cat_recyclerView.setAdapter(cat_Adapter);
                cat_Adapter.setOnItemClickListener(Catagories.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Catagories.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_categaries, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "cart" menu option

            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemClick(int position, TextView main_name, String pid) {
        Intent intent = new Intent(Catagories.this, Cart.class);
        intent.putExtra("pid",pid);
        startActivity(intent);
    }
}