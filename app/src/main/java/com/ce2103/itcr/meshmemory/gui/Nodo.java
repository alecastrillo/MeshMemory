package com.ce2103.itcr.meshmemory.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.ce2103.itcr.meshmemory.*;

public class Nodo extends AppCompatActivity {

    public static boolean master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nodo);

        Button btnmst=(Button)findViewById(R.id.btnmst);
        btnmst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(){

                }*/
                master=true;
                Intent datamst= new Intent(v.getContext(),Datos_nodo.class);
                startActivityForResult(datamst,0);
                /*else{
                    Toast.makeText(Nodo.this, "Not master role available", Toast.LENGTH_SHORT).show();
                    return;
                }*/
            }
        });
        Button btnslv=(Button)findViewById(R.id.btnslv);
        btnslv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(){
                }*/
                master=false;
                Intent dataslv=new Intent(v.getContext(),Datos_nodo.class);
                startActivityForResult(dataslv,1);
               /* else{
                    Toast.makeText(Nodo.this, "Not slave role available", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }
}
