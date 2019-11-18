//http://rodrigolazoti.com.br/2009/07/06/criptografia-tripe-des-em-java/
package br.com.sistema.seguranca;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class TripleDES {
	
	private Cipher cipher;
	private byte[] encryptKey;
	private KeySpec keySpec;
	private SecretKeyFactory secretKeyFactory;
	private SecretKey secretKey;

	public TripleDES(String key) throws Exception {
		encryptKey = key.getBytes("UTF-8");
		cipher = Cipher.getInstance("DESede");
		keySpec = new DESedeKeySpec(encryptKey);
		secretKeyFactory = SecretKeyFactory.getInstance("DESede");
		secretKey = secretKeyFactory.generateSecret(keySpec);
	}

	public String encrypt(String value) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] cipherText = cipher.doFinal(value.getBytes("UTF-8"));
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(cipherText);
	}
	 
	public String decrypt(String value) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		BASE64Decoder dec = new BASE64Decoder();
		byte[] decipherText = cipher.doFinal(dec.decodeBuffer(value));
		return new String(decipherText);
	}
}