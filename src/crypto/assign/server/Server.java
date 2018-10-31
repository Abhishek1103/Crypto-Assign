/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.assign.server;

import encryption.AES;
import encryption.DES;
import encryption.RSA;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author aks
 */
public class Server implements Runnable {

    public static HashMap<String, Client> clientMap;
    
    protected static RSA rsa;
    protected static AES aes;
    protected static DES des;
    
    public static HashMap<String, String> usernameKeyMap;
    public static HashMap<String, String> userSigMap;
    
    @Override
    public void run() {
        try{
            
            File f = new File(System.getProperty("user.home")+"/Crypto");
            if(!f.exists()){
                f.mkdirs();
            }
            
            rsa = new RSA();
            aes = new AES();
            des = new DES();
            
            rsa.generateKeyPair();
            aes.generateAESKey();
            
            rsa.savePublicKey(System.getProperty("user.home")+"/Crypto/server_public_key");
            rsa.savePrivateKey(System.getProperty("user.home")+"/Crypto/server_private_key");
            
            
            
            clientMap = new HashMap<>();
            usernameKeyMap = new HashMap<>();
            userSigMap = new HashMap<>();
            ServerSocket ssock = new ServerSocket(5000);
            new Thread(new FileServer()).start();
            while(true){
                System.out.println("Listening for connections: ");
                Socket sock = ssock.accept();
            
                System.out.println("Connection came: "+sock);
            
                new Thread(new ClientHandler(sock)).start();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void addClient(Client client){
            clientMap.put(client.username, client);
    }
    
}
