package com.ce2103.itcr.meshmemory.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.ce2103.itcr.meshmemory.*;

public class Error_logs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_logs);
        final TextView logTextView=(TextView) findViewById(R.id.logTextView);
        logTextView.setMovementMethod(new ScrollingMovementMethod());
        if(!Manager.servidor.getLog().equals("")){
            logTextView.setText(Manager.servidor.getLog());
        }
        if(!Master.cliente.getLog().equals("")){
            logTextView.setText(Master.cliente.getLog());
        }

    }
}
