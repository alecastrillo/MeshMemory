package com.ce2103.itcr.meshmemory.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ce2103.itcr.meshmemory.*;
import com.ce2103.itcr.meshmemory.server.ManagerServer;
import com.ce2103.itcr.meshmemory.server.Utils;

public class Manager extends AppCompatActivity {
    public ManagerServer servidor= new ManagerServer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        Utils utilidad=new Utils();
        Button btnrdymng=(Button)findViewById(R.id.btnrdymng);
        TextView ip=(TextView)findViewById(R.id.textViewIP);
        ip.setText(utilidad.getIPAddress(true));
        btnrdymng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editPort=(EditText)findViewById(R.id.edtptomng);
                String portmng=editPort.getText().toString();
                if(TextUtils.isEmpty(portmng)){
                    Toast.makeText(Manager.this, "You didn't enter port number", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*
                EditText editIP=(EditText)findViewById(R.id.edtipmng);
                String ipmng=editIP.getText().toString();
                if(TextUtils.isEmpty(ipmng)){
                    Toast.makeText(Manager.this, "You didn't enter IP", Toast.LENGTH_SHORT).show();
                    return;
                }
                */
                else{
                    //Falta guardar informacion
                    Toast.makeText(Manager.this, "Starting the server...", Toast.LENGTH_SHORT).show();
                    servidor.startServer(Integer.parseInt(editPort.getText().toString()));
                    Intent mng = new Intent(v.getContext(),MainManager.class);
                    startActivityForResult(mng,0);
                }
            }
        });
    }


}
