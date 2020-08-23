package com.me.Orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.Prevalent.Prevalent;
import com.me.R;
import com.me.home.Cart_list;

import java.util.ArrayList;

import io.paperdb.Paper;

public class Old_Order_List extends AppCompatActivity {
    DatabaseReference ordersRef;
    private String user_phone, totalPrice;
    private TextView mTextView1,mTextView2;
    private String Date_time;
    private ListView orderList;
    ArrayList<Cart_list> cart_lists;
    ArrayList<String> key = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old__order__list);
        Paper.init(this);
        user_phone = Paper.book().read(Prevalent.userPhone);
        Date_time = getIntent().getStringExtra("date_time");
        totalPrice = getIntent().getStringExtra("total_price");

        mTextView1 = (TextView) findViewById(R.id.texttt);
        mTextView1.setText(Date_time);
        mTextView2 = (TextView) findViewById(R.id.total_price);
        mTextView2.setText("Rs. "+totalPrice);

        orderList = findViewById(R.id.order_details_listView);
        cart_lists = new ArrayList<>();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user_phone).child(Date_time).child("Cart");

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    key.add(dataSnapshot.getKey());
                    Log.d("Cart product key", dataSnapshot.getKey());
                }
                try{
                    for(String curKey: key){
                        Cart_list curValue = new Cart_list();
                        curValue = snapshot.child(curKey).getValue(Cart_list.class);
                        cart_lists.add(curValue);
                    }
                }catch (Exception e){}
                ArrayList<Order_list> order_lists = new ArrayList<>();
                try{
                    for(Cart_list list: cart_lists){
                        String name = list.getName();
                        Log.d("name",""+name);
                        String price = list.getPrice();
                        Log.d("price",""+price);
                        String quantity = Integer.toString(list.getAmount());
                        Log.d("quantity",""+quantity);
                        String total_price=list.getTotal_price();
                        Order_list orderList = new Order_list(name,price,quantity,total_price);
                        order_lists.add(orderList);
                    }
                }catch (Exception e){}
                OrderListAdapter adapter = new OrderListAdapter(Old_Order_List.this, R.layout.order_list, order_lists);
                orderList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}