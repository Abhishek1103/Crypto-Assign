/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.assign.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aks
 */
public class FileServer implements Runnable{

    @Override
    public void run() {
        try {
            ServerSocket ssock = new ServerSocket(5001);
            
            while(true){
                System.out.println("Listening for connections");
                Socket sock = ssock.accept();
                System.out.println("Connection arrives at file server: "+sock);
                new Thread(new FileServerHandler(sock)).start();
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
