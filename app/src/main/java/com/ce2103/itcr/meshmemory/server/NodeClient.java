package com.ce2103.itcr.meshmemory.server;

import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import com.ce2103.itcr.meshmemory.gui.Datos_nodo;
import com.ce2103.itcr.meshmemory.gui.Nodo;
import com.ce2103.itcr.meshmemory.memoryblocks.Node;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

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
    private Node nodo;
    private String log=""; //log de los procesos

    /**
     * Constructor
     */
    public NodeClient() {
        this.socket = null;
        this.entrada = null;
        this.salida = null;
        this.hiloCliente = null;
        this.nodo=null;
    }

    /**
     * Initialize the client with the port and IP of the Manager
     * @param host
     * @param puerto
     * @param bytes
     * @param number
     */
    public void startClient(final String host, final int puerto, final int bytes, final int number) {
        this.host=host;
        this.puerto=puerto;
        this.hiloCliente=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket==null) {
                        log+= DateFormat.getDateTimeInstance().format(new Date())+
                                "-> Conectado al manager"+"\n";
                        socket = new Socket(host, puerto);
                        System.out.println("Conectado a: " + socket.toString());
                        readData(socket);
                        JsonObject output=new JsonObject();
                        output.addProperty("remitente","nodo");
                        output.addProperty("funcion","addNode");
                        output.addProperty("numero", number);
                        output.addProperty("bytes",bytes);
                        output.addProperty("master",true);
                        writeData(output.toString());
                    }
                } catch (Exception ue) {
                    log+= DateFormat.getDateTimeInstance().format(new Date())+"-> EXCEPTION: "+
                            ue.getMessage()+"\n";
                }
            }
        });
        this.hiloCliente.start();
        setNodo(bytes,number);
    }

    public void setNodo(int bytes, int number){
        this.nodo=new Node(bytes,number);
        log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Datos del Nodo: numero "+number+
                ", Bytes "+bytes+"\n";
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
                            log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Recibido: "+
                                    mensaje+"\n";
                            JsonParser parser = new JsonParser();
                            JsonObject mensajeCODE = parser.parse(mensaje).getAsJsonObject();
                            String remitente=mensajeCODE.get("remitente").getAsString();
                            if(remitente.equals("server")){
                                readServer(mensajeCODE);
                            }
                        }
                    }
                } catch (Exception e) {
                    log+= DateFormat.getDateTimeInstance().format(new Date())+"-> EXCEPTION: "+
                            e.getMessage()+"\n";
                    e.printStackTrace();
                }
            }
        });
        leer_hilo.start();
    }

    /**
     * Send data to the manager
     * @param dato
     */
    public void writeData(final String dato){
        try{
            if(dato!=null){
                this.salida = new PrintWriter(socket.getOutputStream(),true);
                this.salida.flush();
                this.salida.println(dato);
                this.salida.flush();
                System.out.println("Enviado: "+dato);
                log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Enviado: "+dato+"\n";
            }
        }catch(Exception ex){
            log+= DateFormat.getDateTimeInstance().format(new Date())+"-> EXCEPTION: "+
                    ex.getMessage()+"\n";
            ex.printStackTrace();
        }
    }

    /**
     * Close the connection between the manager
     */
    public void close() {
        try {
            socket.close();
        } catch (Exception ioe) {
            log+= DateFormat.getDateTimeInstance().format(new Date())+"-> EXCEPTION: "+ioe.getMessage()+"\n";
        }
    }

    /**
     * Decode the message received from the manager and makes the funtion
     * @param mensajeCODE
     */
    public void readServer(JsonObject mensajeCODE) throws InterruptedException {
        JsonObject respuestaJSON = new JsonObject();
        respuestaJSON.addProperty("remitente","nodo");
        Decoder decodificador=new Decoder(mensajeCODE,"server");
        int funcion=decodificador.Decode();
        switch (funcion){
            case 0:{//xMalloc
                log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Funcion xMalloc: En proceso"+"\n";
                int bytes=mensajeCODE.get("bytes").getAsInt();
                int type=mensajeCODE.get("type").getAsInt();
                String uuid=mensajeCODE.get("UUID").getAsString();
                nodo.allocMem(type,bytes,uuid);
                log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Funcion xMalloc: UUID "+uuid+", "
                        +"Bytes "+bytes+", Tipo "+type+"\n";
                log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Funcion xMalloc: Completado"+"\n";
                break;
            }
            case 1:{//desreferencia
                log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Funcion desreferencia: En proceso"
                        +"\n";
                JsonObject[] barray= nodo.getBytesArray();
                String uuid=mensajeCODE.get("UUID").getAsString();
                respuestaJSON.addProperty("funcion","desreferencia");
                /**
                String valueOut[]=new String[10];
                int index=100;
                Arrays.fill(valueOut,"");
                for (int i=0;i<barray.length;i++){
                    if(!barray[i].get("NULL").getAsBoolean()) {
                        if (barray[i].get("UUID").getAsString().equals(uuid)) {
                            valueOut[barray[i].get("index").getAsInt()]=barray[i].get("value").getAsString();
                            if(barray[i].get("index").getAsInt()<=index){
                                index=barray[i].get("index").getAsInt(); //Para asegurarme de que sea el menor indice
                            }
                        }
                    }
                }
                String value="";
                for (int i=0;i<valueOut.length;i++){
                    value+=valueOut[i];
                }
                respuestaJSON.addProperty("funcion", "desreferencia");
                respuestaJSON.addProperty("value", value);
                respuestaJSON.addProperty("index", index);
                respuestaJSON.addProperty("final", false);
                log += DateFormat.getDateTimeInstance().format(new Date()) + "-> Funcion desreferencia: UUID " +
                        uuid + "," + "Index " + index + ", Fin " + false + "\n";
                */
                //PRUEBA A FUERZA BRUTA

                for (int i=0;i<barray.length;i++) {
                    if (!barray[i].get("NULL").getAsBoolean()) {
                        if (barray[i].get("UUID").getAsString().equals(uuid)) {
                            respuestaJSON.addProperty("value",barray[i].get("value").getAsString());
                            respuestaJSON.addProperty("index",barray[i].get("index").getAsInt());
                            respuestaJSON.addProperty("UUID",uuid);
                            writeData(respuestaJSON.toString());
                        }
                    }
                }

                log += DateFormat.getDateTimeInstance().format(new Date()) + "-> Funcion desreferencia: completado";
                break;
            }
            case 2:{//asignar
                log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Funcion asignar: En proceso"+"\n";
                String uuid=mensajeCODE.get("UUID").getAsString();
                String value=mensajeCODE.get("value").getAsString();
                int index=mensajeCODE.get("index").getAsInt();
                boolean fin=mensajeCODE.get("final").getAsBoolean();
                log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Funcion asignar: UUID "+uuid+
                        ", Dato "+value+", Index "+index+", Fin "+fin+"\n";
                nodo.assignData(uuid,value,index,fin); //Este metodo crea un nuevo bloque de memoria
                log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Funcion asignar: Completado"+"\n";
                break;
            }
            case 3: {//xFree
                log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Funcion xFree: En proceso"+"\n";
                String uuid=mensajeCODE.get("UUID").getAsString();
                log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Funcion xFree: UUID "+uuid+"\n";
                nodo.freeData(uuid);
                log+= DateFormat.getDateTimeInstance().format(new Date())+"-> Funcion xFree: Completado"+"\n";
                break;
            }
        }
    }
    public String getLog(){
        return this.log;
    }

    public String[] getBytesArray(){

        JsonObject[] bytes=nodo.getBytesArray();
        String[] out=new String[bytes.length];
        for(int i=0;i<bytes.length;i++){
            if(bytes[i].get("NULL").getAsBoolean()){
                out[i]="Available";
            }
            else{
                out[i]=bytes[i].get("UUID").getAsString();
            }
        }
        return out;
    }

}