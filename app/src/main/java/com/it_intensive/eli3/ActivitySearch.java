package com.it_intensive.eli3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class ActivitySearch extends Activity {
    private final int REQUEST_ACT = 1;

    private EditText editItem;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);

//        Intent intent = getIntent();
        // Object value = intent.get"Type"Extra(key);

        editItem = findViewById(R.id.edit_item);
        btnSearch = findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.putExtra("request_item", editItem.getText().toString());
                setResult(Mart.getInstance().isKorKey(editItem.getText().toString()) ? RESULT_OK : RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}
