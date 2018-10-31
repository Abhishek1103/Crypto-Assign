/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.assign.server;

import com.google.common.hash.Hashing;
import data_schema.Message;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import utility.SaveFile;
import utility.SendFile;

/**
 *
 * @author aks
 */
public class ClientHandler implements Runnable{
    Socket sock;
    String username;

    ClientHandler(Socket sock) {
        this.sock = sock;
    }
    
    

    @Override
    public void run() {
        
        
        try {
//            DataInputStream dis = new DataInputStream(sock.getInputStream());
//            DataOutputStream dout = new DataOutputStream(sock.getOutputStream());
//            ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
//            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
            
            
            Client c = new Client("", sock);
            username = c.dis.readUTF();
            c.setUsername(username);
            synchronized(this){
                Server.addClient(c);
            }
            
            
            while(true){
                String flag = c.dis.readUTF();
                
                switch(flag){
                    case "#MSG":{
                        try{
                            Message msg = (Message)(c.ois.readObject());
                            byte[] encryptedMessage = msg.getMessage();
                            String hash = msg.getHash();
                            String sig = msg.getSignature();
                            String user = msg.getUser();
                            
                            
                            String genHash =  Hashing.sha256().hashBytes(encryptedMessage).toString();
                            System.out.println("Generated hash: "+genHash);
                            
                            if(genHash.equals(hash)){
                                System.out.println("Message integrity: Verified");
                            }else {
                                System.out.println("Message integrity: Failed");
                                break;
                            }
                            
                            if(sig.equals(Server.userSigMap.get(username))){
                                System.out.println("Sender Authenticated");
                            }else {
                                System.out.println("Digital Signature cannot be verified");
                                break;
                            }
                            
                            ByteArrayInputStream bais = new ByteArrayInputStream(encryptedMessage);
                            String decmsg = (String)Server.aes.decryptWithAES(bais, Server.aes.decodeKey(Server.usernameKeyMap.get(username)));
                            System.out.println("Decrypted Message: "+decmsg);
                            
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            SecretKey skey = Server.aes.decodeKey(Server.usernameKeyMap.get(user));
                            Server.aes.setSecretKey(skey);
                            Server.aes.encryptWithAES(decmsg, baos);
                            
                            msg.setMessage(baos.toByteArray());
                            msg.setHash(Hashing.sha256().hashBytes(baos.toByteArray()).toString());
                            msg.setUser(username);
                            
                            Server.clientMap.get(user).dos.writeUTF("#MSG");
                            Server.clientMap.get(user).oos.writeObject(msg);
                            System.out.println("Message Sent");
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        
                    }
                    break;
                    
                    case "#FILE":{
                        
                    }
                    break;
                    
                    case "#KEYTRANSFER":{
                        try{
                            SaveFile saveFile = new SaveFile();
                            saveFile.saveFile(System.getProperty("user.home")+"/Crypto/"+username+ "_public_key", c.ois);
                            System.out.println("Client's Public key Received");
                            
                            SendFile sendFile = new SendFile();
                            sendFile.sendFile(System.getProperty("user.home")+"/Crypto/server_public_key", c.oos);
                            System.out.println("Sever's Public key sent");
                            
                            byte[] rsaEncryptedSecretKey = (byte[])c.ois.readObject();
                            SecretKey skey = Server.rsa.decryptAESKey(rsaEncryptedSecretKey, Server.rsa.getPrivateKey());
                            System.out.println("Clients Secret key received");
                            
                            synchronized(this){
                                Server.usernameKeyMap.put(username, Server.aes.getEncodedKey(skey));
                            }
                            System.out.println("Added to usernameKeyMap"+ Server.usernameKeyMap);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                    
                    case "#DIG":{
                        String sig = c.dis.readUTF();
                        System.out.println("Signature Received: "+sig);
                        synchronized(this){
                            Server.userSigMap.put(username, sig);
                        }
                    }
                    break;
                    
                    default:System.out.println("Some error occured");
                }
            }
            
            
            
            
        } catch (IOException ex) {
           ex.printStackTrace();
        } 
       
    }
    
}
