package com.me.Orders;

import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;
import com.me.Model.User;
import com.me.Prevalent.Prevalent;
import com.me.R;
import com.me.home.Cart_list;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Current_Order extends Fragment {
    DatabaseReference ordersRef;
    private static String user_phone = "", currentDate, total_price, total_items,address = "";
    ArrayList<Order_Details> order_details;
    private User user;
    private TextView fullAddress,totalPrice;

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

        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        fullAddress = view.findViewById(R.id.address_full);
        totalPrice = view.findViewById(R.id.total_price_of_order);

        order_details = new ArrayList<>();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user_phone);

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String date = dataSnapshot.child("date_time").getValue().toString();
                    String[] dateTime = date.split(" ",2);
                    if(currentDate.equals(dateTime[0])){
                        Order_Details data = dataSnapshot.getValue(Order_Details.class);
                        order_details.add(data);
                    }
                }
                Collections.reverse(order_details);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        address = "";
        address += user.getName()+",\n"+user_phone+", "+user.getAddress();
        fullAddress.setText(address);
        try{
            Order_Details lastOrder = order_details.get(0);
            Cart_list cart_list = lastOrder.getCart_list();
            total_items = lastOrder.getTotal_items();
            total_price = lastOrder.getTotal_price();
            //totalPrice.setText("Total price: "+total_price);
        }catch (Exception e){}

    }
}
