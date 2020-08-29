package com.yukkti.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yukkti.JavaMailApi;
import com.yukkti.Model.User;
import com.yukkti.Orders.Order_History;
import com.yukkti.Prevalent.Prevalent;
import com.yukkti.R;
import com.yukkti.Register.profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Cart extends AppCompatActivity {
    private Toolbar cart_toolbar;
    private RecyclerView cartRecyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference cartrefence , orderRef;
    DatabaseReference user_reference;
    List<Cart_list> cart_lists;
    CartAdapter cartAdapter;
    public String user_phone;
    String currentDate, currentTime, message="";
    Button order_button;
    private int total_price_of_all_items = 0,total_items = 0;
    TextView emptyText;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        checkConnection();
        order_button=findViewById(R.id.fab);
        Paper.init(this);

        order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAddress();
                if (checkAddress()) {
                    confirmOrder();
                }
            }
        });

        emptyText = findViewById(R.id.total_amount_cart);
        emptyText.setVisibility(View.INVISIBLE);

        user_phone = Paper.book().read(Prevalent.userPhone);
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
        orderRef = FirebaseDatabase.getInstance().getReference();

//        here we will pass the user phone number and from that we fetch of
//        particular user
        user_reference= cartrefence.child(user_phone);


//        fetch the item from the firebase cart node according to there phone number
        user_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cart_lists.clear(); //this will clear the list which will override
                for (DataSnapshot postSnapshot :snapshot.getChildren()){
                    Cart_list cart_list = postSnapshot.getValue(Cart_list.class);
//                    adding the item to the list which we get from the data base
                    cart_lists.add(cart_list);
                    assert cart_list != null;
                    Log.d("fetched",cart_list.getName());
                }
                cartAdapter =new CartAdapter(Cart.this,cart_lists);
                cartRecyclerView.setAdapter(cartAdapter);

                emptyView=findViewById(R.id.empty_view);
                if (cartAdapter.getItemCount() == 0){
                    cartRecyclerView.setAdapter(null);
                    emptyView.setVisibility(View.VISIBLE);
                    order_button.setVisibility(View.GONE);
                } else {
                    cartRecyclerView.setAdapter(cartAdapter);
                    order_button.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

//                this is code for deleting the item from the cart
                cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                    @Override
                    public void onDeleteClick(int position) {
                        Intent intent = new Intent(Cart.this,Cart.class);
                        String id=cart_lists.get(position).getPid().trim();
                        DatabaseReference driverRef = user_reference.child(id);
                        driverRef.removeValue();
                        cart_lists.remove(position);
                        cartAdapter.notifyItemRemoved(position);
                        cartAdapter.notifyDataSetChanged();

                        if (cartAdapter.getItemCount() == 0){
                            cartRecyclerView.setAdapter(null);
                            emptyView.setVisibility(View.VISIBLE);
                            emptyView.setText(Html.fromHtml("<h5>You have deleted all items </h5><br>" +
                                    "<p> please add item </p>"));
                            order_button.setVisibility(View.GONE);
                            emptyView.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            cartRecyclerView.setAdapter(cartAdapter);
                            order_button.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onPlusMinusClick(int position, int amt) {
                        cart_lists.get(position).setAmount(amt);
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Cart.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void checkConnection() {
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

    public void updateDetailsToCart(){

        sendEmail(message);// Method to call send Email
        total_items = cart_lists.size();
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        HashMap<String, Object> orderDetails = new HashMap<>();
        orderDetails.put("total_price",Integer.toString(total_price_of_all_items));
        orderDetails.put("Cart",cart_lists);
        orderDetails.put("date_time",currentDate+" "+currentTime);
        orderDetails.put("total_items",Integer.toString(total_items));
        Toast.makeText(Cart.this,"We Will Contact You Shortly.",Toast.LENGTH_SHORT).show();
        orderRef.child("Orders").child(user_phone).child(currentDate+" "+currentTime).updateChildren(orderDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user_reference.removeValue();
                            cart_lists.clear();
                            order_button.setEnabled(false);
                            cartRecyclerView.setAdapter(null);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Cart.this, Order_History.class);
                                    intent.putExtra("total_price",Integer.toString(total_price_of_all_items));
                                    startActivity(intent);
                                }
                            },2000);
                        }
                    }
                });
    }

    private void sendEmail(String message) {
        // String message=  "New Order is Placed.";
        String subject= "New Order is Confirmed.";
        String Email="shivanand103kumar@gmail.com";
        JavaMailApi javaMailApi= new JavaMailApi(Cart.this, Email, subject, message);
        javaMailApi.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        for(Cart_list cart_list: cart_lists){
            cart_list.setAmount(cart_list.getAmount());
            String p = cart_list.getPrice();
            cart_list.setPrice(p);
            cart_list.setTotal_price(cart_list.getTotal_price());
            String id = cart_list.getName()+cart_list.getPrice();
            user_reference.child(id).setValue(cart_list);
        }

    }

    public void confirmOrder(){

        total_price_of_all_items = 0;

        if(total_price() <= 50){
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText("Minimum Order is 50.");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
        builder.setIcon(R.drawable.confirm)
                .setMessage(Html.fromHtml("<font color='#000000'><h2>Confirm Order?</h2> Total price = Rs. </font>"+total_price()))
                .setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        emptyText.setVisibility(View.GONE);
                    }
                });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                updateDetailsToCart();
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

    private boolean checkAddress() {
        User user = Prevalent.currentOnlineUser;
        final boolean[] val = {false};
        if(user.getAddress().trim().isEmpty() ){
            AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
            builder.setIcon(R.drawable.confirm)
                    .setMessage(Html.fromHtml("<font color='#000000'><h2>Did you forget to enter Address?</h2> Please enter address by clicking Enter Address. </font>"))
                    .setCancelable(false)

                    .setNegativeButton("", null);
            builder.setPositiveButton("Enter Address", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Cart.this, profile.class);
                    startActivity(i);
                    val[0] =true;
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            //Set positive button background
            pbutton.setBackgroundColor(Color.parseColor("#ffffff"));
            //Set positive button text color
            pbutton.setTextColor(Color.parseColor("#1704FF"));

        }
        else {
            val[0] = true;
        }
        return val[0];
    }

    public int total_price(){
        try{
            total_price_of_all_items = 0;
            for(Cart_list cartList : cart_lists) {
                int total_price = Integer.parseInt(cartList.getTotal_price());
                message += " Item name: "+cartList.getName()+" Price: "+total_price+" Quantity: "+cartList.getAmount();
                total_price_of_all_items += total_price;
                user_reference.child(cartList.getPid()).setValue(cartList);
            }
            message += " Name: "+Prevalent.currentOnlineUser.getName()+ " Address: "
                    +Prevalent.currentOnlineUser.getAddress();
        }catch (Exception e){}
        return total_price_of_all_items;
    }
}