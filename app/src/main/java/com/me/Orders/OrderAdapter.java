package com.me.Orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter {
    List<Order_Details> order_details;
    String date_time;

    public OrderAdapter(List<Order_Details> order_details) {
        this.order_details = order_details;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderClass viewHolderClass = (ViewHolderClass) holder;
        Order_Details current_order = order_details.get(position);

        ((ViewHolderClass) holder).total_price.setText("Total price: "+current_order.getTotal_price());

        date_time = current_order.getDate_time();
        String[] dateTime = date_time.split(" ",2);

        ((ViewHolderClass) holder).date.setText(dateTime[0]);

        String time = time_12_format(dateTime[1]);
        ((ViewHolderClass) holder).time.setText(time);

        ((ViewHolderClass) holder).total_items.setText("Total No of items: "+current_order.getTotal_items());
    }

    @Override
    public int getItemCount() {
        return order_details.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView date, time, total_price,total_items;


        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.order_date);
            time = itemView.findViewById(R.id.order_time);
            total_price = itemView.findViewById(R.id.order_total_price);
            total_items = itemView.findViewById(R.id.order_total_items);
        }
    }

    public String time_12_format(String str){
        String time = "";
        int h1 = (int)str.charAt(0) - '0';
        int h2 = (int)str.charAt(1)- '0';

        int hh = h1 * 10 + h2;

        String Meridien;
        if (hh < 12) {
            Meridien = "AM";
        }
        else{
            Meridien = "PM";
        }

        hh %= 12;

        if (hh == 0) {
            System.out.print("12");
            time += "12";

            for (int i = 2; i < 8; ++i) {
                System.out.print(str.charAt(i));
                time += str.charAt(i);
            }
        }
        else {
            time += hh;
            for (int i = 2; i < 8; ++i) {
                time += str.charAt(i);
            }
        }
        return time.substring(0,4) +" "+ Meridien;
    }
}
