/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import crypto.assign.client.Messenger;
import static crypto.assign.client.Messenger.aes;
import crypto.assign.client.ReceiveHandler;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

/**
 *
 * @author aks
 */
public class InitialKeyExchange implements Runnable{
    
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public InitialKeyExchange(ObjectInputStream ois, ObjectOutputStream oos) {
        this.ois = ois;
        this.oos = oos;
    }
    
    

    @Override
    public void run() {
        
        try{
            
            SendFile sendFile = new SendFile();
            SaveFile saveFile = new SaveFile();
            
            sendFile.sendFile(System.getProperty("user.home")+"/Crypto/"+Messenger.username+"/public_key", oos);
            
            saveFile.saveFile(System.getProperty("user.home")+"/Crypto/"+Messenger.username+"/server_public_key", ois);
            
            System.out.println("Public Keys Exchanged");
            
             SecretKey skey = aes.getSecretKey();
             PublicKey pkey = Messenger.rsa.readPublicKeyFromFile(System.getProperty("user.home")+"/Crypto/"+Messenger.username+"/server_public_key");
             byte[] encryptedSecretKey = Messenger.rsa.encryptSecretKey(skey, pkey);
             
             oos.writeObject(encryptedSecretKey);
             System.out.println("SecretKey Sent");
             
             JOptionPane.showMessageDialog(null, "Key Exchange SuccessFul");
             
             ReceiveHandler rc = new ReceiveHandler();
             rc.dis = Messenger.dis;
             new Thread(rc).start();
             
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }
    
}
