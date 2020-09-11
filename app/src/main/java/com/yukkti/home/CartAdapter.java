package com.yukkti.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yukkti.Prevalent.Prevalent;
import com.yukkti.R;

import java.util.List;

import io.paperdb.Paper;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartAdapter_Holder> {

    List<Cart_list> cart_list;
    private OnItemClickListener mListener;
    public String user_phone = MainActivity.user_phone;
    private DatabaseReference cartRefence;
    private String pid;
    private LayoutInflater mInflater;
    private DatabaseReference dbref;
    Intent intent;

    Context context;
    public CartAdapter(Context context, List<Cart_list> cart_lists) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.cart_list = cart_lists;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public CartAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Paper.init(context);
        intent = new Intent(context,Cart.class);
        user_phone = Paper.book().read(Prevalent.userPhone);
        dbref = FirebaseDatabase.getInstance().getReference().child("Cart").child(user_phone);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cart_item, parent, false);
        cartRefence = FirebaseDatabase.getInstance().getReference().child("Cart").child(user_phone);
        return new CartAdapter.CartAdapter_Holder(listItem,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter_Holder holder, final int position) {
        final Cart_list current_position = cart_list.get(position);
        pid = current_position.getPid();

//        setting the name
        holder.name.setText(current_position.getName());

//        setting the final price for the each item in layout
        final int price=Integer.parseInt(current_position.getPrice());
        String newprice="Rs. "+current_position.getPrice();
        holder.price.setText(newprice);
        int amt = current_position.getAmount();
        holder.total_price.setText("Rs. "+Integer.toString(amt*price));
        holder.amount.setNumber(Integer.toString(amt));
//        her we will set the final price according to the amount
        final int[] getamount = new int[1];

        holder.amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if (newValue<=10) {
                    current_position.setAmount(newValue);
                    holder.amount.setNumber(String.valueOf(newValue));
                    cart_list.get(position).setAmount(newValue );
                    setprice(newValue);
                }
                else{
                    open();
                }
            }

            private void setprice(int i) {
                int total_price = price*i;
                holder.total_price.setText("Rs. "+total_price);
                current_position.setTotal_price(Integer.toString(total_price));
                cart_list.get(position).setTotal_price(Integer.toString(total_price));
            }
        });

    }

    public void open() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.confirm)
                .setMessage(Html.fromHtml("<font color='#000000'><h2>Message</h2>Item cannot be exceed 10 !!</font>"))
                .setCancelable(false)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        //Set negative button background
        nbutton.setBackgroundColor(Color.parseColor("#ffffff"));
        //Set negative button text color
        nbutton.setTextColor(Color.parseColor("#1704FF"));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        //Set positive button background
        pbutton.setBackgroundColor(Color.parseColor("#ffffff"));
        //Set positive button text color
        pbutton.setTextColor(Color.parseColor("#1704FF"));
    }

    @Override
    public int getItemCount() {
        return cart_list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public class CartAdapter_Holder extends RecyclerView.ViewHolder {
        public ElegantNumberButton amount;
        public TextView name;
        private TextView price;
        private TextView total_price;
        private ImageView cart_delete_btn;

        public CartAdapter_Holder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            this.name = itemView.findViewById(R.id.cart_name);
            this.price = itemView.findViewById(R.id.cart_price);
            this.amount=itemView.findViewById(R.id.amount);
            this.total_price =itemView.findViewById(R.id.total_amount);
            this.cart_delete_btn=itemView.findViewById(R.id.delete_cart_item);

            cart_delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        try {
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onDeleteClick(position);
                            }
                        }
                        catch (Exception e)
                        {

                        }
                    }
                }
            });
            amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onPlusMinusClick(position,Integer.parseInt(amount.getNumber()));
                        }
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onPlusMinusClick(int position, int amt);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
