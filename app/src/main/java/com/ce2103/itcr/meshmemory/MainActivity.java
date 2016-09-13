package com.ce2103.itcr.meshmemory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ce2103.itcr.meshmemory.Server;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private Server servidor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        Toast.makeText(this, "¡Bienvenido!",
                Toast.LENGTH_SHORT).show();
        servidor= new Server();
        servidor.startServer();

        Button clickButton = (Button) findViewById(R.id.button);
        final EditText cajita= (EditText) findViewById(R.id.editText);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servidor.escribir((Socket)servidor.listaSockets.get(0),cajita.getText().toString());
            }
        });
        */
        Toast.makeText(this, "¡Bienvenido!",
                Toast.LENGTH_SHORT).show();
        servidor= new Server();
        Button clickButton = (Button) findViewById(R.id.button);
        final EditText cajita= (EditText) findViewById(R.id.editText);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    System.out.println("Trying to connect to the host: "+cajita.getText().toString());
                    servidor.startClient(cajita.getText().toString(),8080);
                }catch(Exception e){}
            }
        });
    }
}