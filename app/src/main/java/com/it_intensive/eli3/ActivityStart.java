package com.it_intensive.eli3;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;


public class ActivityStart extends AppCompatActivity {
    private Button btnStart;
    private Button btnChangeIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setIds();

        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ActivityStart.this, ActivityMain.class);
                // intent.putExtra(key, value);
                startActivity(intent);
            }
        });
        btnChangeIp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ActivityStart.this, ActivityChangeIp.class);
                startActivity(intent);
            }
        });
    }

    private void setIds(){
        btnStart = findViewById(R.id.btn_start);
        btnChangeIp = findViewById(R.id.btn_change_ip);
    }
}
