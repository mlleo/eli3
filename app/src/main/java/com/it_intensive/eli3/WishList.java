package com.it_intensive.eli3;

import java.util.LinkedList;

public class WishList {
    public LinkedList<Item> listItem;


    private static WishList instance;
    private WishList(){
        listItem = new LinkedList<>();
    }

    public static WishList getInstance(){
        if(instance == null)
            instance = new WishList();
        return instance;
    }

    public void addItemToWishList(Item item){
        for(Item i : listItem)
            if(i.equals(item)) return ;
        listItem.add(item);
    }

    public void removeItemFromWishList(int indexItem){
        listItem.remove(indexItem);
    }

    public LinkedList<Item> getListItem() {
        return listItem;
    }
}
