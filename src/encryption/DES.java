/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author aks
 */
public class DES {
    private static final byte[] iv = { 11, 22, 33, 44, 99, 88, 77, 66 };
    private final String ALGORITHM = "DES";
    private final String ALGORITHM_PADDING = "DES/CBC/PKCS5Padding";
    
    private SecretKey skey;
    private AlgorithmParameterSpec paramSpec;
    
    public void generateKey() throws NoSuchAlgorithmException{
        SecretKey key = KeyGenerator.getInstance(ALGORITHM).generateKey();
        paramSpec = new IvParameterSpec(iv);
        this.skey = key;
    }
    
    public SecretKey getSecretKey(){
        return this.skey;
    }
    
    public void setPublicKey(SecretKey skey){
        this.skey = skey;
    }
    
    
    // Encode Secret key to Base64 string
    public String getEncodedKey(SecretKey key) throws Exception{
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // Decode base64 String to Secret Key
    public SecretKey decodeKey(String encodedKey) throws Exception{
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
        return originalKey;
    }
    
    
    public void encryptWithDES(Serializable object, OutputStream _outputStream) throws InvalidKeyException, IOException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        Cipher cipher = Cipher.getInstance(ALGORITHM_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skey, paramSpec);
        SealedObject sealedObject = new SealedObject(object, cipher);

        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(_outputStream, cipher);
        ObjectOutputStream outputStream = new ObjectOutputStream(cos);
        outputStream.writeObject(sealedObject);
        outputStream.close();
    }

    // Decrypt with AES
    public Object decryptWithDES(InputStream _inputStream, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);

        CipherInputStream cipherInputStream = new CipherInputStream(_inputStream, cipher);
        ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
        SealedObject sealedObject;
        try {
            sealedObject = (SealedObject) inputStream.readObject();
            return sealedObject.getObject(cipher);
        } catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    
    public String encryptFile(String filename, SecretKey skey) throws FileNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, InvalidAlgorithmParameterException {
        File f = new File(filename);
        String outFile = f.getName()+".enc";
        
        FileInputStream fis = new FileInputStream(f);
        FileOutputStream fos = new FileOutputStream(outFile);
        
        Cipher cipher = Cipher.getInstance(ALGORITHM_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skey, paramSpec);
        
        OutputStream os = new CipherOutputStream(fos, cipher);
        writeData(fis, os);
        
        return outFile;
    }
    
    public void decryptFile(String filename, String outFile,SecretKey skey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, IOException, InvalidAlgorithmParameterException{
        Cipher cipher = Cipher.getInstance(ALGORITHM_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skey, paramSpec);
        
        InputStream is = new CipherInputStream(new FileInputStream(new File(filename)), cipher);
        writeData(is, new FileOutputStream(new File(outFile)));
    }
    
    
    private  void writeData(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[1024];
		int numRead = 0;
		// read and write operation
		while ((numRead = is.read(buf)) >= 0) {
			os.write(buf, 0, numRead);
		}
		os.close();
		is.close();
	}
}
