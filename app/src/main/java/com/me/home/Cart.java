package com.me.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.Orders.OrderActivity;
import com.me.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Cart extends AppCompatActivity {
    private Toolbar cart_toolbar;
    private RecyclerView cartRecyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference cartrefence;
    DatabaseReference user_reference;
    List<Cart_list> cart_lists;
    CartAdapter cartAdapter;
    String user_phone;
    FloatingActionButton order_button;
    private int total_price_of_all_items = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        order_button=findViewById(R.id.fab);
        order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartRecyclerView.setAdapter(null);
                updateDetailsToCart();
                Intent intent = new Intent(Cart.this, OrderActivity.class);
                intent.putExtra("total_price",Integer.toString(total_price_of_all_items));
                startActivity(intent);

                Toast.makeText(Cart.this, "order is placed", Toast.LENGTH_SHORT).show();
            }
        });

        user_phone= getIntent().getStringExtra("user_phone");
        cart_toolbar=findViewById(R.id.cart_toolbar);
        setSupportActionBar(cart_toolbar);

        cartRecyclerView=findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setHasFixedSize(false);
        cart_lists=new  ArrayList<>();

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(cartRecyclerView);

//        implement the database
        firebaseDatabase= FirebaseDatabase.getInstance();
//        getting the reference of the Cart
        cartrefence = firebaseDatabase.getReference("Cart");

//        here we will pass the user phone number and from that we fetch of
//        particular user
        user_reference= cartrefence.child(user_phone);

//        fetch the item from the firebase cart node according to there phone number

        user_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot :snapshot.getChildren()){
                    Cart_list cart_list = postSnapshot.getValue(Cart_list.class);
//                    adding the item to the list which we get from the data base
                    cart_lists.add(cart_list);
                    Log.d("fetched",cart_list.getName());
                }
                cartAdapter =new CartAdapter(Cart.this,cart_lists);
                cartRecyclerView.setAdapter(cartAdapter);


//                this is code for deleting the item from the cart
                cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                    @Override
                    public void onDeleteClick(int position) {
                        Intent intent = new Intent(Cart.this,Cart.class);
                        String id=cart_lists.get(position).getPid().trim();
                        Log.d("deleted item ",id);

                        DatabaseReference driverRef = user_reference.child(id);
                        driverRef.removeValue();

                        finish();
                        overridePendingTransition(0,0);
                        intent.putExtra("user_phone",user_phone);
                        startActivity(intent);
                        overridePendingTransition(0,0);

                        cart_lists.remove(position);
                        cartAdapter.notifyItemRemoved(position);
                        return;
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Cart.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateDetailsToCart(){
        for(Cart_list cartList : cart_lists) {
            int total_price = Integer.parseInt(cartList.getTotal_price());
            total_price_of_all_items += total_price;
            user_reference.child(cartList.getPid()).setValue(cartList);
        }
    }
}