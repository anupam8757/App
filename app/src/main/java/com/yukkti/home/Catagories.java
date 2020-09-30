package com.yukkti.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yukkti.Prevalent.Prevalent;
import com.yukkti.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class Catagories extends AppCompatActivity implements Cat_Adapter.OnItemClickListener {
    private Toolbar cat_toolbar;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference productrefence, cart;
    private DatabaseReference product_child;
    private RecyclerView cat_recyclerView;
    private List<Cat_list> cat_lists;
    private Cat_Adapter cat_Adapter;
    private  String cat_name;
    private TextView textCartItemCount;
    private int mCartItemCount = 0;
    public static String user_phone;
    private DatabaseReference user_reference;
    LinearLayout linearLayout;
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
        setContentView(R.layout.activity_catagories);
        CheckConnection();
        linearLayout=findViewById(R.id.Catogaries_layout);
        Paper.init(this);
        cat_toolbar=findViewById(R.id.cat_toolbar);
        setSupportActionBar(cat_toolbar);
        user_phone = Paper.book().read(Prevalent.userPhone);

        Log.d("catogaroes","in oncreate");

//        adding the title which we will get from the main activity
        cat_name = getIntent().getStringExtra("cat_name");
//
        Objects.requireNonNull(getSupportActionBar()).setTitle(cat_name);

//        display the back button on the activity to go back to home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        get the reference of the cat_recylerView

        cat_recyclerView=findViewById(R.id.cat_recyclerView);
        cat_recyclerView.setHasFixedSize(true);
        cat_lists=new  ArrayList<>();

//        the no of column in the layout is 2

        cat_recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        implement the database
        firebaseDatabase=FirebaseDatabase.getInstance();
//        getting the reference of the products
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
            case "PATANJALI ITEMS":
                product_child=productrefence.child("PatanJali_Items");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            case "CLEANING & HOUSEHOLD":
                product_child=productrefence.child("cleaning_and_household");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;
            case "SWEETS":
                product_child=productrefence.child("Sweets");
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
            case "AMUL ITEMS":
                product_child=productrefence.child("amul");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;

            default:
                product_child=productrefence.child("other");
                Log.d("fetched","before fetched");
                fetchdata(product_child);
                break;

        }
        setcart();
    }

    private void CheckConnection() {
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

    // this is method which will fetch the data from the server according to its child
    private void fetchdata(DatabaseReference product_child) {
        product_child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot :snapshot.getChildren()){
                    Cat_list cat_list = postSnapshot.getValue(Cat_list.class);
//                    adding the item to the list which we get from the data base
                    cat_lists.add(cat_list);
//                    Log.d("fetched",cat_list.getName());
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
        final MenuItem cartItem = menu.findItem(R.id.action_cart);
        View actionView = cartItem.getActionView();
        textCartItemCount =actionView.findViewById(R.id.cart_badge);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(cartItem);
            }
        });

//        this code is for the searching the item in the layout
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cat_Adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "cart" menu option
            case R.id.action_cart:
                Intent intent = new Intent(Catagories.this, Cart.class);
//                    intent.putExtra("user_phone", user_phone);
                startActivity(intent);
                break;

            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
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


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemClick(final int position) {
        String pressed_item = null;
        try {

            pressed_item= cat_lists.get(position).getName().trim() + cat_lists.get(position).getPrice().trim();
//        checking for the data item is already added or not
//        Log.d("pressed_item",pressed_item);
        }
        catch (Exception e){

        }

        final String finalPressed_item = pressed_item;
        user_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(finalPressed_item)){
//                    add.setText("added");
//                    add.setTextColor(Color.GREEN);
                    Toast.makeText(Catagories.this, "Item is already added", Toast.LENGTH_SHORT).show();
                }
//                if item which is pressed is not present then add this to the cart
                else {
                    mCartItemCount++;
                    setupBadge();
                    cart = FirebaseDatabase.getInstance().getReference().child("Cart");
                    final String pid = cat_lists.get(position).getName().trim() +cat_lists.get(position).getPrice().trim();
                    int amount = 1;
                    Log.d("............", cat_name);
                    Log.d("............", pid);
                    Log.d("............", "phone " + user_phone);

                    if (user_phone == null) {
                        Toast.makeText(Catagories.this, "You must Login First...", Toast.LENGTH_SHORT).show();

                    } else {

                        final HashMap<String, Object> cartMap = new HashMap<>();
                        cartMap.put("pid", pid);
                        cartMap.put("name",cat_lists.get(position).getName().trim());
                        cartMap.put("price",cat_lists.get(position).getPrice().trim());
                        cartMap.put("categories", cat_name);
                        cartMap.put("amount", amount);
                        cartMap.put("total_price",cat_lists.get(position).getPrice().trim());

                        cart.child(user_phone).child(pid).updateChildren(cartMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //  Toast.makeText(Catagories.this, "Product added....", Toast.LENGTH_SHORT).show();
                                Snackbar.make(linearLayout,"Item added to Cart",Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
