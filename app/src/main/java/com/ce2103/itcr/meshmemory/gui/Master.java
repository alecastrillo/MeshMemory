package com.ce2103.itcr.meshmemory.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ce2103.itcr.meshmemory.*;
import com.ce2103.itcr.meshmemory.server.Server;

public class Master extends AppCompatActivity {
    public Server cliente= new Server();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        System.out.println("IP: "+Datos_nodo.ip+", Puerto: "+Datos_nodo.port);
        Toast.makeText(Master.this, "Connecting to the manager...", Toast.LENGTH_SHORT).show();
        cliente.startClient(Datos_nodo.ip,Datos_nodo.port );
        Button btnmemp=(Button)findViewById(R.id.btnmpmem);
        btnmemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map_memory=new Intent(v.getContext(),Memory_map.class);
                startActivityForResult(map_memory,0);
            }
        });

        Button btnmemav=(Button)findViewById(R.id.btnmemav);
        btnmemav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent avmem=new Intent(v.getContext(),Available_mem.class);
                startActivityForResult(avmem,0);
            }
        });

        Button btnsync=(Button)findViewById(R.id.btnsync);
        btnsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sync=new Intent(v.getContext(),Sync_Satatus.class);
                startActivityForResult(sync,0);
            }
        });

        Button btnerror=(Button)findViewById(R.id.btnerlogs);
        btnerror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent error=new Intent(v.getContext(),Error_logs.class);
                startActivityForResult(error,0);
            }
        });
    }
}
