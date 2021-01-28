package com.it_intensive.eli3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class ActivityChangeIp extends Activity {
    private EditText editIp;
    private Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_change_ip);

        editIp = findViewById(R.id.edit_ip);
        btnChange = findViewById(R.id.btn_change);

        btnChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MobiusConfig.changeIp(editIp.getText().toString());
                finish();
            }
        });
    }
}
