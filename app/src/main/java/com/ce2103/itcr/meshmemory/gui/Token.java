package com.ce2103.itcr.meshmemory.gui;

import android.util.Base64;
import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import java.io.UnsupportedEncodingException;

/**
 * Created by alecm and estape11 on 18/09/16.
 */
public class Token {

    private boolean verifyToken(String token) throws UnsupportedEncodingException {
        //Verifies if the token exists and if it hasn't expired
        //Convierte de base64 a string
        boolean value=false;
        byte[] data = Base64.decode(token, Base64.DEFAULT);
        String text = new String(data, "UTF-8");
        long time=System.currentTimeMillis();
        if((time-Long.parseLong(text))<600000){ //Verifica que el token tenga menos de 10 minutos
            value=true;
        }
        return value;
    }

    public int verifyToken(DoubleLinkedList listToken,String token) throws UnsupportedEncodingException {
        int value=1; // 1 no esta en la lista de tokens, 2 ya expiro y 0 que es valido
        for(int i=0;i<listToken.size();i++){
            if( token.equals(listToken.get(i)) ){
                if(verifyToken(token)){
                    value=0;
                    break;
                }
                else {
                    value = 2;
                }
            }
            else{
                value=1;
            }
        }
        return value;
    }

    public String genToken() throws UnsupportedEncodingException {
        //Generates and saves token
        long time=System.currentTimeMillis();
        String tok=String.valueOf(time);
        //Convierte de string a base 64;
        byte[] data = tok.getBytes("UTF-8");
        String token = Base64.encodeToString(data, Base64.DEFAULT);
        String tkn = token.substring(0, token.length()-2);
        System.out.println("Token sin los dos ultimos "+ tkn);
        System.out.println("Token "+token);
        return  token;
    }

    void deleteToken(){
        //Creo que esto deberia ir en la clase Manager Server ya que el es el que tiene la lista de tokens
        //Deletes the  token saved if it expired
    }


}
