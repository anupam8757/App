package com.me.Orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.me.Prevalent.Prevalent;
import com.me.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Old_Orders extends Fragment {
    private RecyclerView orderRecyclerView;
    List<Order_Details> order_details;
    DatabaseReference ordersRef;
    OrderAdapter orderAdapter;
    static String user_phone = "";
    private String currentDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.old_orders,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user_phone = Paper.book().read(Prevalent.userPhone);
        orderRecyclerView = view.findViewById(R.id.order_recyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        order_details = new ArrayList<>();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user_phone);

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    String date = dataSnapshot.child("date_time").getValue().toString();
//                    String[] dateTime = date.split(" ",2);
//                    if(!currentDate.equals(dateTime[0])){
                        Order_Details data = dataSnapshot.getValue(Order_Details.class);
                        order_details.add(data);
//                    }
                }
                order_details.remove(order_details.size()-1);
                Collections.reverse(order_details);
                orderAdapter = new OrderAdapter(order_details);
                orderRecyclerView.setAdapter(orderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
