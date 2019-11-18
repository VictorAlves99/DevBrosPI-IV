//http://www.guj.com.br/java/134647-criptografia-aes---java-e-c
package br.com.sistema.seguranca;

import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import br.com.sistema.jsf.Mensagens;

public class AES {

	private static final String hexDigits = "0123456789abcdef";

	public static String criptografar(String key, String message)
			throws Exception {

		byte[] hexByte = asByte(key);
		SecretKeySpec skeySpec = new SecretKeySpec(hexByte, "AES");

		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		byte[] encrypted = cipher.doFinal((message.getBytes()));
		return asHex(encrypted);
	}

	public static String deDeriptografar(String key, String hex)
			throws Exception {

		byte[] hexByte = asByte(key);
		SecretKeySpec skeySpec = new SecretKeySpec(hexByte, "AES");

		Cipher cipher = null;

		cipher = Cipher.getInstance("AES");

		byte[] encrypted = new BigInteger(hex, 16).toByteArray();

		cipher.init(Cipher.DECRYPT_MODE, skeySpec);

		byte[] original = null;

		original = cipher.doFinal(encrypted);

		String originalString = new String(original);

		return originalString;
	}

	public static String asHex(byte[] b) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < b.length; i++) {
			int j = (b[i]) & 0xFF;
			buf.append(hexDigits.charAt(j / 16));
			buf.append(hexDigits.charAt(j % 16));
		}

		return buf.toString();
	}

	public static byte[] asByte(String hexa) throws Exception {

		if (hexa.length() % 2 != 0) {
			throw new Exception(
					Mensagens
							.getMensagem("A Key precisa ter uma quantidade par de elementos"));
		}

		byte[] b = new byte[hexa.length() / 2];

		for (int i = 0; i < hexa.length(); i += 2) {
			b[i / 2] = (byte) ((hexDigits.indexOf(hexa.charAt(i)) << 4) | (hexDigits
					.indexOf(hexa.charAt(i + 1))));
		}
		return b;
	}
}
