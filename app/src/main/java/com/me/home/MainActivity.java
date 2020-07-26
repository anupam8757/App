package com.me.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private static final String TAG = "MainActivity";
    private TextView name,email;
    private DatabaseReference FetchDataRef;

    //main list view
    private GridView main_list_View;
     //    caeouselview variable

    private CarouselView carouselView;
    int[] sampleImages={R.drawable.backgroundgreen,R.drawable.backgroundgreen,R.drawable.backgroundgreen};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);


        // reference of the main layout ie drawer layout
        drawer = findViewById(R.id.drawer_layout);

        //   navigation bar toogling functionality
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.open_drawer,R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        creating the arraylist of type Main_list_item
        final ArrayList<Main_list_item> main_list_items=assign_main_item();

//
//        adapter knows how to create list items for each item in the list.
        com.me.home.MainAdapter adapter = new com.me.home.MainAdapter(this, main_list_items);

        main_list_View=findViewById(R.id.gridview);

        main_list_View.setAdapter(adapter);
        //  setting the adapter class
        main_list_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                com.me.home.Main_list_item main_list_item=main_list_items.get(position);
                String cat_name=main_list_item.getMain_item_name();
                Intent intent =new Intent(MainActivity.this, com.me.home.Catagories.class);
                intent.putExtra("cat_name",cat_name);
                startActivity(intent);
            }
        });
        //taking the reference of caouselView
        carouselView =findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
           imageView.setImageResource(sampleImages[position]);
            Toast.makeText(MainActivity.this, "carouselView clicked"+position, Toast.LENGTH_SHORT).show();
        }
        };



    private ArrayList<Main_list_item> assign_main_item() {

        ArrayList<Main_list_item> main_list_items=new ArrayList<Main_list_item>();
        main_list_items.add(new Main_list_item(R.drawable.backgroundgreen,"VEGETABLE"));
        main_list_items.add(new Main_list_item(R.drawable.backgroundgreen,"FRUITS"));
        main_list_items.add(new Main_list_item(R.drawable.backgroundgreen,"GROCERIES"));
        main_list_items.add(new Main_list_item(R.drawable.backgroundgreen,"WATER BOTTLES & DRINKS"));
        main_list_items.add(new Main_list_item(R.drawable.backgroundgreen,"MILK,DAIRY & BAKERY"));
        main_list_items.add(new Main_list_item(R.drawable.backgroundgreen,"MASALAS & MUSTARD OIL"));

        Toast.makeText(this,"Completed",Toast.LENGTH_SHORT).show();
        return main_list_items;
    }

//    to close the navigation bar when the task is completed
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return true;
    }
}