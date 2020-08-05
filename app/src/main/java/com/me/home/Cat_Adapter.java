package com.me.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.me.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Cat_Adapter<pid> extends RecyclerView.Adapter <Cat_Adapter.MainAdapter_Holder> implements Filterable {

        private static final String LOG_TAG = MainAdapter.class.getSimpleName();
//        DatabaseReference storageReference= FirebaseDatabase.getInstance().getReference("main");


        List<Cat_list> cat_list;
        List<Cat_list> cat_list_full;
        private static String pid;

        private LayoutInflater mInflater;
        Context context;
        private static OnItemClickListener mListener;

        public Cat_Adapter(Context context, List<Cat_list> cat_list) {
            this.context=context;
            this.mInflater = LayoutInflater.from(context);
            this.cat_list = cat_list;
//        to create a new list
            this.cat_list_full=new ArrayList<>(cat_list);
        }

        @NonNull
        @Override
        public MainAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.cat_list_item, parent, false);
            return new Cat_Adapter.MainAdapter_Holder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull MainAdapter_Holder holder, int position) {
           Cat_list currentPosition=cat_list.get(position);

            holder.name.setText(currentPosition.getName());
            holder.price.setText(currentPosition.getPrice());
            holder.description.setText(currentPosition.getDescription());
            Picasso.with(context)
                    .load(currentPosition.getImage())
                    .error(R.drawable.heart)
                    .placeholder(R.drawable.loading)
                    .into(holder.image);
        }

        @Override
        public int getItemCount() {
            return cat_list.size();
        }


        public static class MainAdapter_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView image;
            public TextView name;
            private TextView price;
            private TextView description;

            public MainAdapter_Holder(@NonNull View itemView) {
                super(itemView);
                this.image = itemView.findViewById(R.id.image);
                this.name = itemView.findViewById(R.id.name);
                this.description=itemView.findViewById(R.id.description);
                this.price=itemView.findViewById(R.id.price);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if(mListener !=null){
                    int position=getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        try {
                            mListener.onItemClick(position, name, price);
                        }catch (Exception e){}
                    }
                }
            }
        }


//    the method which we have to override for implementing the filterable
//    interface

        @Override
        public Filter getFilter() {
            return cat_list_filter;
        }
        private Filter cat_list_filter =new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Cat_list> filteredList=new ArrayList<>();
                if(constraint ==null || constraint.length()==0){
                    filteredList.addAll(cat_list_full);
                }
                else {
                    String filterPattern=constraint.toString().toLowerCase().trim();

                    for(Cat_list item :cat_list_full){
                        if(item.getName().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results=new FilterResults();
                results.values=filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cat_list.clear();
                cat_list.addAll((List) results.values);
                notifyDataSetChanged();

            }
        };
        public interface OnItemClickListener{

            void onItemClick(int position, TextView main_name, TextView price);
        }
        public void setOnItemClickListener(OnItemClickListener listener){
            mListener = listener;
        }
    }
