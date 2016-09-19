package com.ce2103.itcr.meshmemory.memoryblocks;

import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import com.ce2103.itcr.meshmemory.server.NodeSocket;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.UUID;

/**
 * Created by estape11 on 15/09/16.
 */

public class Node {
    private int empyMem;
    private int totalMem;
    private int usedMem;
    private int numTel;
    private DoubleLinkedList memList;
    private boolean master;
    public Node(int totalMem,int numTel){
        this.totalMem=totalMem;
        this.empyMem=totalMem;
        this.usedMem=0;
        this.numTel=numTel;
        memList=new DoubleLinkedList();
        this.master=true;
    }

    public JsonObject findData(String UUID){ //Devuelve el indice en donde esta el dato buscado para castearlo
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        return value;
    }

    private int findIndex(String uuid){
        int value=0;
        for(int i=0;i<memList.size();i++){
            JsonObject temp=(JsonObject) memList.get(i);
            if (uuid.equals(temp.get("UUID").toString())){
                value=i;
                break;
            }
            else{
                continue;
            }
        }
        return value;

    }

    public String allocMem(int type,int size){// aparta la memoria para usarla despues
        JsonObject memBlock=new JsonObject();
        switch (type){
            case 0: {
                memBlock.addProperty("type", "int");
            }
            case 1: {
                memBlock.addProperty("type","long");
            }
            case 2: {
                memBlock.addProperty("type","float");
            }
            case 3: {
                memBlock.addProperty("type","string");
            }
            case 4: {
                memBlock.addProperty("type","char");
            }
        }
        String uuid = UUID.randomUUID().toString();
        memBlock.addProperty("size",size);
        memBlock.addProperty("UUID", uuid);
        memBlock.addProperty("value","");
        this.memList.add(memBlock);
        this.empyMem-=size;
        this.usedMem+=size;
        return uuid;
    }

    public void assignData(String UUID, String pvalue){
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        value.remove("value");
        value.addProperty("value",pvalue);
        memList.swapData(findIndex(UUID),value);
    }

    public void freeData(String UUID){
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        int tofree=value.get("size").getAsInt();
        memList.remove(findIndex(UUID));
        this.usedMem-=tofree;
        this.empyMem+=tofree;
    }

    public void getData(String UUID){
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        String type=value.get("type").getAsString();
        switch (type){
            case "int": {
                getAsInt(UUID);
            }
            case "long": {
                getAsLong(UUID);
            }
            case "float": {
                getAsFloat(UUID);
            }
            case "string": {
                getAsString(UUID);
            }
            case "char": {
                getAsChar(UUID);
            }

        }
    }

    public int getAsInt(String UUID){
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        int type=value.get("type").getAsInt();
        return type;
    }
    public long getAsLong(String UUID){
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        long type=value.get("type").getAsLong();
        return type;
    }
    public float getAsFloat(String UUID){
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        float type=value.get("type").getAsFloat();
        return type;
    }
    public String getAsString(String UUID){
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        String type=value.get("type").getAsString();
        return type;
    }
    public char getAsChar(String UUID){
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        char type=value.get("type").getAsCharacter();
        return type;
    }


    ////////////////////GETTERS/////////////////////
    public int getEmpyMem() {return empyMem;}
    public int getNumTel() {
        return numTel;
    }
    public int getTotalMem() {
        return totalMem;
    }
    public boolean isMaster() {
        return master;
    }
    public int getUsedMem() {return usedMem;}
}
