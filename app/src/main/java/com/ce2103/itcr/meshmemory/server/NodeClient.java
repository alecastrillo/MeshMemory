package com.ce2103.itcr.meshmemory.server;

import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import com.ce2103.itcr.meshmemory.gui.Nodo;
import com.ce2103.itcr.meshmemory.memoryblocks.Node;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by estape11 on 17/09/16.
 */

public class NodeClient extends Thread {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private int puerto;
    private String host;
    private Thread hiloCliente;
    private DoubleLinkedList listaSockets;
    private Node nodo;

    public NodeClient() {
        this.socket = null;
        this.entrada = null;
        this.salida = null;
        this.hiloCliente = null;
        this.listaSockets = new DoubleLinkedList();
        this.nodo=null;
    }

    public void startClient(final String host, final int puerto) {
        this.host=host;
        this.puerto=puerto;
        this.hiloCliente=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket==null) {
                        socket = new Socket(host, puerto);
                        System.out.println("Conectado a: " + socket.toString());
                        AgregarSocket(socket);
                        readData(socket);
                    }
                } catch (UnknownHostException ue) {} catch (IOException ie) {}
            }
        });
        this.hiloCliente.start();
    }

    public void setNodo(int bytes, int number){
        this.nodo=new Node(bytes,number);
    }

    public void readData(final Socket sock){
        Thread leer_hilo=new Thread(new Runnable(){
            public void run(){
                try{
                    entrada =new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    while(true){
                        String mensaje= entrada.readLine();
                        if (mensaje!=null) {
                            System.out.println("Recibido: " + mensaje);
                            JsonParser parser = new JsonParser();
                            JsonObject mensajeCODE = parser.parse(mensaje).getAsJsonObject();
                            String remitente=mensajeCODE.get("remitente").getAsString();
                            if(remitente.equals("server")){
                                readServer(mensajeCODE);
                            }
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
                    System.out.println("ERROR");
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

    public void readServer(JsonObject mensajeCODE){
        JsonObject respuestaJSON=new JsonObject();
        respuestaJSON.addProperty("remitente","nodo");
        Decoder decodificador=new Decoder(mensajeCODE,"server");
        int funcion=decodificador.Decode();
        switch (funcion){
            case 0:{//xMalloc
                int bytes=mensajeCODE.get("bytes").getAsInt();
                int type=mensajeCODE.get("type").getAsInt();
                String uuid=mensajeCODE.get("UUID").getAsString();
                nodo.allocMem(type,bytes,uuid);
                break;
            }
            case 1:{//desreferencia
                String uuid=mensajeCODE.get("UUID").getAsString();
                String value=nodo.getData(uuid);
                respuestaJSON.addProperty("funcion","desreferencia");
                respuestaJSON.addProperty("value",value);
                writeData(respuestaJSON.toString());
                break;
            }
            case 2:{//asignar
                String uuid=mensajeCODE.get("UUID").getAsString();
                String value=mensajeCODE.get("value").getAsString();
                int index=mensajeCODE.get("index").getAsInt();
                boolean fin=mensajeCODE.get("final").getAsBoolean();
                nodo.assignData(uuid,value,index,fin); //Este metodo crea un nuevo bloque de memoria
                break;
            }
        }

        /*
        if(funcion.equals("alloc")){
            int tipo=mensajeCODE.getAsJsonObject().get("type").getAsInt();
            int bytes=mensajeCODE.getAsJsonObject().get("bytes").getAsInt();
            String UUID=Datos_nodo.node.allocMem(tipo,bytes);
            JsonObject output=new JsonObject();
            output.addProperty("UUID",UUID);
            writeData(output.toString());
        }
        */
    }
}