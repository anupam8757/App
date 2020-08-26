package com.me.home;

import android.widget.Toast;

import com.me.R;

import java.util.ArrayList;
// this class is static and contains all the categories item

public class  Data {

    public static   ArrayList<Main_list_item> assign_main_item() {

        ArrayList<Main_list_item> main_list_items=new ArrayList<Main_list_item>();
        main_list_items.add(new Main_list_item(R.drawable.food_min_min,"RESTAURANT FOODS & CUISINES"));
        main_list_items.add(new Main_list_item(R.drawable.bakerycakesanddairy_min,"BAKERY,CAKES & DAIRY"));
        main_list_items.add(new Main_list_item(R.drawable.vegetables_min,"VEGETABLE"));
        main_list_items.add(new Main_list_item(R.drawable.grocery_min,"GROCERIES"));
        main_list_items.add(new Main_list_item(R.drawable.fruits_min,"FRUITS"));
        main_list_items.add(new Main_list_item(R.drawable.beverages_min,"BEVERAGES"));
        main_list_items.add(new Main_list_item(R.drawable.snacks_min,"SNACKS & PACKED FOOD"));
        main_list_items.add(new Main_list_item(R.drawable.patanjali_logo,"PATANJALI ITEMS"));
        main_list_items.add(new Main_list_item(R.drawable.household_min,"CLEANING & HOUSEHOLD"));
        main_list_items.add(new Main_list_item(R.drawable.sweets,"SWEETS"));
        main_list_items.add(new Main_list_item(R.drawable.nonveg_min,"EGG,MEAT & FISH"));
        main_list_items.add(new Main_list_item(R.drawable.babycare_min,"BABY CARE"));
        main_list_items.add(new Main_list_item(R.drawable.other_min,"OTHER ITEMS"));


        return main_list_items;
    }
}
