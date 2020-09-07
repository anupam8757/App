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
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
                if (!time_lies()) {
                    show_time_constraint();
                }
                else{
                    if (total_price() < 75) {
                        show_price_pop();
                    } else {
                        if (checkAddress()) {
                            confirmOrder();
                        }
                    }
                }

            }

        });

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
                            emptyView.setTextColor(Color.parseColor("#ff0000"));
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


        AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
        builder.setIcon(R.drawable.confirm)
                .setMessage(Html.fromHtml("<font color='#000000'><h2>Confirm Order?</h2> Total price = Rs. </font>"+total_price()))
                .setCancelable(false)
                .setNegativeButton("No", null);
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
        final boolean[] val = {false};
        try {
            User user = Prevalent.currentOnlineUser;

            if (user.getAddress().trim().isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
                builder.setIcon(R.drawable.confirm)
                        .setMessage(Html.fromHtml("<font color='#000000'><h2>Did you forget to enter Address?</h2> Please enter address by clicking Enter Address. </font>"))
                        .setCancelable(false)

                        .setNegativeButton("", null);
                builder.setPositiveButton("Enter Address", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Cart.this, profile.class);
                        startActivity(i);
                        val[0] = true;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                //Set positive button background
                pbutton.setBackgroundColor(Color.parseColor("#ffffff"));
                //Set positive button text color
                pbutton.setTextColor(Color.parseColor("#1704FF"));

            } else {
                val[0] = true;
            }
        }
        catch (Exception e)
        {

        }
        return val[0];
    }

    public int total_price(){
        total_price_of_all_items = 0;
        try{
            for(Cart_list cartList : cart_lists) {
                int total_price = Integer.parseInt(cartList.getTotal_price());
                message += " Item name: "+cartList.getName()+" Price: "+total_price+" Quantity: "+cartList.getAmount() + "\n";
                total_price_of_all_items += total_price;
                user_reference.child(cartList.getPid()).setValue(cartList);
            }
            message += " Name: "+Prevalent.currentOnlineUser.getName()+ " Address: "
                    +Prevalent.currentOnlineUser.getAddress()+"\n" +"Total price "+total_price_of_all_items
            + "\n ";
            message+=Prevalent.currentOnlineUser.getPhone().substring(0,5);

        Log.d("Cart","price: "+ total_price_of_all_items);
        }catch (Exception e){}
        return total_price_of_all_items;
    }
    private void show_price_pop() {
        // Initializing a new alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String titleText = "SORRY !";

        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

        // Apply the text color span
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Set the alert dialog title using spannable string builder
        builder.setTitle(ssBuilder);

        // Show a message on alert dialog
        builder.setMessage(Html.fromHtml("<font color='#000000'><h5>Please order above Rs. 75 "));

        // Set the positive button
        builder.setPositiveButton("Ok",null);


        // Create the alert dialog
        AlertDialog dialog = builder.create();

        // Finally, display the alert dialog
        dialog.show();
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //Set positive button background
        pbutton.setBackgroundColor(Color.parseColor("#ffffff"));
        //Set positive button text color
        pbutton.setTextColor(Color.parseColor("#1704FF"));

    }
    private void show_time_constraint(){
        // Initializing a new alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String titleText = "SORRY !";

        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

        // Apply the text color span
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Set the alert dialog title using spannable string builder
        builder.setTitle(ssBuilder);

        // Show a message on alert dialog
        builder.setMessage(Html.fromHtml("<font color='#000000'><h5>Please order in between</> <h5>8:00 am to 8:30 pm</h5> "));

        // Set the positive button
        builder.setPositiveButton("Ok",null);


        // Create the alert dialog
        AlertDialog dialog = builder.create();

        // Finally, display the alert dialog
        dialog.show();
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //Set positive button background
        pbutton.setBackgroundColor(Color.parseColor("#ffffff"));
        //Set positive button text color
        pbutton.setTextColor(Color.parseColor("#1704FF"));

    }
    private boolean time_lies(){
        boolean value = false;
        try {
            String string1 = "08:00:00";
            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);

            String string2 = "20:30:00";
            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

            String someRandomTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            Date d = new SimpleDateFormat("HH:mm:ss").parse(someRandomTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);
            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                value=true;
            }
            else value=false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }


}