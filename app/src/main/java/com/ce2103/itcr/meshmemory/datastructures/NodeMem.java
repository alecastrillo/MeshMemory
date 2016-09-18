package com.ce2103.itcr.meshmemory.datastructures;

/**
 * Created by alecm on 17/09/16.
 */
public class NodeMem {
    int phoneNum;
    int totalBytes;
    int occuBytes;
    int bytesAvailable;
    boolean master;



    NodeMem(){

    }

    NodeMem(int pBytes, int pPhoneNum){
        this.master = true;
        this.phoneNum = pPhoneNum;
        this.totalBytes = pBytes;
        this.bytesAvailable = pBytes;
        this.occuBytes = 0;

    }
}
