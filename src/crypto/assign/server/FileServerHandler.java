/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.assign.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import utility.SaveFile;

/**
 *
 * @author aks
 */
public class FileServerHandler implements Runnable{

    Socket sock;

    public FileServerHandler(Socket sock) {
        this.sock = sock;
    }
    
    
    
    @Override
    public void run() {
        try{
            System.out.println("File Handler started");
            DataInputStream disf = new DataInputStream(sock.getInputStream());
            DataOutputStream dosf = new DataOutputStream(sock.getOutputStream());
            ObjectOutputStream oosf = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream oisf = new ObjectInputStream(sock.getInputStream());
            
            System.out.println("Calling save method");
            SaveFile saveFile = new SaveFile();
            saveFile.saveFile("/home/aks/Desktop/received.enc", oisf);
            
            System.out.println("File Saved");
            
            sock.close();
            
        }catch(Exception e){
            
        }
    }
    
}
