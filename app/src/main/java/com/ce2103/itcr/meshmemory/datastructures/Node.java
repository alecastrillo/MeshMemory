package com.ce2103.itcr.meshmemory.datastructures;

/**
 * Created by alecm on 17/09/16.
 * Class for the DoublyLinkedList
 */

public class Node {
    public NodeMem master;
    public NodeMem Slave;
    public Node next;
    public Node prev;
    public int bytes;
    public int bytesAvailable;
    public String[] memoryBlock;

    public Node(NodeMem newMem){
        this.master = newMem;
        this.Slave = null;
        this.prev = null;
        this.next = null;
        this.bytes = newMem.bytesAvailable;
        this.bytesAvailable = newMem.bytesAvailable;
        this.memoryBlock = new String[bytes];
        for(int i=0; i<bytes; i++){
            this.memoryBlock[i]="Available";
        }
         
    }

    /**
     * Search by in the memory blocks of the node, the bytes associated
     * @param UUID in string
     * @return int
     */
    int bytesWithUUID(String UUID){
        int Bytes=0;
        for(int i=0; i<bytes; i++) {
            if(memoryBlock[i]==UUID){
                Bytes++;
            }
        }
        return Bytes;
    }

    /**
     * Serarch if the node contains the specific UUID in their memory block
     * @param UUID
     * @return boolean
     */
    boolean ownerUUID(String UUID){
        for(int i=0; i<this.bytes; i++){
            if (memoryBlock[i].equals(UUID)){
                return true;
            }
        }
        return false;
    }

    void deleteUUID(String UUID){
        for(int i=0; i<this.bytes; i++){
            if(memoryBlock[i].equals(UUID)){
                memoryBlock[i]="Available";
                bytesAvailable--;
            }
        }
    }

    int firstByteAvailable(){
        for(int i=0; i<bytes;i++){
            if(memoryBlock[i].equals("Available")){
                return i;
            }
        }return -1;
    }
    void burping(int pByte, int available){
        if(pByte==bytes){
            return;
        }else if(memoryBlock[pByte-1].equals("Available")){
            available++;
            pByte++;
            burping(pByte, available);
        }else if (available>=1){
            int index = firstByteAvailable();
            memoryBlock[index]=memoryBlock[pByte-1];
            memoryBlock[pByte]="Available";
            available++;
            pByte++;
            burping(pByte, available);
        }else{
            pByte++;
            burping(pByte, available);
        }
    }

    /**
     * Gets the total amount of bytes associated to the node
     * @return int
     */
    public int getTotalBytes(){
        return bytes;
    }

    /**
     * Gets the available bytes in the node
     * @return int
     */
    public int getBytesAvailable(){
        return this.master.bytesAvailable;
    }

    /**
     * Gets the used amount of bytes
     * @return int
     */
    public int getBytesOccupied(){
        return this.master.totalBytes - this.master.bytesAvailable;
    }

    /**
     * Gets the whole array of the memory block
     * @return array of strings
     */
    public String[] getBytesArray(){
        return this.memoryBlock;
    }


}
