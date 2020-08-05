package com.me.home;
import android.content.Context;
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

public class MainAdapter extends RecyclerView.Adapter <MainAdapter.MainAdapter_Holder> implements Filterable {
    private static final String LOG_TAG = MainAdapter.class.getSimpleName();

    List<Main_list_item> main_list_items;

    List<Main_list_item> main_list_items_full;

    Context context;
    private static OnItemClickListener mListener;

    public MainAdapter(Context context, List<Main_list_item> main_list_items) {
        this.context=context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        this.main_list_items = main_list_items;
//        to create a new list
        this.main_list_items_full=new ArrayList<>(main_list_items);

    }

    @NonNull
    @Override
    public MainAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.main_list_item,parent,false);
        return new MainAdapter.MainAdapter_Holder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter_Holder holder, int position) {
        Main_list_item currentPosition=main_list_items.get(position);
        holder.main_name.setText(currentPosition.getMain_item_name());
        holder.main_image.setImageResource(currentPosition.getMain_item_image());
    }

    @Override
    public int getItemCount() {
        return main_list_items.size();
    }



    public static class MainAdapter_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView main_image;
        public TextView main_name;

        public MainAdapter_Holder(@NonNull View itemView) {
            super(itemView);
            this.main_image = itemView.findViewById(R.id.main_item_image);
            this.main_name = itemView.findViewById(R.id.main_item_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener !=null){
                int position=getAdapterPosition();
                if(position !=RecyclerView.NO_POSITION){
                    mListener.onItemClick(position,main_name);
                }
            }
        }
    }


//    the method which we have to override for implementing the filterable
//    interface

    @Override
    public Filter getFilter() {
        return main_list_filter;
    }
    private Filter main_list_filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Main_list_item> filteredList=new ArrayList<>();
            if(constraint ==null || constraint.length()==0){
                filteredList.addAll(main_list_items_full);
            }
            else {
                String filterPattern=constraint.toString().toLowerCase().trim();

                for(Main_list_item item :main_list_items_full){
                    if(item.getMain_item_name().toLowerCase().contains(filterPattern)){
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
            main_list_items.clear();
            main_list_items.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
    public interface OnItemClickListener{
        void onItemClick(int position, TextView main_name);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener =listener;
    }
}
