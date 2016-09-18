package com.ce2103.itcr.meshmemory.datastructures;

/**
 * Created by alecm on 17/09/16.
 */

public class DoublyLinkedList {
    Node head;
    Node tail;

    DoublyLinkedList(){
        this.head = null;
        this.tail = null;
    }

    Node emptySlave(){
        Node current;
        if(this.head == null){
            return null;
        }else if(this.head == this.tail){
            if(this.head.Slave == null){
                return this.head;
            }else{
                return null;
            }
        }
        for(current=head; current!=tail; current=current.next){
            if(current.Slave == null){
                return current;
            }
        }
        if(current.next.Slave==null){
            return current;
        }else {
            return null;
        }

    }

    void addMem(NodeMem newMem){
        Node newNode = emptySlave();
        if (newNode == null){      //No slave role available
            newNode = new Node(newMem);
            newNode.bytes = newMem.totalBytes;

            addNode(newNode);                  //empty slave
        }else{
            newNode.Slave = newMem;
            addNode(newNode);
        }
    }

    void addNode(Node newNode){
        if(this.head == null){
            this.head = newNode;
            this.head.next = null;
            this.head.prev = null;
            this.tail = head;
        }else if(this.head == this.tail){
            this.head.next = newNode;
            newNode.prev = head;
            newNode.next = null;
            this.tail = newNode;
            this.head.next = newNode;
        }else{
            this.tail.next = newNode;
            newNode.prev= tail;
            newNode.next = null;
            this.tail.next = newNode;
            this.tail = newNode;
        }

    }

    void deleteNode(Node pNode){
        if(this.head==pNode) {
            if (this.tail == this.head) {
                this.head = null;
                this.tail = null;
            }else if(pNode.next==tail){
                pNode.next.prev=null;
                this.tail = pNode;
                this.head = pNode;
            }else{
                pNode.next.prev = null;
                pNode.next = head;
            }
        }else if(this.tail==pNode){
            if(pNode.prev==head){
                this.head.next = null;
                this.tail = this.head;
            }else{
                pNode.prev.next = null;
                this.tail = pNode.prev;
            }
        }else{
            pNode.prev.next = pNode.next;
            pNode.next.prev = pNode.prev;
            pNode.prev.next = pNode.next;
        }

    }

    void delNodeMem(Node pNodeofMem, boolean master){
        if (master) {
            if (pNodeofMem.Slave == null) {
                deleteNode(pNodeofMem);
            } else {
                pNodeofMem.Slave.master = true;
                pNodeofMem.Slave = null;
                pNodeofMem.prev.next = pNodeofMem;
                pNodeofMem.next.prev = pNodeofMem;

                //******************NOTIFICAR ESCLAVO

            }
        }else {
            pNodeofMem.Slave=null;
            pNodeofMem.prev.next=pNodeofMem;
            pNodeofMem.next.prev=pNodeofMem;

            //*****************NOTIFICAR MAESTRO DE QUE NO TIENE ESCLAVO

        }
    }

    int getTotalBytes(){
        int bytes = 0;
        for(Node current = head; current != tail; current = current.next){
            bytes+= current.getTotalBytes();
        }
        if(head!=null && head ==tail){
            return head.getTotalBytes();
        }
        return bytes + tail.getTotalBytes();
    }

    int getAvailableBytes(){
        int availableBytes=0;
        for(Node current = head; current != tail; current = current.next){
            availableBytes += current.getBytesAvailable();
        }
        if(head!=null && head ==tail){
            return head.getBytesAvailable();
        }
        return availableBytes + tail.getBytesAvailable();
    }

    int getOccupiedBytes(){
        int occupiedBytes=0;
        for(Node current = head; current != tail; current = current.next){
            occupiedBytes += current.getBytesOccupied();
        }
        if(head!=null && head ==tail){
            return head.getBytesOccupied();
        }
        return occupiedBytes + tail.getBytesOccupied();
    }


    String[] getBytesArray(){
        int totalBytes = getTotalBytes();
        int currentByte = 0;
        String bytesArray[] = new String[totalBytes];

        if (totalBytes == 0){
            return null;
        }

        for(Node current = head; current != tail; current = current.next){
            String currentNodeArray[] = current.getBytesArray();
            for(int i=0; i<current.bytes; i++){
                bytesArray[currentByte] = currentNodeArray[i];
                currentByte++;
            }
        }

        if(head==tail){
            bytesArray = head.getBytesArray();
        }

        return bytesArray;

    }

}


