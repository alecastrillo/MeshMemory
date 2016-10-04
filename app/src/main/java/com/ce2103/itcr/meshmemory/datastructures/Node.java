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
        System.out.print("UUID: "+ UUID+". ");
        for(int i=0; i<this.bytes; i++){
            System.out.print("Memoryblock[i]: "+memoryBlock[i]+". ");
            if (memoryBlock[i]==UUID){
                return true;
            }
        }
        return false;
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
