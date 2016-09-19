package com.ce2103.itcr.meshmemory.gui;
import android.util.Base64;
import java.io.UnsupportedEncodingException;

/**
 * Created by alecm on 18/09/16.
 */
public class Token {

    public boolean verifyToken(String token) throws UnsupportedEncodingException {
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

    public String genToken() throws UnsupportedEncodingException {
        //Generates and saves token
        long time=System.currentTimeMillis();
        String tok=String.valueOf(time);
        //Convierte de string a base 64;
        byte[] data = tok.getBytes("UTF-8");
        String token = Base64.encodeToString(data, Base64.DEFAULT);
        return  token;
    }

    void deleteToken(){
        //Creo que esto deberia ir en la clase Manager Server ya que el es el que tiene la lista de tokens
        //Deletes the  token saved if it expired
    }


}
