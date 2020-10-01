package com.yukkti.Orders;

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
import com.yukkti.Model.User;
import com.yukkti.Prevalent.Prevalent;
import com.yukkti.R;
import com.yukkti.home.Cart_list;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Current_Order extends Fragment {
    DatabaseReference ordersRef;
    private static String user_phone = "", FetchedTime, total_price, total_items,address = "";
    List<Cart_list> cart_list;
    private User user;
    private ListView orderList;
    private TextView fullAddress,totalPrice,DateTime,total_no_items,user_name;
    TextView empty_current_order;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.current_order,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Paper.init(getContext());

        user_phone = Paper.book().read(Prevalent.userPhone);
        user = Prevalent.currentOnlineUser;

        fullAddress = view.findViewById(R.id.address_full);
        totalPrice = view.findViewById(R.id.total_price_of_order);
        user_name = view.findViewById(R.id.user_name);
        user_name.setVisibility(View.GONE);
//        DateTime = view.findViewById(R.id.date_time_order);
//        total_no_items = view.findViewById(R.id.date_time_order);
        orderList = view.findViewById(R.id.list_view_orders);

//        empty list view
        empty_current_order=view.findViewById(R.id.empty_current_order);

        try{

            String Datetime = Paper.book().read("Key");
            Log.d("Current_Order"," "+Datetime);

            cart_list = new ArrayList<>();

            ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user_phone).child(Datetime);
            ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    total_price = (String) dataSnapshot.child("total_price").getValue();
                    total_items = (String) dataSnapshot.child("total_items").getValue();
                    if(total_items != null) {
                        user_name.setVisibility(View.VISIBLE);
                        for (int i = 0; i < Integer.parseInt(total_items); i++) {
                            Cart_list cart_list1 = dataSnapshot.child("Cart").child(String.valueOf(i)).getValue(Cart_list.class);
                            cart_list.add(cart_list1);
                        }
                        totalPrice.setText(" Rs. "+total_price);
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
                    else {
                        user_name.setVisibility(View.GONE);
                        empty_current_order.setVisibility(View.VISIBLE);
                        totalPrice.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }catch (Exception e){ }

        try
        {
            String []userAddress = new String[100];
            userAddress[0] = user.getAddress();
            userAddress[1] = " ";
            if(user.getAddress().contains("#")){
                userAddress = user.getAddress().split("#",2);
                userAddress[0] = userAddress[0]+", ";
                userAddress[1] = userAddress[1] +".";
            }
            else {
                userAddress[0] = user.getAddress();
                userAddress[1] = " ";
            }
            user_name.setText("Thank you "+ user.getName()+",\n"+"Your" +
                    " Order is Successfully Placed." + " \n We Will Contact You Shortly" );
            address = "";
            address += user.getName() + ",\n" + user_phone + ", " + userAddress[0]+userAddress[1];
            fullAddress.setText(address);
        }
        catch (Exception e){

        }

    }

}