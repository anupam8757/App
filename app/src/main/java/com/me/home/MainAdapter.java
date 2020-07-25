package com.me.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.me.R;

import java.util.ArrayList;

public class MainAdapter extends ArrayAdapter<Main_list_item> {
    private static final String LOG_TAG = MainAdapter.class.getSimpleName();

    public MainAdapter(Context context, ArrayList<Main_list_item> main_list_items){

        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context,0,main_list_items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView=convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.main_list_item, parent, false);
        }
      Main_list_item current_main_item= getItem(position);

//        taking the refence of both the item in the main-list-item.xml
        ImageView main_item_image=listItemView.findViewById(R.id.main_item_image);
        TextView main_item_name=listItemView.findViewById(R.id.main_item_name);

        main_item_image.setImageResource(current_main_item.getMain_item_image());
        main_item_name.setText(current_main_item.getMain_item_name());
        return listItemView;
    }
}
