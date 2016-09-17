package com.ce2103.itcr.meshmemory.server;

/**
 * Created by estape11 on 09/09/16.
 */

import android.app.job.JobScheduler;

import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import com.ce2103.itcr.meshmemory.gui.Datos_nodo;
import com.ce2103.itcr.meshmemory.gui.Nodo;
import com.ce2103.itcr.meshmemory.memoryblocks.Node;
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
    //public Node node;

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
                        System.out.println("Recibido: " + mensaje);
                        if (mensaje!=null) {
                            JsonParser parser = new JsonParser();
                            JsonElement mensajeCODE = parser.parse(mensaje);
                            String remitente = mensajeCODE.getAsJsonObject().get("remitente").getAsString();
                            if (remitente.equals("cliente")) {
                                read_client(sock, mensajeCODE);
                            } else if (remitente.equals("node")) {
                                read_node(sock,mensajeCODE);
                            } else if (remitente.equals("server")){
                                read_server(mensajeCODE);
                            }

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
        System.out.println("2");
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
                    System.out.println("Conectado a: "+socket.toString());
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

    public void read_client(Socket sock, JsonElement mensajeCODE) throws InterruptedException, IOException {
        JsonParser parser=new JsonParser();
        JsonObject respuestaJSON=new JsonObject();
        String respuesta;
        String funcion = mensajeCODE.getAsJsonObject().get(metodo).getAsString();
        if (funcion.equals("token")){
            respuestaJSON.addProperty("token","a3s4f5f62");
            respuesta= respuestaJSON.toString();
            escribir(sock,respuesta);
        }
        else if(funcion.equals("xMalloc")){
            int bytes=mensajeCODE.getAsJsonObject().get("bytes").getAsInt();
            int tipo=mensajeCODE.getAsJsonObject().get("type").getAsInt();
            int index=findSpace(bytes);
            JsonObject output=new JsonObject();
            if(index<0){
                output.addProperty("UUID","no espacio");
                escribir(sock,output.toString());
            }
            else{
                NodeSocket temp = (NodeSocket) listNodes.get(index);
                JsonObject peticion=new JsonObject();
                peticion.addProperty("remitente","server");
                peticion.addProperty("funcion","alloc");
                peticion.addProperty("bytes",bytes);
                peticion.addProperty("type",tipo);
                escribir(temp.getSocket(),peticion.toString());
                String mensaje= entrada.readLine();
                System.out.println(mensaje);
                output.addProperty("UUID","hola");
                escribir(sock,output.toString());

            }

        }

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

    public void read_node(Socket sock, JsonElement mensajeCODE ){
        JsonParser parser=new JsonParser();
        JsonObject respuestaJSON=new JsonObject();
        String respuesta;
        String funcion = mensajeCODE.getAsJsonObject().get(metodo).getAsString();
        if (funcion.equals("addNode")){
            int bytes=mensajeCODE.getAsJsonObject().get("bytes").getAsInt();
            NodeSocket node=new NodeSocket(sock,sock.toString(),bytes);
            //addNodeSocket(node);
            listNodes.add(node);
            System.out.println("Nodo agregado: "+node.getBloque().toString());
        }
    }

    public void read_server(JsonElement mensajeCODE){
        String funcion=mensajeCODE.getAsJsonObject().get(metodo).getAsString();
        if(funcion.equals("alloc")){
            int tipo=mensajeCODE.getAsJsonObject().get("type").getAsInt();
            int bytes=mensajeCODE.getAsJsonObject().get("bytes").getAsInt();
            String UUID=Datos_nodo.node.allocMem(tipo,bytes);
            JsonObject output=new JsonObject();
            output.addProperty("UUID",UUID);
            escribir(output.toString());
        }
    }

    public void codeEntry(String entry){

    }
}