/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.assign.server;

import static crypto.assign.client.Messenger.dis;
import static crypto.assign.client.Messenger.dos;
import static crypto.assign.client.Messenger.ois;
import static crypto.assign.client.Messenger.oos;
import static crypto.assign.client.Messenger.sock;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aks
 */
public class Client {
    String username;
    Socket sock;
    DataInputStream dis;
    DataOutputStream dos;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public Client(String username, Socket sock) {
        try {
            this.username = username;
            this.sock = sock;
            
            this.dis = new DataInputStream(sock.getInputStream());
            this.dos = new DataOutputStream(sock.getOutputStream());
            this.ois = new ObjectInputStream(sock.getInputStream());
            this.oos = new ObjectOutputStream(sock.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setUsername(String username){
        this.username = username;
    }
}
