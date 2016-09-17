package com.ce2103.itcr.meshmemory.server;

/**
 * Created by estape11 on 09/09/16.
 */

import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.*;

public class Server extends Thread {
    private ServerSocket servidor;
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private int puerto;
    private Thread hiloServer;
    private DoubleLinkedList listaSockets;
    private DoubleLinkedList listNodes;
    private String metodo="funcion";

    public Server() {
        this.listaSockets = new DoubleLinkedList();
        this.listNodes=new DoubleLinkedList();
        this.socket = null;
        this.entrada = null;
        this.salida = null;
        this.servidor = null;
        this.hiloServer = null;
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
                            readData(socket);
                        } catch (Exception e) {e.printStackTrace();}
                    }
                }
            });
            hiloServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                            String remitente = mensajeCODE.get("remitente").getAsString();
                            if (remitente.equals("cliente")) {
                                readClient(sock, mensajeCODE);
                            } else if (remitente.equals("node")) {
                                readNode(sock,mensajeCODE);
                            }
                        }
                    }
                } catch (IOException e) {e.printStackTrace();} catch (InterruptedException e) {e.printStackTrace();}
            }
        });
        leer_hilo.start();
    }

    public void writeData(final Socket socket, final String dato){
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

    public void close(){
        try {
            this.socket.close();
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

    public void readClient(Socket sock, JsonObject mensajeCODE) throws InterruptedException, IOException {
        JsonObject respuestaJSON=new JsonObject();
        Decoder decodicador=new Decoder(mensajeCODE,"cliente");
        int funcion=decodicador.Decode();
        switch (funcion){
            case 0:{
                respuestaJSON.addProperty("token","a3s4f5f62"); //aqui va el token
                writeData(socket,respuestaJSON.toString());
            }
            case 1:{

            }
        }
/*
        else if(funcion.equals("xMalloc")){
            int bytes=mensajeCODE.getAsJsonObject().get("bytes").getAsInt();
            int tipo=mensajeCODE.getAsJsonObject().get("type").getAsInt();
            int index=findSpace(bytes);
            JsonObject output=new JsonObject();
            if(index<0){
                output.addProperty("UUID","no espacio");
                writeData(sock,output.toString());
            }
            else{
                NodeSocket temp = (NodeSocket) listNodes.get(index);
                JsonObject peticion=new JsonObject();
                peticion.addProperty("remitente","server");
                peticion.addProperty("funcion","alloc");
                peticion.addProperty("bytes",bytes);
                peticion.addProperty("type",tipo);
                writeData(temp.getSocket(),peticion.toString());
                String mensaje= entrada.readLine();
                System.out.println(mensaje);
                output.addProperty("UUID","hola");
                writeData(sock,output.toString());
            }
        }
        */

    }

    public int findSpace(int bytes){
        int index=-2;
        NodeSocket temp;
        for(int i=0;i<listNodes.size();i++){
            temp=(NodeSocket) listNodes.get(i);
            if (temp.getBytes()>bytes){
                index=i;
                break;
            }
        }
        return index;
    }

    public void readNode(Socket sock, JsonObject mensajeCODE ){
        JsonObject respuestaJSON=new JsonObject();
        Decoder decodificador=new Decoder(mensajeCODE,"nodo");
        int funcion=decodificador.Decode();
        switch (funcion){
            case 0:{
                respuestaJSON.addProperty("respuesta","aceptado");
                writeData(sock,respuestaJSON.toString());
            }
        }
        /*
        if (funcion.equals("addNode")){
            int bytes=mensajeCODE.getAsJsonObject().get("bytes").getAsInt();
            NodeSocket node=new NodeSocket(sock,sock.toString(),bytes);
            //addNodeSocket(node);
            listNodes.add(node);
            System.out.println("Nodo agregado: "+node.getBloque().toString());
        }
        */
    }
}