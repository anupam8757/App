package com.yukkti_gumla.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.yukkti_gumla.AboutUs;
import com.yukkti_gumla.Orders.Order_History;
import com.yukkti_gumla.Prevalent.Prevalent;
import com.yukkti_gumla.R;
import com.yukkti_gumla.Register.profile;
import com.yukkti_gumla.login;

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
    private RecyclerView main_list_View;
    private DatabaseReference user_reference;
    private TextView textCartItemCount;
    private int mCartItemCount = 0;
    public static String user_phone;
    private FirebaseDatabase firebaseDatabase;
    private ImageSlider image_slider;
    public MainActivity() throws IOException {

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("catogaroes","in onstart");
        firebaseDatabase= FirebaseDatabase.getInstance();
//        getting the reference of the Cart
        DatabaseReference cartrefence = firebaseDatabase.getReference("Cart");
//        here we will pass the user phone number and from that we fetch of
//        particular user
        user_reference = cartrefence.child(user_phone);
        user_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get total available quest
                int size = (int) dataSnapshot.getChildrenCount();
                mCartItemCount=size;
                setupBadge();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkconnection();
        // for internet connection lost
        //for Notification
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel=new NotificationChannel("MyNotification","MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager= getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            else
            {
                Toast.makeText(this,"Problem Here",Toast.LENGTH_LONG).show();
            }
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       String msg="SuccessFull";
                       if(!task.isSuccessful())
                       {
                           msg="Failed";
                       }
                        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                });
        firebaseDatabase= FirebaseDatabase.getInstance();
//        getting the reference of the Cart
        DatabaseReference cartrefence = firebaseDatabase.getReference("Cart");
        Paper.init(this);
        user_phone = Paper.book().read(Prevalent.userPhone);
        user_reference = cartrefence.child(user_phone);

//       toolbar in main Activity is added
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        setcart();


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

        //    getting he reference of the FirebaseData base
            firebaseDatabase =FirebaseDatabase.getInstance();

//        getting the reference of the database carousel
        image_slider=findViewById(R.id.image_slider);
        final List<SlideModel> slideArray=new ArrayList<>();
         firebaseDatabase.getReference().child("carousel")
                 .addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         for(final DataSnapshot data :snapshot.getChildren()){
                           slideArray.add(new SlideModel(data.child("url").getValue().toString(), data.child("title").getValue().toString(),ScaleTypes.FIT));
//
                           image_slider.setImageList(slideArray,ScaleTypes.FIT);
//                           add the listener
                             image_slider.setItemClickListener(new ItemClickListener() {
                                 @Override
                                 public void onItemSelected(int i) {
                                      String name= slideArray.get(i).getTitle().trim();
                                     Intent intent = new Intent(MainActivity.this, Catagories.class);
                                     intent.putExtra("cat_name",name);
                                     startActivity(intent);
                                 }
                             });

                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });

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

        final MenuItem cartItem = menu.findItem(R.id.action_cart_home);
        View actionView = cartItem.getActionView();
        textCartItemCount =actionView.findViewById(R.id.cart_badge);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(cartItem);
            }
        });
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
            case R.id.action_cart_home:
                Intent intent = new Intent(MainActivity.this, Cart.class);
//                intent.putExtra("user_phone_no", user_phone);
                startActivity(intent);
                break;
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
                finish();
                Log.d("MainActivity","finished");
                break;
            case R.id.app_logout:
                showPopup();
                break;
            case R.id.app_Orders:
                intent = new Intent(MainActivity.this, Order_History.class);
                startActivity(intent);
                break;
            case R.id.app_share:

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String sharebody="https://play.google.com/store/apps/details?id=com.udaan.recstudentportalV2%hl=en";
                String Subject="Fast Delivery APP";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,Subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT,sharebody);
                startActivity(Intent.createChooser(shareIntent,"Share Using"));
                break;
            case R.id.app_about:
                Intent aboutus= new Intent(MainActivity.this, AboutUs.class);
                startActivity(aboutus);
                break;
            case R.id.app_contact:
                String p = "6202123103";
                Intent in = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", p, null));
                startActivity(in);
                break;


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
        Paper.book().delete(Prevalent.userPhone);
        Paper.book().delete(Prevalent.userPassword);
        Prevalent.currentOnlineUser = null;
        Intent intent = new Intent(MainActivity.this, login.class);
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

    private void setcart() {
        firebaseDatabase= FirebaseDatabase.getInstance();
//        getting the reference of the Cart
        DatabaseReference cartrefence = firebaseDatabase.getReference("Cart");
//        here we will pass the user phone number and from that we fetch of
//        particular user
        user_reference = cartrefence.child(user_phone);
        user_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get total available quest
                int size = (int) dataSnapshot.getChildrenCount();
                mCartItemCount=size;
                setupBadge();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount ==0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}
