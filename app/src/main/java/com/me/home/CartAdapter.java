package com.me.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.me.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartAdapter_Holder> {

    List<Cart_list> cart_list;

    private LayoutInflater mInflater;
    Context context;
    public CartAdapter(Context context, List<Cart_list> cart_lists) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.cart_list = cart_lists;
    }

    @NonNull
    @Override
    public CartAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cart_item, parent, false);
        return new CartAdapter.CartAdapter_Holder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter_Holder holder, int position) {
        Cart_list current_position=cart_list.get(position);

        Log.d("name",current_position.getName());
//        setting the name
        holder.name.setText(current_position.getName());
//        setting the final price for the each item in layout
        final int price=Integer.parseInt(current_position.getPrice());
        String newprice="Rs"+current_position.getPrice();
        holder.price.setText(newprice);

//        her we will set the final price according to the amount
        final int[] getamount = new int[1];
        holder.amount.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
               getamount[0] = Integer.parseInt(holder.amount.getNumber());
               setprice(getamount[0]);
            }

//            this method is called every time when the elegant button is onclick
            private void setprice(int i) {
                int total_price=price*i;

                holder.total_price.setText("Rs"+total_price);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cart_list.size();
    }

    public static class CartAdapter_Holder extends RecyclerView.ViewHolder {
        public ElegantNumberButton amount;
        public TextView name;
        private TextView price;
        private TextView total_price;


        public CartAdapter_Holder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.cart_name);
            this.price = itemView.findViewById(R.id.cart_price);
            this.amount=itemView.findViewById(R.id.amount);
            this.total_price =itemView.findViewById(R.id.total_amount);

        }

    }
}
