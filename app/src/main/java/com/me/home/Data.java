package com.me.home;

import android.widget.Toast;

import com.me.R;

import java.util.ArrayList;
// this class is static and contains all the categories item
public class  Data {

    public static   ArrayList<Main_list_item> assign_main_item() {

        ArrayList<Main_list_item> main_list_items=new ArrayList<Main_list_item>();
        main_list_items.add(new Main_list_item(R.drawable.vegetables,"VEGETABLE"));
        main_list_items.add(new Main_list_item(R.drawable.grocery,"FRUITS"));
        main_list_items.add(new Main_list_item(R.drawable.fruits,"GROCERIES"));
        main_list_items.add(new Main_list_item(R.drawable.backgroundgreen,"WATER BOTTLES & DRINKS"));
        main_list_items.add(new Main_list_item(R.drawable.backgroundgreen,"MILK,DAIRY & BAKERY"));
        main_list_items.add(new Main_list_item(R.drawable.backgroundgreen,"MASALAS & MUSTARD OIL"));

//        Toast.makeText(get,"Completed",Toast.LENGTH_SHORT).show();
        return main_list_items;
    }
}
