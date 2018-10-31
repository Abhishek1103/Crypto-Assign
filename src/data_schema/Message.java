/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_schema;

import java.io.Serializable;

/**
 *
 * @author aks
 */
public class Message implements Serializable{
    
    byte[] message;
    String hash;
    String signature;
    String user;

    public Message(byte[] message, String hash, String signature, String user) {
        this.message = message;
        this.hash = hash;
        this.signature = signature;
        this.user = user;
    }
    
    public byte[] getMessage(){
        return this.message;
    }
    
    public String getHash(){
        return this.hash;
    }
    
    public String getSignature(){
        return this.signature;
    }
    
    public String getUser(){
        return this.user;
    }
    
    
    public void setMessage(byte[] message){
        this.message = message;
    }
    
    public void setHash(String hash){
        this.hash = hash;
    }
    
    public void setSignature(String signature){
        this.signature = signature;
    }
    
    public void setUser(String user){
        this.user = user;
    }
}
