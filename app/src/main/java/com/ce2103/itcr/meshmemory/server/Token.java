package com.ce2103.itcr.meshmemory.server;

import android.util.Base64;
import com.ce2103.itcr.meshmemory.datastructures.DoubleLinkedList;
import java.io.UnsupportedEncodingException;

/**
 * This class generate and verify the tokens
 * Created by estape11 on 18/09/16.
 */
public class Token {
    private int expTime;

    /**
     * Constructor set the expiration time
     */
    public Token(){
        this.expTime=600000; //Tiempo de expiracion en milisegundos 600000=10 minutos
    }

    /**
     * Verify if the input token is valid by the expiration time
     * @param token
     * @return
     * @throws UnsupportedEncodingException
     */
    private boolean verifyToken(String token) throws UnsupportedEncodingException {
        //Verifies if the token exists and if it hasn't expired
        //Convierte de base64 a string
        boolean value=false;
        byte[] data = Base64.decode(token, Base64.DEFAULT);
        String text = new String(data, "UTF-8");
        long time=System.currentTimeMillis();
        if((time-Long.parseLong(text))<expTime){
            value=true;
        }
        return value;
    }

    /**
     * Verify if the input token is in the list and then if is valid by the expiration time
     * @param listToken
     * @param token
     * @return
     * @throws UnsupportedEncodingException
     */
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

    /**
     * Generates the token (using the exact time of the manager)
     * @return
     * @throws UnsupportedEncodingException
     */
    public String genToken() throws UnsupportedEncodingException {
        //Generates and saves token
        long time=System.currentTimeMillis();
        String tok=String.valueOf(time);
        //Convierte de string a base 64;
        byte[] data = tok.getBytes("UTF-8");
        String token = Base64.encodeToString(data, Base64.DEFAULT);
        return  token;
    }

}
