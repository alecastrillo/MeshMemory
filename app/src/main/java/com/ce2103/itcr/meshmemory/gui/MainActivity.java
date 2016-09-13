package com.ce2103.itcr.meshmemory.gui;

/**
 * Created by estape11 on 12/09/16.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ce2103.itcr.meshmemory.*;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnmng=(Button)findViewById(R.id.btnMng);
        btnmng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Manager.class);
                startActivityForResult(intent,0);
            }
        });

        Button btnode=(Button)findViewById(R.id.btnode);
        btnode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent node = new Intent (v.getContext(),Nodo.class);
                startActivityForResult(node,1);
            }
        } );
    }
}