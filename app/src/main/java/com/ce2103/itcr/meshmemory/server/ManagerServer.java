package com.ce2103.itcr.meshmemory.server;

/**
 * Created by estape11 on 09/09/16.
 */

import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import com.ce2103.itcr.meshmemory.datastructures.DoublyLinkedList;
import com.ce2103.itcr.meshmemory.datastructures.Node;
import com.ce2103.itcr.meshmemory.datastructures.NodeMem;
import com.ce2103.itcr.meshmemory.gui.Token;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.bind.ObjectTypeAdapter;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class ManagerServer extends Thread {
    private ServerSocket servidor;
    private Socket socket;
    private Socket socketCliente;
    private BufferedReader entrada;
    private PrintWriter salida;
    private int puerto;
    private Thread hiloServer;
    private DoubleLinkedList listaSockets;
    private DoublyLinkedList listNodes;
    private DoubleLinkedList listTokens;
    private String metodo="funcion";
    private String log; //log de los procesos

    public ManagerServer() {
        this.listaSockets = new DoubleLinkedList();
        this.listNodes=new DoublyLinkedList();
        this.listTokens= new DoubleLinkedList();
        this.socket = null;
        this.entrada = null;
        this.salida = null;
        this.servidor = null;
        this.hiloServer = null;
        this.log="";
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
                            socket = servidor.accept();
                            AgregarSocket(socket);
                            System.out.println("Nuevo cliente conectado: "+String.valueOf(socket));
                            log+=DateFormat.getDateTimeInstance().format(new Date())+"-> "+"Nuevo cliente conectado: "+String.valueOf(socket)+"\n";
                            readData(socket);
                        } catch (Exception e) {continue;}
                    }
                }
            });
            hiloServer.start();
        } catch (Exception e) {
            log+=DateFormat.getDateTimeInstance().format(new Date())+"-> "+"Error "+e.getMessage()+"\n";
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
                        log+=DateFormat.getDateTimeInstance().format(new Date())+"-> "+"Recibido: " + mensaje+"\n";
                        if (mensaje!=null){
                            JsonParser parser = new JsonParser();
                            JsonObject mensajeCODE = parser.parse(mensaje).getAsJsonObject();
                            String remitente = mensajeCODE.get("remitente").getAsString();
                            if (remitente.equals("cliente")) {
                                readClient(sock, mensajeCODE);
                            } else if (remitente.equals("nodo")) {
                                readNode(sock,mensajeCODE);
                            }
                        }
                    }
                } catch (IOException io) {log+=DateFormat.getDateTimeInstance().format(new Date())+"-> "+"Error "+io.getMessage()+"\n";}
                  catch (InterruptedException ie) {log+=DateFormat.getDateTimeInstance().format(new Date())+"-> "+"Error "+ie.getMessage()+"\n";}
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
                        log+=DateFormat.getDateTimeInstance().format(new Date())+"-> "+"Enviado: "+dato+"\n";
                    }
                }catch(Exception ex){log+=DateFormat.getDateTimeInstance().format(new Date())+"-> "+"Error "+ex.getMessage()+"\n";}
            }
        });
        escribir_hilo.start();
    }

    public void close(Socket sock){
        try {
            sock.close();
        } catch (IOException ioe) {ioe.printStackTrace();}
    }

    private void AgregarSocket(Socket socket1) {
        boolean result = false;
        if (this.listaSockets != null) {
            for (int s = 0; s < this.listaSockets.size(); s++) {
                if (this.listaSockets.get(s).equals(socket1)) {
                    result = true;
                    break;
                }
            }
            if (!result) {
                this.listaSockets.add(socket1);
            }
        }
        else {
            this.listaSockets.add(socket1);
        }
    }

    public void readClient(Socket sock, JsonObject mensajeCODE) throws InterruptedException, IOException {
        JsonObject respuestaJSON=new JsonObject();
        Decoder decodicador=new Decoder(mensajeCODE,"cliente");
        respuestaJSON.addProperty("remitente","server");
        Token genTok=new Token();
        int funcion=decodicador.Decode();
        switch (funcion){
            case 0:{//Token
                this.socketCliente=sock;
                String token=genTok.genToken();
                listTokens.add(token);
                respuestaJSON.addProperty("token",token);
                writeData(socket,respuestaJSON.toString());
                break;
            }
            case 1:{//xMalloc
                String token=mensajeCODE.get("token").getAsString();
                int verificador=genTok.verifyToken(listTokens,token);
                if(verificador==0){
                    String uuid = UUID.randomUUID().toString(); //genero el UUID para el espacio de memoria
                    int bytes=mensajeCODE.get("bytes").getAsInt();
                    int type=mensajeCODE.get("type").getAsInt();
                    Object[] array=listNodes.nodesBytesAvailable(bytes,uuid);
                    if (array!=null){
                        for(int i=0;i<array.length;i+=2){
                            JsonObject mensajeNode=new JsonObject();
                            mensajeNode.addProperty("funcion","xMalloc");
                            mensajeNode.addProperty("bytes",(int) array[i]);
                            mensajeNode.addProperty("UUID",uuid);
                            mensajeNode.addProperty("type",type);
                            writeData(((Node) array[i+1]).master.socket,mensajeNode.toString());
                        }
                    }
                    else{
                        genError(mensajeCODE,sock,3);
                    }
                    //busco el espacio disponible
                    //si no hay devolver un error numero 3
                }
                else{
                    genError(mensajeCODE,sock,verificador);
                }
                break;
            }
            case 2:{//desreferencia
                String token=mensajeCODE.get("token").getAsString();
                int verificador=genTok.verifyToken(listTokens,token);
                if(verificador==0) {
                    String uuid = mensajeCODE.get("UUID").getAsString();
                    String value = mensajeCODE.get("value").getAsString();
                    Socket tempSock = listNodes.ownerOfUUID(uuid).getSocket();
                    respuestaJSON.addProperty("funcion", "asignar");
                    respuestaJSON.addProperty("value", value);
                    writeData(tempSock, respuestaJSON.toString());
                }
                else{
                    genError(mensajeCODE,sock,verificador);
                }
                break;
            }
            case 3:{//asignar
                String token=mensajeCODE.get("token").getAsString();
                int verificador=genTok.verifyToken(listTokens,token);
                if(verificador==0){
                   //Hace lo de asignar un valor a un espacio de memoria referenciado por un UUID
                }
                else {
                    genError(mensajeCODE,sock,verificador);
                }
            }
        }
    }

    public void readNode(Socket sock, JsonObject mensajeCODE ){
        JsonObject respuestaJSON=new JsonObject();
        respuestaJSON.addProperty("remitente","server");
        Decoder decodificador=new Decoder(mensajeCODE,"nodo");
        int funcion=decodificador.Decode();
        switch(funcion){
            case 0:{ //addNode
                int number=mensajeCODE.get("numero").getAsInt();
                int bytes=mensajeCODE.get("bytes").getAsInt();
                boolean master=mensajeCODE.get("master").getAsBoolean();
                NodeMem newNode=new NodeMem(bytes,number,sock);
                listNodes.addMem(newNode);
                respuestaJSON.addProperty("funcion","aceptado");
                writeData(sock,respuestaJSON.toString());
                break;
            }
            case 1:{ //desreferencia (devuelvo el valor)
                String value=mensajeCODE.get("value").getAsString();
                mensajeCODE.addProperty("funcion","desreferencia");
                mensajeCODE.addProperty("value",value);
                writeData(socketCliente,mensajeCODE.toString()); //Se lo envio al cliente
                break;
            }
        }
    }

    public String getLog(){return this.log;}
    public void genError(JsonObject respuestaJSON, Socket sock, int error){
        respuestaJSON.addProperty("funcion","error");
        respuestaJSON.addProperty("error",error);
        writeData(sock,respuestaJSON.toString());
    }
}