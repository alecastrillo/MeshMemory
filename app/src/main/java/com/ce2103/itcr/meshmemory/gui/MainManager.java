package com.ce2103.itcr.meshmemory.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ce2103.itcr.meshmemory.*;
import com.ce2103.itcr.meshmemory.server.Utils;
import com.google.gson.JsonObject;

public class MainManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manager);
        Utils utilidad=new Utils();
        TextView iptext=(TextView)findViewById(R.id.textViewIP);
        iptext.setText(utilidad.getIPAddress(true));
        Button btnbrp=(Button)findViewById(R.id.btnbrp);
        btnbrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent burping= new Intent(v.getContext(),Burping.class);
                //startActivityForResult(burping,0);
                Manager.servidor.burpingInterno();
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

        Button btnerror=(Button)findViewById(R.id.buttonLog);
        btnerror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent error=new Intent(v.getContext(),Error_logs.class);
                startActivityForResult(error,0);
            }
        });
    }
}
