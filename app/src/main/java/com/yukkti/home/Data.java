package com.yukkti.home;

import com.yukkti.R;

import java.util.ArrayList;
// this class is static and contains all the categories item

public class  Data {

    public static   ArrayList<Main_list_item> assign_main_item() {

        ArrayList<Main_list_item> main_list_items=new ArrayList<Main_list_item>();
        main_list_items.add(new Main_list_item(R.mipmap.restrorennnttt,"RESTAURANT FOODS & CUISINES"));
        main_list_items.add(new Main_list_item(R.mipmap.bakerycakesanddairy_min,"BAKERY,CAKES & DAIRY"));
        main_list_items.add(new Main_list_item(R.mipmap.sweets,"SWEETS"));
        main_list_items.add(new Main_list_item(R.mipmap.amul,"AMUL"));
        main_list_items.add(new Main_list_item(R.mipmap.ptanajli,"PATANJALI ITEMS"));
        main_list_items.add(new Main_list_item(R.mipmap.grocery_min,"GROCERIES"));
        main_list_items.add(new Main_list_item(R.mipmap.fruits,"FRUITS"));
        main_list_items.add(new Main_list_item(R.mipmap.beverages_min,"BEVERAGES"));
        main_list_items.add(new Main_list_item(R.mipmap.snacks_min,"SNACKS & PACKED FOOD"));
        main_list_items.add(new Main_list_item(R.mipmap.cleanin,"CLEANING & HOUSEHOLD"));
        main_list_items.add(new Main_list_item(R.mipmap.nonveg_min,"EGG,MEAT & FISH"));
        main_list_items.add(new Main_list_item(R.mipmap.babycare,"BABY CARE"));
        main_list_items.add(new Main_list_item(R.mipmap.new_vegetable,"VEGETABLE"));
        main_list_items.add(new Main_list_item(R.mipmap.other_min,"OTHER ITEMS"));
        return main_list_items;
    }
}
