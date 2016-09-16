package com.ce2103.itcr.meshmemory.server;
import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import java.net.Socket;

/**
 * Created by estape11 on 15/09/16.
 */

public class NodeSocket {
    private Socket sock;
    private String block;
    private int bytes;

    public NodeSocket(Socket sock,String block, int bytes) {
        this.sock=sock;
        this.block=block;
        this.bytes=bytes;
    }

    public Socket getSocket() {
        return sock;
    }

    public String getBloque() {
        return block;
    }

    public int getBytes() { return bytes; }

}
