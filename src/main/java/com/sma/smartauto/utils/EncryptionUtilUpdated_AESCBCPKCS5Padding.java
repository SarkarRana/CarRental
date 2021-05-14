package com.sma.smartauto.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptionUtilUpdated_AESCBCPKCS5Padding {
	private static final String key = "aesEncryptionKey";
	private static final int ivSize = 16;		//16 Bytes = 128 bits
	private static final int keySize = 16;		//16 Bytes = 128 bits
	private static Cipher cipher = null;
	
	static {
		 try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (Exception ex) {
//			logger.error("Error while initializing encryption util", ex);
			try {
				throw new Exception( "Error while initializing encryption util");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String encrypt(String plainText) throws Exception {
		if(cipher == null) {
			throw new Exception( "Error while initializing encryption util");
		}
		try {
	        byte[] clean = plainText.getBytes();
	
	        // Generating IV.
	        byte[] iv = new byte[ivSize];
	        SecureRandom random = new SecureRandom();
	        random.nextBytes(iv);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
	
	        // Hashing key.
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        digest.update(key.getBytes("UTF-8"));
	        byte[] keyBytes = new byte[keySize];
	        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
	        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
	
	        // Encrypt.       
	        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
	        byte[] encrypted = cipher.doFinal(clean);
	
	        // Combine IV and encrypted part.
	        byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
	        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
	        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);
	        return new String(Base64.encodeBase64(encryptedIVAndText));
        } catch (Exception e) {
//			logger.error("could not encrypt string:" + plainText, e);
			throw new Exception("encryption error");
		}
    }

    public static String decrypt(String encryptedText) throws Exception {
    	if(cipher == null) {
			throw new Exception("Error while initializing encryption util");
		}
    	try {
	        byte[] encryptedIvTextBytes =  Base64.decodeBase64(encryptedText);
	        
	        // Extract IV.
	        byte[] iv = new byte[ivSize];
	        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
	
	        // Extract encrypted part.
	        int encryptedSize = encryptedIvTextBytes.length - ivSize;
	        byte[] encryptedBytes = new byte[encryptedSize];
	        System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);
	
	        // Hash key.
	        byte[] keyBytes = new byte[keySize];
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        md.update(key.getBytes());
	        System.arraycopy(md.digest(), 0, keyBytes, 0, keyBytes.length);
	        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
	
	        // Decrypt.
	        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
	        byte[] decrypted = cipher.doFinal(encryptedBytes);
	
	        return new String(decrypted);
    	} catch (Exception e) {
//			logger.error("could not decrypt string:" + encryptedText, e);
			throw new Exception( "Invalid encrypted string");
		}
    }
	
	public static void main(String[] args) throws Exception {
		String cipherText = EncryptionUtilUpdated_AESCBCPKCS5Padding.encrypt("mypassword@123");
		System.out.println("cipherText: " + cipherText);
		System.out.println("PlainText: " + EncryptionUtilUpdated_AESCBCPKCS5Padding.decrypt(cipherText));
		}
}
