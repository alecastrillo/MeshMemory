package com.ce2103.itcr.meshmemory.server;

import com.google.gson.JsonObject;

import java.net.Socket;

/**
 * This class decode the incoming message received by the client, node or server
 * Created by estape11 on 17/09/16.
 */
public class Decoder {
    private JsonObject msgCODE;
    private String sender;

    /**
     * Constructor, set the attributes
     * @param mensajeCODE
     * @param sender
     */
    public Decoder(JsonObject mensajeCODE, String sender){
        this.msgCODE=mensajeCODE;
        this.sender=sender;
    }

    /**
     * General method to decode the message
     * @return
     */
    public int Decode(){
        int value =-2;
        if (this.sender.equals("cliente")){
            value= decodeClient();
        }
        else if(sender.equals("nodo")){
            value= decodeNode();
        }
        else if(sender.equals("server")) {
            value = decodeServer();
        }
        return value;
    }

    /**
     * This decode is special for the messages sent by the client
     * @return number of function to do
     */
    private int decodeClient(){
        String funcion=msgCODE.get("funcion").getAsString();
        int value=-2;
        if (funcion.equals("token")){
            value=0;
        }
        else if(funcion.equals("xMalloc")){
            value=1;
        }
        else if(funcion.equals("desreferencia")) {
            value=2;
        }
        else if(funcion.equals("asignar")){
            value=3;
        }
        else if(funcion.equals("xFree")){
            value=4;
        }
        return value;
    }

    /**
     * This decode is special for the messages sent by the Server
     * @return number of function to do
     */
    private int decodeServer(){
        String funcion=msgCODE.get("funcion").getAsString();
        int value=-2;
        if(funcion.equals("xMalloc")){
            value=0;
        }
        else if(funcion.equals("desreferencia")) {
            value=1;
        }
        else if(funcion.equals("asignar")){
            value=2;
        }
        else if(funcion.equals("xFree")){
            value=3;
        }
        return value;
    }

    /**
     * This decode is special for the messages sent by the Node
     * @return number of function to do
     */
    private int decodeNode(){
        String funcion=msgCODE.get("funcion").getAsString();
        int value=-2;
        if(funcion.equals("addNode")){
            value=0;
        }
        else if(funcion.equals("desreferencia")){
            value=1;
        }
        return value;
    }

}
