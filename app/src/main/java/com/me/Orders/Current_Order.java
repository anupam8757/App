package com.me.Orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.me.Model.User;
import com.me.Prevalent.Prevalent;
import com.me.R;
import com.me.home.Cart;
import com.me.home.Cart_list;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Current_Order extends Fragment {
    DatabaseReference ordersRef;
    private static String user_phone = "", FetchedTime, total_price, total_items,address = "";
    List<Cart_list> cart_list;
    ArrayList<String> key = new ArrayList<>();
    private User user;
    private TextView fullAddress,totalPrice,DateTime,total_no_items;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.current_order,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user_phone = Paper.book().read(Prevalent.userPhone);
        user = Prevalent.currentOnlineUser;

        fullAddress = view.findViewById(R.id.address_full);
        totalPrice = view.findViewById(R.id.total_price_of_order);
        DateTime = view.findViewById(R.id.date_time_order);
        total_no_items = view.findViewById(R.id.date_time_order);

        cart_list = new ArrayList<>();

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user_phone);
        Query last_child = ordersRef.orderByKey().limitToLast(1);

        last_child.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("In..Cart","Before Loop");
                for (DataSnapshot order: dataSnapshot.getChildren()){

                    for(DataSnapshot cart_list1: order.child("Cart").getChildren()){
                        key.add(cart_list1.getKey());
                    }

                    try{
                        for(String curKey: key){
                            Cart_list curValue = new Cart_list();
                            curValue = order.child("Cart").child(curKey).getValue(Cart_list.class);
                            Log.d("Current Value",order.child("Cart").child(curKey).getValue().toString());
                            cart_list.add(curValue);
                        }
                    }catch (Exception e){}

                    Log.d("Array length",""+key.size());
                    Log.d("Array length",""+cart_list.size());
                    try{
                        Log.d("Array 1st element",""+cart_list.get(key.size()-2).getName());}catch (Exception e){}
                    total_items = order.child("total_items").getValue().toString();
                    total_price = order.child("total_price").getValue().toString();
                    FetchedTime = order.child("date_time").getValue().toString();
                }
                totalPrice.setText("Total price: "+total_price);
                DateTime.setText(FetchedTime);
                total_no_items.setText("Total items: "+total_items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        address = "";
        address += user.getName()+",\n"+user_phone+", "+user.getAddress();
        fullAddress.setText(address);

    }
}
