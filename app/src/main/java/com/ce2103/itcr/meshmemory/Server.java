package com.ce2103.itcr.meshmemory;

/**
 * Created by estape11 on 09/09/16.
 */

import android.widget.Toast;
import java.io.*;
import java.net.*;

public class Server extends Thread {
    private ServerSocket servidor;
    private static String host = "192.168.100.3";
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private static int puerto = 8080;
    private Thread hiloServer;
    public static DoubleLinkedList listaSockets = new DoubleLinkedList();

    public void Server() {
        this.socket = null;
        this.entrada = null;
        this.salida = null;
        this.servidor = null;
        this.hiloServer = null;
    }

    public void startServer() {
        try {
            servidor = new ServerSocket(puerto);
            System.out.println("Servidor iniciado");
            this.hiloServer = new Thread(new Runnable() {
                public void run() {
                    while (true){
                        try {
                            socket =servidor.accept();
                            AgregarSocket(socket);
                            System.out.println("Nuevo cliente conectado: "+String.valueOf(socket));
                            leer(socket);
                            escribir(socket,"Bienvenido ak7 ak7");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            hiloServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leer(final Socket sock){
        Thread leer_hilo=new Thread(new Runnable(){
            public void run(){
                try{
                    entrada =new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    while(true){
                        String mensaje= entrada.readLine();
                        if (mensaje!=null) {
                            if(mensaje.contains("client")){
                                //read_client()
                            }else if (mensaje.contains("node")){
                                //read_node(); agregar este métodop
                            }
                            System.out.println("Recibido: " + mensaje);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        leer_hilo.start();
    }

    public void read_client(){

    }

    public void read_node(){

    }

    public void codeEntry(String entry){

    }

    public void escribir(final Socket socket, final String dato){
        Thread escribir_hilo=new Thread(new Runnable(){
            public void run(){
                try{
                    if(dato!=null){
                        salida = new PrintWriter(socket.getOutputStream(),true);
                        salida.println(dato);
                        System.out.println("Enviado:"+dato);
                    }
                }catch(Exception ex){

                }
            }
        });
        escribir_hilo.start();
    }

    public void startClient() {
        try {
            //Creamos nuestro socket
            socket = new Socket(host, puerto);
            this.entrada = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            //send("HELLO MOTO");
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida.");
        }
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException ioe) {
        }
    }

    public void AgregarSocket(Socket socket1) {
        boolean result = false;
        if (this.listaSockets != null) {
            for (int s = 0; s < this.listaSockets.size(); s++) {
                if (this.listaSockets.get(s).equals(socket1)) {
                    result = true;
                    break;
                } else {
                }
            }
            if (result == false) {
                this.listaSockets.add(socket1);
            } else {
            }
        } else {
            this.listaSockets.add(socket1);
        }
    }
}