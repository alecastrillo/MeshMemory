package com.ce2103.itcr.meshmemory.server;

import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import com.ce2103.itcr.meshmemory.gui.Datos_nodo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by estape11 on 17/09/16.
 */
public class Client extends Thread {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private int puerto;
    private String host;
    private Thread hiloCliente;
    private DoubleLinkedList listaSockets;

    public Client() {
        this.socket = null;
        this.entrada = null;
        this.salida = null;
        this.hiloCliente = null;
        this.listaSockets = new DoubleLinkedList();
    }

    public void startClient(final String host, final int puerto) {
        this.host=host;
        this.puerto=puerto;
        this.hiloCliente=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(host, puerto);
                    System.out.println("Conectado a: "+socket.toString());
                    AgregarSocket(socket);
                    readData(socket);
                } catch (UnknownHostException ue) {} catch (IOException ie) {}
            }
        });
        this.hiloCliente.start();

    }

    public void readData(final Socket sock){
        Thread leer_hilo=new Thread(new Runnable(){
            public void run(){
                try{
                    entrada =new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    while(true){
                        String mensaje= entrada.readLine();
                        System.out.println("Recibido: " + mensaje);
                        if (mensaje!=null) {
                            JsonParser parser = new JsonParser();
                            JsonObject mensajeCODE = parser.parse(mensaje).getAsJsonObject();
                            //creo el objeto que desencripte el mensaje
                        }
                    }
                } catch (IOException e) {e.printStackTrace();}
            }
        });
        leer_hilo.start();
    }

    public void writeData(final String dato){
        Thread escribir_hilo=new Thread(new Runnable(){
            public void run(){
                try{
                    if(dato!=null){
                        salida = new PrintWriter(socket.getOutputStream(),true);
                        salida.println(dato);
                        System.out.println("Enviado: "+dato);
                    }
                }catch(Exception ex){

                }
            }
        });
        escribir_hilo.start();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException ioe) {}
    }

    private void AgregarSocket(Socket socket1) {
        boolean result = false;
        if (this.listaSockets != null) {
            for (int s = 0; s < this.listaSockets.size(); s++) {
                if (this.listaSockets.get(s).equals(socket1)) {
                    result = true;
                    break;
                } else {
                    continue;
                }
            }
            if (result == false) {
                this.listaSockets.add(socket1);
            }
        }
        else {
            this.listaSockets.add(socket1);
        }
    }

}