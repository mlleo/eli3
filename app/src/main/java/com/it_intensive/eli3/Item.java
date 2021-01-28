package com.it_intensive.eli3;

import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;


public class Item {
    public Drawable drawIamge;
    public String name = "";
    public Point grid = new Point(0, 0);
    public int weight = 0;
    public int price = 0;
    public int count = 1;

    public Item(){}
    public Item(String name, int price){
        this.name = name;
        this.price = price;
        this.drawIamge = ContextCompat.getDrawable(App.getContext(),
                                              App.getContext().getResources().getIdentifier(
                                                      "item_" + name.toLowerCase(),
                                                      "drawable",
                                                      App.getContext().getPackageName()));
    }

    public static Item parseItem(String html){
        Item item = new Item();
        int nextIdx = 0;
        while(html.charAt(nextIdx++) != '>');
        while(html.charAt(nextIdx) != '<')
            item.name += html.charAt(nextIdx++);
        while(html.charAt(nextIdx++) != '[');
        while(html.charAt(nextIdx) != ','){
            item.grid.x *= 10;
            item.grid.x += html.charAt(nextIdx++) - '0';
        }
        while(html.charAt(++nextIdx) != ']'){
            item.grid.y *= 10;
            item.grid.y += html.charAt(nextIdx) - '0';
        }
        while(html.charAt(nextIdx++) != '>');
        while(html.charAt(nextIdx++) != '>');
        while(html.charAt(nextIdx) != '<'){
            item.weight *= 10;
            item.weight += html.charAt(nextIdx++) - '0';
        }
        while(html.charAt(nextIdx++) != '>');
        while(html.charAt(nextIdx++) != '>');
        while(html.charAt(nextIdx) != '<'){
            item.price *= 10;
            item.price += html.charAt(nextIdx++) - '0';
        }
        item.drawIamge = ContextCompat.getDrawable(App.getContext(),
                                                App.getContext().getResources().getIdentifier(
                                                        "item_" + Mart.getInstance().transKorToEng(item.name).toLowerCase(),
                                                        "drawable",
                                                        App.getContext().getPackageName()));
        return item;
    }

    @Override
    public boolean equals(Object obj) {
        return name.equals(((Item) obj).name) && grid.equals(((Item) obj).grid) && weight == ((Item) obj).weight && price == ((Item) obj).price;
    }
}


class ItemsDataAdapter extends RecyclerView.Adapter<ItemsDataAdapter.ItemViewHolder> {
    public LinkedList<Item> listItem;

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cardItem;
        ImageView imgItem;
        TextView txtName;
        TextView txtPrice;
        TextView txtCount;
        Button btnDecrease;
        Button btnIncrease;

        public ItemViewHolder(View view) {
            super(view);
            cardItem = view.findViewById(R.id.card_item);
            imgItem = view.findViewById(R.id.img_item);
            txtName = view.findViewById(R.id.txt_name);
            txtPrice = view.findViewById(R.id.txt_price);
            txtCount = view.findViewById(R.id.txt_count);
            btnDecrease = view.findViewById(R.id.btn_decrease);
            btnIncrease = view.findViewById(R.id.btn_increase);
        }
    }

    public ItemsDataAdapter(LinkedList<Item> listItem) {
        this.listItem = listItem;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_item, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        Item item = ShoppingCart.getInstance().listItem.get(position);
        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShoppingCart.getInstance().decrementItem(position);
                notifyDataSetChanged();
            }
        });
        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShoppingCart.getInstance().incrementItem(position);
                notifyDataSetChanged();
            }
        });
        holder.imgItem.setImageDrawable(item.drawIamge);
        holder.txtName.setText(item.name);
        holder.txtPrice.setText(item.price + "원");
        holder.txtCount.setText(item.count + "");
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}