package com.it_intensive.eli3;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ShoppingCart {
    public LinkedList<Item> listItem;
    public int sumWeight;
    public int sumPrice;

    private static ShoppingCart instance;
    private ShoppingCart(){
        listItem = new LinkedList<>();
        sumWeight = 0;
        sumPrice = 0;

        listItem.add(new Item("Milk", 2000));
        listItem.add(new Item("Bread", 3000));
        listItem.add(new Item("Strawberry", 5000));
        listItem.add(new Item("Milk", 2000));
        listItem.add(new Item("Bread", 3000));
        listItem.add(new Item("Strawberry", 5000));
        listItem.add(new Item("Milk", 2000));
        listItem.add(new Item("Bread", 3000));
        listItem.add(new Item("Strawberry", 5000));
        listItem.add(new Item("Milk", 2000));
        listItem.add(new Item("Bread", 3000));
        listItem.add(new Item("Strawberry", 5000));
    }

    public static ShoppingCart getInstance(){
        if(instance == null)
            instance = new ShoppingCart();
        return instance;
    }

    public void addItemToCart(Item item){
        boolean isAreadyExist = false;
        for(Item i : listItem){
            if(i.equals(item)){
                i.count++;
                isAreadyExist = true;
                break;
            }
        }
        if(!isAreadyExist) listItem.add(item);

        sumWeight += item.price;
        sumPrice -= item.price;
    }

    public void removeItemFromCart(int indexItem){
        Item item = listItem.get(indexItem);
        sumWeight -= item.weight * item.count;
        sumPrice -= item.price * item.count;
        listItem.remove(indexItem);
    }

    public void decrementItem(int indexItem){
        Item item = listItem.get(indexItem);
        if(item.count > 1){
            item.count--;
        }
    }

    public void incrementItem(int indexItem){
        Item item = listItem.get(indexItem);
        item.count++;
    }
}
