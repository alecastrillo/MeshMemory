package com.ce2103.itcr.meshmemory.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ce2103.itcr.meshmemory.*;

public class Manager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        Button btnrdymng=(Button)findViewById(R.id.btnrdymng);
        btnrdymng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editPort=(EditText)findViewById(R.id.edtptomng);
                String portmng=editPort.getText().toString();
                if(TextUtils.isEmpty(portmng)){
                    Toast.makeText(Manager.this, "You didn't enter port number", Toast.LENGTH_SHORT).show();
                    return;
                }
                EditText editIP=(EditText)findViewById(R.id.edtipmng);
                String ipmng=editIP.getText().toString();
                if(TextUtils.isEmpty(ipmng)){
                    Toast.makeText(Manager.this, "You didn't enter IP", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    //Falta guardar informacion
                    Intent mng = new Intent(v.getContext(),MainManager.class);
                    startActivityForResult(mng,0);
                }
            }
        });
    }
}
