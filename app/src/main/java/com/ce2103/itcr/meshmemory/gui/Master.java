package com.ce2103.itcr.meshmemory.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ce2103.itcr.meshmemory.*;
import com.ce2103.itcr.meshmemory.server.Client;
import com.ce2103.itcr.meshmemory.server.Server;
import com.ce2103.itcr.meshmemory.server.Utils;
import com.google.gson.JsonObject;

import java.io.IOException;

public class Master extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        TextView ipTEXT=(TextView)findViewById(R.id.textViewIP);
        ipTEXT.setText(Datos_nodo.ip); //Ajusto el ip al que estoy conectado
        Toast.makeText(Master.this, "Connecting to the manager "+Datos_nodo.ip, Toast.LENGTH_SHORT).show();

        JsonObject output=new JsonObject();
        output.addProperty("remitente","nodo");
        output.addProperty("funcion","addNode");
        output.addProperty("numero",Datos_nodo.number);
        output.addProperty("bytes",Datos_nodo.bytes);
        output.addProperty("master",true);

        Datos_nodo.cliente.writeData(output.toString());

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
