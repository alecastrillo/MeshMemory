package com.ce2103.itcr.meshmemory.server;
//Imports
import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import com.ce2103.itcr.meshmemory.datastructures.DoublyLinkedList;
import com.ce2103.itcr.meshmemory.datastructures.Node;
import com.ce2103.itcr.meshmemory.datastructures.NodeMem;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * This class was the Manager of the server, manage the memory
 * Created by estape11 on 09/09/16.
 */
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
    //private String metodo="funcion"; unused variable
    private String log; //log de los procesos
    private String value[]; //almacena el dato por desreferenciar

    /**
     * Constructor
     */
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
        this.value=new String[10];
    }

    /**
     * Initialize the server by the listener port number
     * @param puerto
     */
    public void startServer(int puerto) {
        this.puerto=puerto;
        try {
            servidor = new ServerSocket(puerto);
            System.out.println("Servidor iniciado");
            log+=DateFormat.getDateTimeInstance().format(new Date())+"-> Servidor iniciado...";
            this.hiloServer = new Thread(new Runnable() {
                public void run() {
                    while (true){
                        try {
                            socket = servidor.accept();
                            AgregarSocket(socket);
                            System.out.println("Nuevo cliente conectado: "+String.valueOf(socket));
                            log+=DateFormat.getDateTimeInstance().format(new Date())+"-> "+"Nuevo cliente conectado: "+
                                    String.valueOf(socket)+"\n";
                            readData(socket);
                        } catch (Exception e) {continue;}
                    }
                }
            });
            hiloServer.start();
        } catch (Exception e) {
            log+=DateFormat.getDateTimeInstance().format(new Date())+"-> "+"Error "+e.getMessage()+"\n";
            e.printStackTrace();
        }
    }

    /**
     * Thread to stay listening the connected clients
     * @param sock
     */
    public void readData(final Socket sock){
        Thread leer_hilo=new Thread(new Runnable(){
            public void run(){
                try{
                    entrada =new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    while(true){
                        String mensaje= entrada.readLine();
                        if (mensaje!=null){
                            log+=DateFormat.getDateTimeInstance().format(new Date())+"-> "+"Recibido: " + mensaje+"\n";
                            System.out.println("Recibido: " + mensaje);
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
                } catch (Exception io) {log+=DateFormat.getDateTimeInstance().
                        format(new Date())+"-> "+"Error "+io.getMessage()+"\n";
                    io.printStackTrace();
                }
                  //catch (InterruptedException ie) {log+=DateFormat.getDateTimeInstance().
                  //        format(new Date())+"-> "+"Error "+ie.getMessage()+"\n";}
            }
        });
        leer_hilo.start();
    }

    /**
     * Send a message to specific socket(client)
     * @param socket
     * @param dato
     */
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
                }catch(Exception ex){
                    log+=DateFormat.getDateTimeInstance().format(new Date())+
                            "-> "+"Error "+ex.getMessage()+"\n";
                    ex.printStackTrace();
                }
            }
        });
        escribir_hilo.start();
    }

    /**
     * Close the socket connection
     * @param sock
     */
    public void close(Socket sock){
        try {
            sock.close();
        } catch (IOException ioe) {
            log+=DateFormat.getDateTimeInstance().format(new Date())+
                    "-> "+"Error "+ioe.getMessage()+"\n";
            ioe.printStackTrace();}
    }

    /**
     * Adds the new sockets(clients) to the sockets list
     * @param socket1
     */
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

    /**
     * Decode and makes the function sent by client
     * @param sock
     * @param mensajeCODE
     * @throws InterruptedException
     * @throws IOException
     */
    public void readClient(Socket sock, JsonObject mensajeCODE) throws InterruptedException, IOException {
        JsonObject respuestaJSON=new JsonObject();
        respuestaJSON.addProperty("remitente","server");
        Decoder decodicador=new Decoder(mensajeCODE,"cliente");
        Token genTok=new Token();
        int funcion=decodicador.Decode();
        switch (funcion){
            case 0:{//Token
                this.socketCliente=sock;
                String token=genTok.genToken();
                listTokens.add(token);
                respuestaJSON.addProperty("funcion","token");
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

                    System.out.println("-Guardando UUID");
                    System.out.println(Arrays.toString(listNodes.head.getBytesArray()));

                    Object[] array=listNodes.nodesBytesAvailable(bytes,uuid);

                    System.out.println(Arrays.toString(listNodes.head.getBytesArray()));

                    if (array!=null){
                        for(int i=0;i<array.length;i+=2){//Distribuye la memoria
                            JsonObject mensajeNode=new JsonObject();
                            mensajeNode.addProperty("remitente","server");
                            mensajeNode.addProperty("funcion","xMalloc");
                            mensajeNode.addProperty("bytes",(int) array[i]);
                            mensajeNode.addProperty("UUID",uuid);
                            mensajeNode.addProperty("type",type);
                            writeData(((Node) array[i+1]).master.socket,mensajeNode.toString());
                        }
                        respuestaJSON.addProperty("UUID",uuid);
                        respuestaJSON.addProperty("funcion","UUID");
                        writeData(socketCliente,respuestaJSON.toString());
                    }
                    else{
                        genError(respuestaJSON,sock,3);
                    }
                }
                else{
                    genError(respuestaJSON,sock,verificador);
                }
                break;
            }
            case 2:{//desreferencia
                Arrays.fill(value,"");//Relleno el arreglo de strings vacios
                String token=mensajeCODE.get("token").getAsString();
                int verificador=genTok.verifyToken(listTokens,token);
                if(verificador==0) {
                    //Hace lo de unir los pedazos del valor almacenado en los nodos
                    String uuid = mensajeCODE.get("UUID").getAsString();
                    respuestaJSON.addProperty("funcion", "desreferencia");
                    //Les escribe a cada uno de los nodos pidiendoles el pedazo de nodo que tengan
                }
                else{
                    genError(respuestaJSON,sock,verificador);
                }
                break;
            }
            case 3:{//asignar
                System.out.println("hey1");
                String token=mensajeCODE.get("token").getAsString();
                int verificador=genTok.verifyToken(listTokens,token);
                if(verificador==0) {
                    System.out.println("hey2");
                    String uuid = mensajeCODE.get("UUID").getAsString();
                    String value = mensajeCODE.get("value").getAsString();
                    Object[] arrayNodes = listNodes.arrayOfNodesWithUUID(uuid);

                    System.out.println(Arrays.toString(listNodes.head.getBytesArray()));

                    if (arrayNodes != null) {
                        System.out.println("hey3");
                        System.out.println(arrayNodes.length);
                        int division = value.length()/arrayNodes.length;
                        System.out.println("hey4"); //AUN NO LLEGA ACA
                        System.out.println(division); //Deberia ser !=0
                        for (int i = 0; i < arrayNodes.length; i++) {
                            if (i == arrayNodes.length-1) { //le envio lo que queda del value
                                Socket tempSock = ((Node) arrayNodes[i]).master.socket;
                                respuestaJSON.addProperty("funcion", "asignar");
                                respuestaJSON.addProperty("value", value);
                                respuestaJSON.addProperty("final", true);
                                respuestaJSON.addProperty("index", i);
                                writeData(tempSock, respuestaJSON.toString());
                            } else {
                                Socket tempSock = ((Node) arrayNodes[i]).master.socket;
                                respuestaJSON.addProperty("funcion", "asignar");
                                respuestaJSON.addProperty("value", Utils.slice_end(value, division));
                                respuestaJSON.addProperty("final", false);
                                respuestaJSON.addProperty("index", i);
                                value = Utils.slice_start(value, division);//Recorto lo que me queda del value
                                writeData(tempSock, respuestaJSON.toString());
                            }
                        }
                    }
                }
                else {
                    genError(respuestaJSON,sock,verificador);
                }
            }
        }
    }

    /**
     * Decode and makes the function sent by node
     * @param sock
     * @param mensajeCODE
     */
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
            case 1:{ //desreferencia (recibo el valor que solicite)
                String dato=mensajeCODE.get("value").getAsString();
                int index=mensajeCODE.get("index").getAsInt();
                boolean fin=mensajeCODE.get("fin").getAsBoolean();
                value[index]=dato;
                if (fin){
                    String valor="";
                    for(int i=0;i<value.length;i++){
                        valor+=value[i];
                    }
                    respuestaJSON.addProperty("funcion","desreferencia");
                    respuestaJSON.addProperty("value",valor);
                    writeData(socketCliente,mensajeCODE.toString()); //Se lo envio al cliente
                }
                break;
            }
        }
    }

    public String getLog(){return this.log;}

    /**
     * Generate error and send to the client
     * @param respuestaJSON
     * @param sock
     * @param error
     */
    public void genError(JsonObject respuestaJSON, Socket sock, int error){
        System.out.println("Error #"+error);
        respuestaJSON.addProperty("funcion","error");
        respuestaJSON.addProperty("error",error);
        writeData(sock,respuestaJSON.toString());
    }
}