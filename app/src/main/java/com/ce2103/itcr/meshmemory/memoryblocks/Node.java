package com.ce2103.itcr.meshmemory.memoryblocks;

import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import com.google.gson.JsonObject;

/**
 * Created by estape11 on 15/09/16.
 */

public class Node {
    private int freeMem;
    private int totalMem;
    private int usedMem;
    private int numTel;
    private DoubleLinkedList memList;
    private boolean master;

    public Node(int totalMem,int numTel){
        this.totalMem=totalMem;
        this.freeMem =totalMem;
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

    public void allocMem(int type,int bytes, String uuid){// aparta la memoria para usarla despues
        JsonObject memBlock=new JsonObject();
        memBlock.addProperty("type",type);
        memBlock.addProperty("bytes",bytes);
        memBlock.addProperty("UUID", uuid);
        this.memList.add(memBlock);
        this.freeMem -=bytes;
        this.usedMem+=bytes;
    }

    public void assignData(String UUID, String pvalue, int index, boolean fin){
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        value.addProperty("value",pvalue);
        value.addProperty("index",index);
        value.addProperty("final",fin);
        memList.swapData(findIndex(UUID),value);
    }

    public void freeData(String UUID){
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        int tofree=value.get("size").getAsInt();
        memList.remove(findIndex(UUID));
        this.usedMem-=tofree;
        this.freeMem +=tofree;
    }

    public String getData(String UUID) {
        JsonObject temp;
        temp = (JsonObject) memList.get(findIndex(UUID));
        String value = temp.get("value").getAsString();
        //Podemos hacer un Json con el valor, el indice y si es la ultima parte
        return value;
    }
    public int getIndex(String UUID){
        JsonObject temp;
        temp = (JsonObject) memList.get(findIndex(UUID));
        int value = temp.get("index").getAsCharacter();
        //Podemos hacer un Json con el valor, el indice y si es la ultima parte
        return value;
    }
    public boolean getFin(String UUID){
        JsonObject temp;
        temp = (JsonObject) memList.get(findIndex(UUID));
        boolean value = temp.get("final").getAsBoolean();
        //Podemos hacer un Json con el valor, el indice y si es la ultima parte
        return value;
    }

        /*
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
    public int getFreeMem() {return freeMem;}
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
    */
}
