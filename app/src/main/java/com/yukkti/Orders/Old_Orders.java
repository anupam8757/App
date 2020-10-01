package com.yukkti.Orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yukkti.Prevalent.Prevalent;
import com.yukkti.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Old_Orders extends Fragment implements OrderAdapter.OrderClick {
    private RecyclerView orderRecyclerView;
    List<Order_Details> order_details;
    DatabaseReference ordersRef;
    OrderAdapter orderAdapter;
    static String user_phone = "";
    private String currentDate;
    TextView empty_old_order;

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
        empty_old_order=view.findViewById(R.id.empty_old_order);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        order_details = new ArrayList<>();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user_phone);

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Order_Details data = dataSnapshot.getValue(Order_Details.class);
                    order_details.add(data);
//                    }
                }
//                Collections.reverse(order_details);
                Collections.sort(order_details, new Compare());
                try{
                    Order_Details curr = order_details.get(0);
                    Paper.book().write("Key",curr.getDate_time());
                    int index=order_details.size()-1;
                    if (index!=-1){
                        order_details.remove(0);
                    }
                }
                catch (Exception e){}

                orderAdapter = new OrderAdapter(order_details, Old_Orders.this);

                orderRecyclerView.setAdapter(orderAdapter);

                if (orderAdapter.getItemCount() == 0){
                    orderRecyclerView.setAdapter(null);
                    empty_old_order.setVisibility(View.VISIBLE);


                } else {
                    orderRecyclerView.setAdapter(orderAdapter);
                    empty_old_order.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onOrderClick(int position) {
        order_details.get(position);
        Intent intent = new Intent(getContext(), Old_Order_List.class);
        intent.putExtra("date_time",order_details.get(position).getDate_time());
        intent.putExtra("total_price",order_details.get(position).getTotal_price());
        startActivity(intent);
    }

    public class Compare implements Comparator<Order_Details>{

        @Override
        public int compare(Order_Details o1, Order_Details o2) {
            String dateTime1 = o1.getDate_time();
            String dateTime2 = o2.getDate_time();
            String [] daTi1 = dateTime1.split(" ");
            String date1 = dateTime1.substring(6,10) + dateTime1.substring(3,5) + dateTime1.substring(0,2);
            int date11 = Integer.parseInt(date1);
            String []time1 = daTi1[1].split(":");



            String [] daTi2 = dateTime2.split(" ");
            String date2 =  dateTime2.substring(6,10) + dateTime2.substring(3,5) + dateTime2.substring(0,2);
            int date22 = Integer.parseInt(date2);
            String []time2 = daTi2[1].split(":");


            if(date11 != date22){
                if(date11 > date22){
                    return -1;
                }
                else{
                    return 1;
                }
            }
            else{
                int a = daTi1[1].compareTo(daTi2[1]);
                if(a>0){
                    return -1;
                }else{
                    return 1;
                }
            }
        }
    }
}