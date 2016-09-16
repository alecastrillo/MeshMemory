package com.ce2103.itcr.meshmemory.server;

/**
 * Created by estape11 on 09/09/16.
 */

import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import com.ce2103.itcr.meshmemory.gui.Nodo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.*;

public class Server extends Thread {
    private ServerSocket servidor;
    private static String host;
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private static int puerto;
    private Thread hiloServer;
    private Thread hiloCliente;
    private Socket socketCL;
    public static DoubleLinkedList listaSockets = new DoubleLinkedList();
    private DoubleLinkedList listNodes=new DoubleLinkedList();
    private String metodo="funcion";

    public Server() {
        this.socket = null;
        this.entrada = null;
        this.salida = null;
        this.servidor = null;
        this.hiloServer = null;
        this.hiloCliente = null;
    }

    public void startServer(int puerto) {
        this.puerto=puerto;
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
                            //escribir(socket," Conexion establecida!");
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
                            JsonParser parser = new JsonParser();
                            JsonElement mensajeCODE = parser.parse(mensaje);
                            String remitente = mensajeCODE.getAsJsonObject().get("remitente").getAsString();
                            System.out.println(remitente);
                            if (remitente.equals("cliente")) {
                                read_client(sock, mensajeCODE);
                            } else if (remitente.equals("node")) {
                                read_node(sock,mensajeCODE);
                            }
                            System.out.println("Recibido: " + mensaje);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        leer_hilo.start();
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
    public void escribir(final String dato){
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

    public void startClient(final String host, final int puerto) {
        this.host=host;
        this.puerto=puerto;
        this.hiloCliente=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(host, puerto);
                    AgregarSocket(socket);
                    leer(socket);
                } catch (UnknownHostException ue) {} catch (IOException ie) {}
            }
        });
        this.hiloCliente.start();
    }

    //Cierra un socket de comunicacion
    public void close(Socket socket) {
        try {
            socket.close();
        } catch (IOException ioe) {
        }
    }

    public void close() throws IOException {
        try {
            this.servidor.close();
            this.socket = null;
            this.entrada = null;
            this.salida = null;
            this.servidor = null;
            this.hiloServer = null;
            this.hiloCliente = null;
            this.listaSockets =new DoubleLinkedList();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void addNodeSocket(NodeSocket nodeSocket) {
        boolean result = false;
        if (this.listNodes != null) {
            for (int s = 0; s < this.listNodes.size(); s++) {
                if (this.listNodes.get(s).equals(nodeSocket)) {
                    result = true;
                    break;
                } else {
                    continue;
                }
            }
            if (result == false) {
                this.listaSockets.add(nodeSocket);
            }
        }
        else {
            this.listaSockets.add(nodeSocket);
        }
    }

    public void read_client(Socket sock, JsonElement mensajeCODE) throws InterruptedException {
        JsonParser parser=new JsonParser();
        JsonObject respuestaJSON=new JsonObject();
        String respuesta;
        String funcion = mensajeCODE.getAsJsonObject().get(metodo).getAsString();
        if (funcion.equals("token")){
            respuestaJSON.addProperty("token","a3s4f5f62");
            respuesta= respuestaJSON.toString();
            escribir(sock,respuesta);
        }

    }

    public void read_node(Socket sock, JsonElement mensajeCODE ){
        JsonParser parser=new JsonParser();
        JsonObject respuestaJSON=new JsonObject();
        String respuesta;
        String funcion = mensajeCODE.getAsJsonObject().get(metodo).getAsString();
        if (funcion.equals("addNode")){
            int bytes=mensajeCODE.getAsJsonObject().get("bytes").getAsInt();
            NodeSocket node=new NodeSocket(sock,sock.toString(),bytes);
            addNodeSocket(node);
        }
    }

    public void codeEntry(String entry){

    }
}