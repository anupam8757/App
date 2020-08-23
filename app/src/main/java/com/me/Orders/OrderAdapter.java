package com.me.Orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter {
    private List<Order_Details> order_details;
    private String date_time;
    private OrderClick morderClick;

    public OrderAdapter(List<Order_Details> order_details,OrderClick morderClick ) {
        this.order_details = order_details;
        this.morderClick = morderClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view, morderClick);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderClass viewHolderClass = (ViewHolderClass) holder;
        Order_Details current_order = order_details.get(position);
        date_time = current_order.getDate_time();
        String[] dateTime = date_time.split(" ",2);

        String time = time_12_format(dateTime[1]);

            ((ViewHolderClass) holder).date.setText(dateTime[0]);
            ((ViewHolderClass) holder).total_price.setText("Total price: "+current_order.getTotal_price());
            ((ViewHolderClass) holder).time.setText(time);
            ((ViewHolderClass) holder).total_items.setText("Total No of items: "+current_order.getTotal_items());
    }

    @Override
    public int getItemCount() {
        return order_details.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date, time, total_price,total_items;
        OrderClick orderClick;
        public ViewHolderClass(@NonNull View itemView,OrderClick orderClick) {
            super(itemView);
            date = itemView.findViewById(R.id.order_date);
            time = itemView.findViewById(R.id.order_time);
            total_price = itemView.findViewById(R.id.order_total_price);
            total_items = itemView.findViewById(R.id.order_total_items);
            this.orderClick = orderClick;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            morderClick.onOrderClick(getAdapterPosition());
        }
    }

    public interface OrderClick{
        void onOrderClick(int position);
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
            time += "12";

            for (int i = 2; i < 8; ++i) {
                time += str.charAt(i);
            }
        }
        else {
            time += hh;
            for (int i = 2; i < 8; ++i) {
                time += str.charAt(i);
            }
        }

        if(time.length() == 7){
            return time.substring(0,4) +" "+ Meridien;
        }
        else {
            return time.substring(0,5) +" "+ Meridien;
        }
    }
}
