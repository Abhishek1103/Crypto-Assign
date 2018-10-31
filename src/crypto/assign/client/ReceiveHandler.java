/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.assign.client;

import com.google.common.hash.Hashing;
import data_schema.Message;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/**
 *
 * @author aks
 */
public class ReceiveHandler implements Runnable{
    
    
    public DataInputStream dis = null;

    @Override
    public void run() {
        System.out.println("Receive handler started");
        
        try{
            String flag = dis.readUTF();
            
            
            switch(flag){
                case "#MSG":{
                    Message m = (Message)Messenger.ois.readObject();
                    byte[] msg = m.getMessage();
                    String hash = m.getHash();
                    String sig = m.getSignature();
                    String user = m.getUser();
                    
                    ByteArrayInputStream bais = new ByteArrayInputStream(msg);
                    String decryptedMsg = (String)Messenger.aes.decryptWithAES(bais, Messenger.aes.getSecretKey());
                    
                    String genHash = Hashing.sha256().hashBytes(msg).toString();
                    System.out.println("Generated Hash: "+ genHash);
                    if(genHash.equals(hash)){
                        System.out.println("Integrity verified");
                    }else {
                        System.out.println("Message integrity failed..!!");
                        break;
                    }
                    
                    // TODO: Check Server digital signature
                    
                    Messenger.displayArea.append(user+": "+decryptedMsg+"\n");
                }
                break;
                
                case "#FILE":{
                    
                }
                break;
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
