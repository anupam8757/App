package com.me.home;

import android.content.Context;

public class Main_list_item {
//  image that to display
  private int main_item_image;

  private String main_item_name;

  public Main_list_item(int main_item_image, String main_item_name) {
    this.main_item_image = main_item_image;
    this.main_item_name = main_item_name;
  }

  public int getMain_item_image() {
    return main_item_image;
  }

  public String getMain_item_name() {
    return main_item_name;
  }
}
