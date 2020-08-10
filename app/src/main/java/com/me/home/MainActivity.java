package com.me.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.Orders.OrderActivity;
import com.me.Prevalent.Prevalent;
import com.me.R;
import com.me.Register.profile;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,MainAdapter.OnItemClickListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private String presentName, presentEmail;

    private MainAdapter mAdapter;

    private List<Main_list_item> main_list_items;
    RecyclerView main_list_View;

    //    caeouselview variable
    CarouselView carouselView;

    int[] sampleImages = {R.drawable.backgroundgreen, R.drawable.backgroundgreen, R.drawable.backgroundgreen};

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference carousel_Reference;

    public MainActivity() throws IOException {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkconnection();
        // for internet connection lost


//       toolbar in main Activity is added
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Paper.init(this);

        // reference of the main layout ie drawer layout
        drawer = findViewById(R.id.drawer_layout);
//   the reference of the navigation bar
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        //   navigation bar toogling functionality
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//
        View headerView = navigationView.getHeaderView(0);
        TextView userNameHeaderView = headerView.findViewById(R.id.nav_header_name);
        TextView userEmailHeaderView = headerView.findViewById(R.id.nav_header_email);
        try {
            presentName = Prevalent.currentOnlineUser.getName();
            presentEmail = Prevalent.currentOnlineUser.getEmail();
            userNameHeaderView.setText(presentName);
            userEmailHeaderView.setText(presentEmail);

        } catch (NullPointerException e) {
            System.out.println("NullPointerException thrown!");
        } catch (RuntimeException e) {
            System.out.println("RuntimeException thrown!");
        }


        main_list_View = findViewById(R.id.main_recyclerView);
        main_list_View.setHasFixedSize(true);


//       setting the column of the gridView
        int number_of_column = 2;
//        assigning the reciclerView as GridView
        main_list_View.setLayoutManager(new GridLayoutManager(this, number_of_column));
//        assign the list as arraylist
        main_list_items = new ArrayList<>();

//         assigning the value into the main_list_item
        main_list_items = Data.assign_main_item();

        mAdapter = new MainAdapter(MainActivity.this, main_list_items);


        main_list_View.setAdapter(mAdapter);
        //  setting the adapter class
        mAdapter.setOnItemClickListener(this);


        //taking the reference of caouselView
        carouselView =findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

//    getting he reference of the FirebaseData base
        firebaseDatabase =FirebaseDatabase.getInstance();

//        getting the reference of the database carousel
        carousel_Reference=firebaseDatabase.getReference("carousel");

        carousel_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                  Log.d("image",dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("The read failed: " ,error.getMessage());

            }
        });

    }



    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
//            Toast.makeText(MainActivity.this, "carouselView clicked"+position, Toast.LENGTH_SHORT).show();
        }
    };



    @Override
    protected void onStart() {
        super.onStart();

    }

    //    to close the navigation bar when the task is completed
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    // starting line og the menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
//        reference of the serch item
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

        }

        return super.onOptionsItemSelected(item);
    }

//    end of the menu item


    //  this is for the overridden function to implement the functionality
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {

            case R.id.user_profile:
                intent = new Intent(MainActivity.this, profile.class);
                startActivity(intent);
                break;
            case R.id.app_logout:
                showPopup();
                break;
            case R.id.app_Orders:
                intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
                break;
            case R.id.app_share:
                Toast.makeText(this, "here i will give the link of google play ", Toast.LENGTH_LONG).show();
                break;
            case R.id.app_about:
                Toast.makeText(this, "this is app of e comerse", Toast.LENGTH_SHORT).show();

        }
        return true;
    }

    //    this will show the pop up message into the item
    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(Html.fromHtml("<font color='#FF7F27'>Want to Logout?</font>"));
        builder.setCancelable(false);
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        //Set negative button background
        nbutton.setBackgroundColor(Color.parseColor("#ffffff"));
        //Set negative button text color
        nbutton.setTextColor(Color.parseColor("#1704FF"));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        //Set positive button background
        pbutton.setBackgroundColor(Color.parseColor("#ffffff"));
        //Set positive button text color
        pbutton.setTextColor(Color.parseColor("#1704FF"));
    }

    private void logout() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
//  this is end of the navigation functionality

    //   what to do when the the item is clicked
    @Override
    public void onItemClick(int position, TextView main_name) {
        Intent intent = new Intent(MainActivity.this, Catagories.class);
        intent.putExtra("cat_name", main_name.getText());
        startActivity(intent);
    }
    public void checkconnection()
    {
     ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
     NetworkInfo networkInfo =  connectivityManager.getActiveNetworkInfo();
     if(networkInfo== null || !networkInfo.isConnected() || !networkInfo.isAvailable())
     {
         Dialog dialog= new Dialog(this);
         dialog.setContentView(R.layout.alert_dailog);
         dialog.setCancelable(false);
         dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
         dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
         Button button = dialog.findViewById(R.id.internet_lost);
         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 recreate();
             }
         });
         dialog.show();
     }
    }

}
