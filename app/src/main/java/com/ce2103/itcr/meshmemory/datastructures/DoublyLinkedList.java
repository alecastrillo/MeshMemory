package com.ce2103.itcr.meshmemory.datastructures;

import com.ce2103.itcr.meshmemory.gui.GarbageCollection;

/**
 * Created by alecm on 17/09/16.
 */

public class DoublyLinkedList {
    public Node head;
    public Node tail;

    public DoublyLinkedList(){
        this.head = null;
        this.tail = null;
    }

    /**
     * Determines if there is a node without a slave in the list
     * @return The node without slave of null if there isn't a node without a slave
     */
    public Node emptySlave(){
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

    /**
     * Adds a Member to the list, it will be a slave if there is a node
     * without a slave, or master i there is no slave role available.
     * @param newMem
     */
    public void addMem(NodeMem newMem){
        Node newNode = emptySlave();
        if (newNode == null){      //No slave role available
            newMem.master=true;
            newMem.master = true;
            newNode = new Node(newMem);
            newNode.bytes = newMem.totalBytes;
            addNode(newNode);                  //empty slave
            System.out.println("Node Member created as a Master");
        }else{
            newMem.master = false;
            newMem.master = false;
            newNode.Slave = newMem;
            if(newNode.prev==null){
                if (newNode.next==null){
                    head = newNode;
                    tail = newNode;
                }else{
                    newNode.next.prev = newNode;
                    head = newNode;
                }
            }else if(newNode.next==null){
                newNode.prev.next = newNode;
                tail = newNode;
            }else{
                newNode.prev.next = newNode;
                newNode.next.prev = newNode;
            }
            System.out.println("Node Member created as a Slave");
        }

    }

    /**
     * Adds a node as the tail of the list
     * @param newNode
     */
    public void addNode(Node newNode){
        if(this.head == null){
            this.head = newNode;
            this.head.next = null;
            this.head.prev = null;
            this.tail = head;
        }else if(this.head == this.tail){                       /////////////////
            this.head.next = newNode;
            newNode.prev = head;
            newNode.next = null;
            this.tail = newNode;
            this.head.next = tail;
        }else{
            this.tail.next = newNode;
            newNode.prev= tail;
            newNode.next = null;
            this.tail.next = newNode;
            this.tail = newNode;
        }
        System.out.println("Node added to the list");

    }

    /**
     * @param pNode
     */
    public void deleteNode(Node pNode){
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

    /**
     * Deletes a member of the node, if the member deleted is a master
     * then the member left (the slave) will be the new master of the
     * node.
     * @param pNodeofMem
     * @param master
     */
    public void delNodeMem(Node pNodeofMem, boolean master){
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

    /**
     * @return Total bytes of the list (bytes given by the nodes)
     */
    public int getTotalBytes(){
        int bytes = 0;
        for(Node current = head; current != tail; current = current.next){
            bytes+= current.getTotalBytes();
        }
        if(head!=null && head ==tail){
            return head.getTotalBytes();
        }
        return bytes + tail.getTotalBytes();
    }

    /**
     * @return Amount of bytes available in the list
     */
    public int getAvailableBytes(){
        int availableBytes=0;
        for(Node current = head; current != tail; current = current.next){
            availableBytes += current.getBytesAvailable();
        }
        if(head!=null && head ==tail){
            return head.getBytesAvailable();
        }
        return availableBytes + tail.getBytesAvailable();
    }

    /**
     * @return Amount of bytes occupied of the list
     */
    public int getOccupiedBytes(){
        int occupiedBytes=0;
        for(Node current = head; current != tail; current = current.next){
            occupiedBytes += current.getBytesOccupied();
        }
        if(head!=null && head ==tail){
            return head.getBytesOccupied();
        }
        return occupiedBytes + tail.getBytesOccupied();
    }

    /**
     * Makes a String array with the size of the total amount of bytes
     * in the list, each element of the array will contain one of two
     * options:
     *   - "Available"
     *   - "Token; UUID" : token of the client who it belongs and the UUID related to the data
     * @return
     */
    public String[] getBytesArray(){
        int totalBytes = getTotalBytes();
        int currentByte = 0;
        String bytesArray[] = new String[totalBytes];
        if (totalBytes == 0){
            return null;
        }
        for(Node current = head; current != null; current = current.next){
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

    private void printArrayOfNode(Node node){
        System.out.print("Array: ");
        for(int i=0; i<node.bytes; i++){
            System.out.print(node.memoryBlock[i]);
        }
        System.out.println(" ");
    }

    /**
     * Search for the node that contains the data
     * with the UUID taken as parameter
     * @param UUID
     * @return
     */

    public NodeMem ownerOfUUID(String UUID){
        if(head==null){
            return null;
        }
        if(amountOfNodes()==1){
            System.out.println("head==tail");
            if(head.ownerUUID(UUID)){
                System.out.println("head.ownerUUID");
                printArrayOfNode(head);
                return head.master;
            }
        }
        for(Node current = head; current!=tail; current=current.next){
            if (current.ownerUUID(UUID)){
                printArrayOfNode(current);
                return  current.master;
            }
        }
        if (tail.ownerUUID(UUID)){
            printArrayOfNode(tail);
            return tail.master;
        }
        return null;
    }

    /**
     * Search for a node with the amount of bytes available
     * defined as parameter.
     * @param bytes
     * @return
     */
    public Node nodeBytesAvailable(int bytes){
        for(Node current = head; current != null; current=current.next){
            if(current.getBytesAvailable()>=bytes){
                return current;
            }
        }
        return null;
    }

    /**
     * @return AMount of nodes in the list
     */
    public int amountOfNodes(){
        int count=0;
        for(Node current=head; current!=null; current=current.next){
            count++;
        }
        System.out.println("count "+count);
        return count;
    }

    /**
     * Creates an array with the size of twice the amount
     * of nodes of the list, each node is related to two
     * elements of the array, the first element will contain the
     * amount of bytes that the node will provide to save
     * the data, the second one contains the node. For example:
     * if the list has only three nodes:
     *     returns {2,head,5,tail}
     *     The first node is the head and it will provide 2 bytes
     *     The second node provides 5bytes
     * @param pBytes
     * @return Array
     */
    //Estebitan
    public Object[] nodesBytesAvailable(int pBytes, String UUID){
        if (pBytes<=0){
            return null;
        }
        System.out.println("Bytes available in the list: "+getAvailableBytes());
        if (getAvailableBytes()<pBytes){
            return null;
        }
        Object[] nodes;
        Node node = nodeBytesAvailable(pBytes);
        if(node!=null){
            nodes = new Object[2];
            nodes[0]=pBytes;
            nodes[1]=node;
            saveUUIDinNodeBytesArray(pBytes, (Node)nodes[1], UUID);
            return nodes;
        }
        int nodesGivingBytes=0;
        int indexBeingFilled=0;
        int bytesFilled=0;
        nodes = new Object[amountOfNodes()*2];
        for(Node current=head; current!=null; current=current.next){
            if(bytesFilled==pBytes){
                nodes[indexBeingFilled] = null;
                nodes[indexBeingFilled+1] = null;
                indexBeingFilled+=2;
            }else if(current.getBytesAvailable()==0){
                nodes[indexBeingFilled]=null;
                nodes[indexBeingFilled+1]=null;
                indexBeingFilled+=2;
            }else{
                int x=current.getBytesAvailable();
                if(x <= pBytes-bytesFilled){
                    nodes[indexBeingFilled]=x;
                    nodes[indexBeingFilled+1]=current;
                    bytesFilled+= x;
                    nodesGivingBytes++;
                    indexBeingFilled+=2;
                }else if(x > pBytes-bytesFilled){
                    nodes[indexBeingFilled]=pBytes-bytesFilled;
                    nodes[indexBeingFilled+1]=current;
                    bytesFilled+= pBytes-bytesFilled;
                    nodesGivingBytes++;
                    indexBeingFilled+=2;
                }
            }

        }
        return saveUUIDinBytes(nodes, nodesGivingBytes, UUID);
    }

    public Object[] arrayOfNodesWithUUID(String UUID){
        int totalBytes = getTotalBytes();
        System.out.println("Total bytes: "+totalBytes);
        Object[] array = new Object[totalBytes];
        int nodes = 0;
        int currentIndex = 0;
        for(Node currentNode = head; currentNode!=null; currentNode=currentNode.next){
            //int x = currentNode.bytesWithUUID(UUID);
            if(currentNode.ownerUUID(UUID)){
                System.out.println("currentNode.ownerUUID");
                array[currentIndex]=currentNode;
                nodes++;
            }else{
                array[currentIndex]=null;
            }
            currentIndex+=2;
        }
        Object[] arrayWithNodes = new Object[nodes];
        System.out.println("nodes: " + nodes);
        int nodesDone = 0;
        for(int i=0; i<totalBytes; i++){
            if(array[i]!=null){
                arrayWithNodes[nodesDone]=array[i];
                nodesDone++;
            }
        }
        return arrayWithNodes;

    }

    /**
     * Saves the UUID in the nodes given in the array
     *
     */
    public Object[] saveUUIDinBytes(Object[] nodesArray, int nodesGivingBytes, String UUID){
        Object[]array = new Object[nodesGivingBytes*2];
        int arrayIndex = 0;
        for(int i=0 ; i<amountOfNodes()*2 ; i+=2){
            if(nodesArray[i]!=null){
                array[arrayIndex]=nodesArray[i];
                array[arrayIndex+1]=nodesArray[i+1];
                arrayIndex+=2;
            }
        }
        for(int i=0; i<nodesGivingBytes*2; i+=2){
            saveUUIDinNodeBytesArray((int)array[i], (Node)array[i+1], UUID);
        }
        return nodesArray;

    }
    /*
    public void nodeModified(Node node){
        for(Node current=node; current!=null; current=current.prev){
            if(current.master.socket==head.master.socket){
                head=current;
                break;
            }
        }
    }
    */
    /**
     * Saves the UUID in the array of the node entered as 
     * parameter
     */
    public void saveUUIDinNodeBytesArray(int bytes, Node node, String UUID){
        int x=0;
        for(int i=0; i<node.bytes; i++){
            if(x==bytes){
                return;
            }
            if(node.memoryBlock[i]=="Available"){
                node.memoryBlock[i]=UUID;
                node.bytesAvailable--;
                if(node.next!=null){
                    node.next.prev = node;
                }if(node.prev!=null){
                    node.prev.next=node;
                }
                //nodeModified(node);
                x++;
            }
        }
        System.out.println("UUID saved...UUID: "+UUID+" socket: "+node.master.socket.toString());
    }

    void burping(){

    }

    void garbageCollection(){

    }

}


