package com.ce2103.itcr.meshmemory.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ce2103.itcr.meshmemory.*;

public class Available_mem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_mem);
        TextView numbytes=(TextView)findViewById(R.id.numbytes);
        String dato= Integer.toString(Master.cliente.getAvailableBytes())+ " bytes";
        numbytes.setText(dato);

    }
}
