/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DigSigTest;

import encryption.DigitalSignature;
import encryption.RSA;

/**
 *
 * @author aks
 */
public class DigSig {
    public static void main(String[] args) throws Exception{
        RSA rsa = new RSA();
        rsa.generateKeyPair();
        
        DigitalSignature ds = new DigitalSignature(rsa.getPrivateKey());
        String username = "AKqS";
        System.out.println(""+ds.getSignature(username));
        System.out.println(""+ds.getSignature(username));
    }
}
