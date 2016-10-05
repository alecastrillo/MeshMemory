package com.ce2103.itcr.meshmemory.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ce2103.itcr.meshmemory.*;
import com.ce2103.itcr.meshmemory.server.NodeClient;

public class Datos_nodo extends AppCompatActivity {
    public static String ip;
    public static int port;
    public static NodeClient cliente= new NodeClient();
    public static int number;
    public static int bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_nodo);

        Button btnrdynde=(Button)findViewById(R.id.btnrdy);
        btnrdynde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtxtportmng=(EditText)findViewById(R.id.edtpto);
                String dtportmng=edtxtportmng.getText().toString();
                if(TextUtils.isEmpty(dtportmng)){
                    edtxtportmng.setError("Specify manager port");
                }
                EditText edtxtipmng=(EditText)findViewById(R.id.editTextIP);
                String dtipmng=edtxtipmng.getText().toString();

                if(TextUtils.isEmpty(dtipmng)){
                    edtxtipmng.setError("Specify manager IP");
                }

                EditText edtxtmem=(EditText)findViewById(R.id.edtmem);
                String dtmem=edtxtmem.getText().toString();

                if(TextUtils.isEmpty(dtmem)){
                    edtxtmem.setError("Specify your available memory bytes");
                }

                EditText edtnum=(EditText)findViewById(R.id.edtnum);
                String dtnum=edtnum.getText().toString();

                if(TextUtils.isEmpty(dtnum)){
                    edtnum.setError("Specify your cellphone number");
                }
                /*
                else {
                    boolean h = Nodo.master;
                    if(dtportmng=="Something") {//something hay que cambiarlo por la lista de ale
                        if(dtipmng=="Somethin"){
                            if (h==true){
                                int memoria =0;
                                int numero=0;
                                try{
                                    memoria=Integer.parseInt(dtmem);
                                    numero=Integer.parseInt(dtnum);
                                }
                                catch(NumberFormatException nfe){
                                }

                                //enviar informacion como master a ale

                                Intent master= new Intent(v.getContext(),Master.class);
                                startActivityForResult(master,0);
                            }
                            else{
                                int memoria =0;
                                int numero=0;
                                try{
                                    memoria=Integer.parseInt(dtmem);
                                    numero=Integer.parseInt(dtnum);
                                }
                                catch(NumberFormatException nfe){
                                }
                                //enviar info a ale como slave
                                Intent slave=new Intent(v.getContext(),Master.class);
                                startActivityForResult(slave,0);
                            }
                        }

                        else{
                            Toast.makeText(Datos_nodo.this, "IP didn't match", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(Datos_nodo.this, "Port didn't match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }*/
                //Prueba cliente
                else{
                    ip=edtxtipmng.getText().toString();
                    port=Integer.parseInt(edtxtportmng.getText().toString());
                    bytes=Integer.parseInt(edtxtmem.getText().toString());
                    number=Integer.parseInt(edtnum.getText().toString());
                    //Se conecta con el servidor
                    cliente.startClient(ip,port,bytes,number);
                    Intent master= new Intent(v.getContext(),Master.class);
                    startActivityForResult(master,0);
                }
            }
        });
    }

}
