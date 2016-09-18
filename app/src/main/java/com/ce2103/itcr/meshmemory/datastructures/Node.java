package com.ce2103.itcr.meshmemory.datastructures;

/**
 * Created by alecm on 17/09/16.
 */

public class Node {
    NodeMem master;
    NodeMem Slave;
    Node next;
    Node prev;
    int bytes;
    int bytesAvailable;
    String memoryBlock[];

    public Node(NodeMem newMem){
        this.master = newMem;
        this.Slave = null;
        this.prev = null;
        this.next = null;
        this.bytes = newMem.bytesAvailable;
        this.bytesAvailable = newMem.bytesAvailable;
        this.memoryBlock = new String[bytes];
        for(int i=0; i==bytes; i++){
            this.memoryBlock[i]="Available";
        }
    }

    public int getTotalBytes(){
        return bytes;
    }

    public int getBytesAvailable(){
        return this.master.bytesAvailable;
    }

    public int getBytesOccupied(){
        return this.master.totalBytes - this.master.bytesAvailable;
    }

    public String[] getBytesArray(){
        return this.memoryBlock;
    }


}
