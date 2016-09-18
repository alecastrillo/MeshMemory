package com.ce2103.itcr.meshmemory.server;

import com.google.gson.JsonObject;

import java.net.Socket;

/**
 * Created by estape11 on 17/09/16.
 */
public class Decoder {
    JsonObject msgCODE;
    String sender;
    public Decoder(JsonObject mensajeCODE, String sender){
        this.msgCODE=mensajeCODE;
        this.sender=sender;
    }
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
    private int decodeClient(){
        String funcion=msgCODE.get("funcion").getAsString();
        int value=-2;
        if (funcion.equals("token")){
            value=0;
        }
        else if(funcion.equals("xMalloc")){
            value=1;
        }
        return value;
    }

    private int decodeServer(){
        String funcion=msgCODE.get("funcion").getAsString();
        int value=-2;
        if(funcion.equals("xMalloc")){
            value=0;
        }
        return value;
    }

    private int decodeNode(){
        String funcion=msgCODE.get("funcion").getAsString();
        int value=-2;
        if(funcion.equals("addNode")){
            value=0;
        }
        return value;
    }
}
