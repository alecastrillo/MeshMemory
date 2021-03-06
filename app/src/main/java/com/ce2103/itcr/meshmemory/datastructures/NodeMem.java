package com.ce2103.itcr.meshmemory.datastructures;

import java.net.Socket;

/**
 * Created by alecm on 17/09/16.
 */
public class NodeMem {
    public int phoneNum;
    public int totalBytes;
    public int occuBytes;
    public int bytesAvailable;
    public boolean master;
    public Socket socket;
    public NodeMem(){}

    public NodeMem(int pBytes, int pPhoneNum, Socket socket){
        this.master = true;
        this.phoneNum = pPhoneNum;
        this.totalBytes = pBytes;
        this.bytesAvailable = pBytes;
        this.occuBytes = 0;
        this.socket = socket;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    public int getOccuBytes() {
        return occuBytes;
    }

    public int getBytesAvailable() {
        return bytesAvailable;
    }

    public Socket getSocket() {
        return socket;
    }

}
