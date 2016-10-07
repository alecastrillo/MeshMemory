package com.ce2103.itcr.meshmemory.memoryblocks;

import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import com.google.gson.JsonObject;

import java.util.Arrays;

/**
 * This node class is used for the NodeClient
 * Created by estape11 on 15/09/16.
 *
 */

public class Node {
    private int freeMem;
    private int totalMem;
    private int usedMem;
    private int numTel;
    private DoubleLinkedList memList;
    private boolean master;
    private JsonObject bytesArray[];

    public Node(int totalMem,int numTel){
        this.totalMem=totalMem;
        this.freeMem =totalMem;
        this.usedMem=0;
        this.numTel=numTel;
        memList=new DoubleLinkedList();
        this.master=true;
        bytesArray = new JsonObject[totalMem];
        for(int i=0;i<bytesArray.length;i++) {
            JsonObject NULL = new JsonObject();
            NULL.addProperty("NULL", true);
            bytesArray[i]=NULL;
        }
    }

    /**
     * Find the data stored in the node (JSON) by the UUID
     * @param UUID
     * @return
     */
    public JsonObject findData(String UUID){ //Devuelve el indice en donde esta el dato buscado para castearlo
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        return value;
    }

    /**
     * Search the index of the memory linked by UUID in the node
     * @param uuid
     * @return int of the index
     */
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

    /**
     * Store the memory block with UUID, bytes and the type to store
     * @param type (number)
     * @param bytes (total memory to allocate)
     * @param uuid (the tag of reference)
     */
    public void allocMem(int type,int bytes, String uuid){// aparta la memoria para usarla despues
        int bytesU=0;
        JsonObject temp=new JsonObject();
        temp.addProperty("type", type);
        temp.addProperty("bytes", bytes);
        temp.addProperty("UUID", uuid);
        temp.remove("NULL");
        temp.addProperty("NULL", false);
        temp.addProperty("value", "");
        for (int i=0;i<bytesArray.length;i++){
            if(bytes==bytesU){
                break;
            }
            else {
                if(bytesArray[i].get("NULL").getAsBoolean()){
                    bytesU++;
                    bytesArray[i].addProperty("type", type);
                    bytesArray[i].addProperty("bytes", bytes);
                    bytesArray[i].addProperty("UUID", uuid);
                    bytesArray[i].remove("NULL");
                    bytesArray[i].addProperty("NULL", false);
                    bytesArray[i].addProperty("value", "");
                }
            }
        }
        System.out.println(bytesU);
        this.freeMem -=bytes;
        this.usedMem+=bytes;
    }

    /**
     * Assign in the memory block the data to store
     * @param UUID (reference to the momory block)
     * @param pvalue (the value to store)
     * @param index (the index o the value)
     * @param fin (the value to know if the value was the last)
     */
    public void assignData(String UUID, String pvalue, int index, boolean fin){
        /**
        JsonObject tempMem;
        tempMem=(JsonObject) memList.get(findIndex(UUID));
        tempMem.addProperty("value",pvalue);
        tempMem.addProperty("index",index);
        tempMem.addProperty("final",fin);
        memList.swapData(findIndex(UUID),tempMem);*/
        for(int i=0;i<bytesArray.length;i++){
            if (bytesArray[i].get("UUID").getAsString().equals(UUID)){
                if (bytesArray[i].get("value").getAsString().equals("")){
                    bytesArray[i].remove("value");
                    bytesArray[i].addProperty("value",pvalue);
                    bytesArray[i].addProperty("index",index);
                    bytesArray[i].addProperty("final",fin);
                    break;
                }
                else{
                    continue;
                }
            }
            else{
                continue;
            }
        }
    }

    /**
     * Release the memory linked by specific UUID
     * @param UUID (reference to the memory block)
     */
    public void freeData(String UUID){
        /**
        JsonObject value;
        value=(JsonObject) memList.get(findIndex(UUID));
        int tofree=value.get("size").getAsInt();
        memList.remove(findIndex(UUID));*/

        bytesArray=new JsonObject[totalMem];
        int tofree=0;
        for(int i=0;i<bytesArray.length;i++){
            if(!bytesArray[i].get("NULL").getAsBoolean()) {
                if (bytesArray[i].get("UUID").getAsString().equals(UUID)) {
                    JsonObject NULL=new JsonObject();
                    NULL.addProperty("NULL",true);
                    bytesArray[i]=NULL;
                    tofree++;
                }
                else{
                    continue;
                }
            }
            else{
                continue;
            }
        }
        this.usedMem-=tofree;
        this.freeMem +=tofree;
    }

    /**
     * Return the value stored by UUID
     * @param UUID (reference to the memory block)
     * @return string (return the memory value)
     */
    public String getData(String UUID) {
        /**
        JsonObject temp;
        temp = (JsonObject) memList.get(findIndex(UUID));
        String value = temp.get("value").getAsString();*/

        String value="";
        for(int i=0;i<bytesArray.length;i++){
            if(bytesArray[i].get("UUID").getAsString().equals(UUID)){
                value=bytesArray[i].get("value").getAsString();
                break;
            }
            else{
                continue;
            }
        }
        return value;
    }

    /**
     * Gets the index of the specific memory block by UUID
     * @param UUID (reference)
     * @return int of index
     */
    public int getIndex(String UUID){
        int temp=-1;
        for(int i=0;i<bytesArray.length;i++){
            if(bytesArray[i].get("UUID").getAsString().equals(UUID)){
                temp= bytesArray[i].get("index").getAsInt();
            }
            else{
                continue;
            }
        }
        return temp;
    }

    /**
     * Gets the boolean if it's the last value part
     * @param UUID (reference)
     * @return boolean
     */
    public boolean getFin(String UUID){
        boolean temp=false;
        for(int i=0;i<bytesArray.length;i++){
            if(bytesArray[i].get("UUID").getAsString().equals(UUID)){
                temp= bytesArray[i].get("final").getAsBoolean();
            }
            else{
                continue;
            }
        }
        return temp;
    }

    public void burping(){

    }

    ////////////////////GETTERS/////////////////////
    public int getFreeMem() {return freeMem;}
    public int getNumTel() {return numTel;}
    public int getTotalMem() {return totalMem;}
    public boolean isMaster() {return master;}
    public int getUsedMem() {return usedMem;}
    public JsonObject[] getBytesArray(){return  bytesArray;}
}
