package com.ce2103.itcr.meshmemory.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class NodeSockets{
    int port;
    String ip;
    public static Socket socket = null;
    public  static  String mensajeria;
    BufferedReader reader = null;
    PrintWriter writer = null;
    Thread thread=null;
    String message_in = null;

    String response = "";

    public NodeSockets(){
        connect();
    }

    public void connect(){
        thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if(socket==null){
                                socket = new Socket(ip, port);
                                read();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            System.out.println("Couldn't connect with manager");
                        }
                    }
                }
        );
        thread.start();
    }

    public void read(){
        Thread read_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println(reader.toString());
                    while (true){
                        String message = reader.readLine();
                        System.out.println("Received: "+ message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        read_thread.start();
    }

    public void write(String pMessageOut){
        try{
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(pMessageOut);
            System.out.println(pMessageOut);
        }catch (Exception e ){
            e.printStackTrace();
        }
    }



    //video YT
    protected void client(){
        Socket socket = null;
        try {
            System.out.println("Trying to connect to server");
            socket = new Socket(ip, port);
            System.out.println("Connection established");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.newLine();
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Message from manager: "+ br.readLine());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    //pag intern
    NodeSockets(int port, String ip){
        this.port = port;
        this.ip = ip;
    }

    protected Void doInBackground(Void... arg0) {
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int bytesRead;
            InputStream inputStream = socket.getInputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
