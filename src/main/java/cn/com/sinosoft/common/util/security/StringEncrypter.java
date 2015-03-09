package cn.com.sinosoft.common.util.security;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StringEncrypter {

	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";

	public static final String DES_ENCRYPTION_SCHEME = "DES";

	private static KeySpec keySpec;

	private static SecretKeyFactory keyFactory;

	private static SecretKey key;

	private static final String UNICODE_FORMAT = "UTF-8";

	private static String getKey() {
		return "123CSM34567890ENCRYPTIONC3PR4KEY5678901234567890";
	}

	
	public StringEncrypter() throws EncryptionException {
	 	this(DESEDE_ENCRYPTION_SCHEME, getKey()); 
	}


	private StringEncrypter(String encryptionScheme, String encryptionKey)
			throws EncryptionException {

		if (encryptionKey == null)
			throw new IllegalArgumentException();
		if (encryptionKey.trim().length() < 24)
			throw new IllegalArgumentException();
	}


	public synchronized String encrypt(String unencryptedString) throws EncryptionException {
		if (unencryptedString == null || unencryptedString.trim().length() == 0)
			throw new IllegalArgumentException();

		Cipher ecipher;
		Cipher dcipher;
		try {
			byte[] keyAsBytes = getKey().getBytes(UNICODE_FORMAT);
			keySpec = new DESedeKeySpec(keyAsBytes);
			keyFactory = SecretKeyFactory.getInstance(DESEDE_ENCRYPTION_SCHEME);
			key = keyFactory.generateSecret(keySpec);
			ecipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
			dcipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			AlgorithmParameters ap = ecipher.getParameters();
			dcipher.init(Cipher.DECRYPT_MODE, key, ap);

		} catch (InvalidKeyException e) {
			throw new EncryptionException(e);
		} catch (UnsupportedEncodingException e) {
			throw new EncryptionException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e);
		} catch (NoSuchPaddingException e) {
			throw new EncryptionException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new EncryptionException(e);
		} catch (InvalidKeySpecException e) {
			throw new EncryptionException(e);
		}
		
		try {
			
			byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] ciphertext = ecipher.doFinal(cleartext);
			BASE64Encoder base64encoder = new BASE64Encoder();
			return base64encoder.encode(ciphertext);

		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}

	public synchronized String decrypt(String encryptedString) throws EncryptionException {
		if (encryptedString == null || encryptedString.trim().length() <= 0)
			throw new IllegalArgumentException();

		Cipher ecipher;
		Cipher dcipher;
		try {
			byte[] keyAsBytes = getKey().getBytes(UNICODE_FORMAT);
			keySpec = new DESedeKeySpec(keyAsBytes);
			keyFactory = SecretKeyFactory.getInstance(DESEDE_ENCRYPTION_SCHEME);
			key = keyFactory.generateSecret(keySpec);
			ecipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
			dcipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			AlgorithmParameters ap = ecipher.getParameters();
			dcipher.init(Cipher.DECRYPT_MODE, key, ap);

		} catch (InvalidKeyException e) {
			throw new EncryptionException(e);
		} catch (UnsupportedEncodingException e) {
			throw new EncryptionException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e);
		} catch (NoSuchPaddingException e) {
			throw new EncryptionException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new EncryptionException(e);
		} catch (InvalidKeySpecException e) {
			throw new EncryptionException(e);
		}
		
		try {
			BASE64Decoder base64decoder = new BASE64Decoder();
			byte[] cleartext = base64decoder.decodeBuffer(encryptedString);
			byte[] ciphertext = dcipher.doFinal(cleartext);
			return bytes2String(ciphertext);

		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}

	private static String bytes2String(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			stringBuffer.append((char) bytes[i]);
		}
		return stringBuffer.toString();
	}

	public static class EncryptionException extends Exception {
		public EncryptionException(Throwable t) {
			super(t);
		}
	}
	
	public static void main(String args[]){
		StringEncrypter enc;
		try {
			enc = new StringEncrypter();
			String a = enc.decrypt("d3d291554ff67666e173dbcec9e07189201dd326df78d146");
			
			System.out.println(a);
		} catch (EncryptionException e) {
			e.printStackTrace();
		}
		
	}
}
