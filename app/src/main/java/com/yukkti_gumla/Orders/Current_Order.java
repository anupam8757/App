package com.yukkti_gumla.Orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.yukkti_gumla.Model.User;
import com.yukkti_gumla.Prevalent.Prevalent;
import com.yukkti_gumla.R;
import com.yukkti_gumla.home.Cart_list;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Current_Order extends Fragment {
    DatabaseReference ordersRef;
    private static String user_phone = "", FetchedTime, total_price, total_items,address = "";
    List<Cart_list> cart_list;
    ArrayList<String> key = new ArrayList<>();
    private User user;
    private ListView orderList;
    private TextView fullAddress,totalPrice,DateTime,total_no_items,user_name;

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
        user_name = view.findViewById(R.id.user_name);
//        DateTime = view.findViewById(R.id.date_time_order);
//        total_no_items = view.findViewById(R.id.date_time_order);
        orderList = view.findViewById(R.id.list_view_orders);

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
                totalPrice.setText(" Rs. "+total_price);
//                DateTime.setText(FetchedTime);
//                total_no_items.setText("Total items: "+total_items);
                ArrayList<Order_list> order_lists = new ArrayList<>();
                try{
                    for(Cart_list list: cart_list){
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
                OrderListAdapter adapter = new OrderListAdapter(getContext(), R.layout.order_list, order_lists);
                orderList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        try {
            user_name.setText("Thank you "+ user.getName()+",\n"+"your order is successfully placed.");
            address = "";
            address += user.getName() + ",\n" + user_phone + ", " + user.getAddress();
            fullAddress.setText(address);
        }
        catch (Exception e){

        }


    }
}
