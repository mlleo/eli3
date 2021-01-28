package com.it_intensive.eli3;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ListView;


public class ActivityViewCart extends AppCompatActivity{
    private RecyclerView recyclerItem;
    private ItemsDataAdapter adapterItems;
    private SwipeController controllerSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        adapterItems = new ItemsDataAdapter(ShoppingCart.getInstance().listItem);
//        adapterItem.addItem(ContextCompat.getDrawable(this, getResources().getIdentifier("item_" + item.name.toLowerCase(), "drawable", getPackageName())), item);

        recyclerItem = findViewById(R.id.recycle_items);
        recyclerItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerItem.setAdapter(adapterItems);

        controllerSwipe = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                adapterItems.listItem.remove(position);
                adapterItems.notifyItemRemoved(position);
                adapterItems.notifyItemRangeChanged(position, adapterItems.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(controllerSwipe);
        itemTouchhelper.attachToRecyclerView(recyclerItem);

        recyclerItem.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                controllerSwipe.onDraw(c);
            }
        });
    }
}
