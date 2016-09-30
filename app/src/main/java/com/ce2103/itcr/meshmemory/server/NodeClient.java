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
 * Client class for the Node
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

    /**
     * Constructor
     */
    public NodeClient() {
        this.socket = null;
        this.entrada = null;
        this.salida = null;
        this.hiloCliente = null;
        this.listaSockets = new DoubleLinkedList();
        this.nodo=null;
    }

    /**
     * Initialize the client with the port and IP of the Manager
     * @param host
     * @param puerto
     */
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

    /**
     * Thread to read data from the manager
     * @param sock
     */
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

    /**
     * Send data to the manager
     * @param dato
     */
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

    /**
     * Close the connection between the manager
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException ioe) {}
    }

    /**
     * Adds the new socket connection into the sockets list
     * @param socket1
     */
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

    /**
     * Decode the message received from the manager and makes the funtion
     * @param mensajeCODE
     */
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
                String value=nodo.getData(uuid); //Obtengo el pedazo de valor
                int index=nodo.getIndex(uuid); //Obtengo el indice del pedazo que posee el nodo
                boolean fin=nodo.getFin(uuid); //Obtengo un booleano para saber si es el ultimo pedazo
                respuestaJSON.addProperty("funcion","desreferencia");
                respuestaJSON.addProperty("value",value);
                respuestaJSON.addProperty("index",index);
                respuestaJSON.addProperty("final",fin);
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
    }
}