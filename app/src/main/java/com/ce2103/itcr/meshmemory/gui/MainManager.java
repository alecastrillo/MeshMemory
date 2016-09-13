package com.ce2103.itcr.meshmemory.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ce2103.itcr.meshmemory.*;

public class MainManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manager);

        Button btnbrp=(Button)findViewById(R.id.btnbrp);
        btnbrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent burping= new Intent(v.getContext(),Burping.class);
                startActivityForResult(burping,0);
            }
        });

        Button btnmem=(Button)findViewById(R.id.btnmem);
        btnmem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent memory=new Intent(v.getContext(),Memory.class);
                startActivityForResult(memory,0);
            }
        });

        Button btnalt=(Button)findViewById(R.id.btnalt);
        btnalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alerts=new Intent(v.getContext(),Alerts.class);
                startActivityForResult(alerts,0);
            }
        });
        Button btnnddt=(Button)findViewById(R.id.btndt);
        btnnddt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent node_data=new Intent(v.getContext(),Node_data.class);
                startActivityForResult(node_data,0);
            }
        });
        Button btngc=(Button)findViewById(R.id.btngc);
        btngc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent garbcol=new Intent(v.getContext(),GarbageCollection.class);
                startActivityForResult(garbcol,0);
            }
        });
    }
}
