/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aks
 */



public class DigitalSignature {

    private static final String ALGO = "SHA256WithRSA";
    
    Signature signature;
    
    public DigitalSignature(PrivateKey pkey) {
        
        try {
            Signature signature = Signature.getInstance(ALGO);
            SecureRandom secureRandom = new SecureRandom();
            
            signature.initSign(pkey, secureRandom);
            this.signature = signature;
        
        
        } catch (Exception ex) {
            Logger.getLogger(DigitalSignature.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    
    public byte[] getSignature(String username)throws Exception{
        this.signature.update(username.getBytes("UTF-8"));
        
        byte[] digitalSignature = this.signature.sign();
        return digitalSignature;
    }
    
    
}
