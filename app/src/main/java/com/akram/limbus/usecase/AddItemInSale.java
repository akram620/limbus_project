package com.akram.limbus.usecase;

import android.annotation.SuppressLint;

import com.akram.limbus.activity.oformit;
import com.akram.limbus.model.AboutUser;

import java.util.ArrayList;
import java.util.List;

public class AddItemInSale {
    static List<AboutUser.CategoryNewItems> categoryNewItemsList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public static void additemsalefunc(String id, int number, String name, int qwentity, int price){
        categoryNewItemsList.add(new AboutUser.CategoryNewItems(id, number, name, qwentity, price));
        oformit.adapterNewItems.notifyDataSetChanged();
    }

    public static int getSize(){
        return categoryNewItemsList.size();
    }

    public static List<AboutUser.CategoryNewItems> returnList(){
        return categoryNewItemsList;
    }

}