package local.hqapp.utilities.productimageuploader.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;

public class Encryptor {

	private SecretKey secretKey;
	private String encryptedText;
	private IvParameterSpec iv; 
	
	public String getEncryptedText() {
		return encryptedText;
	}
	
	public void encrypt(String text) {
		
		String sKey = generateSKeyText();
		String salt = generateSKeySalt();
		secretKey = generateKey(sKey, salt);
		iv = generateIv();
		String algorithm = "AES/CBC/PKCS5Padding";
		
		if (secretKey != null) {
			encryptedText = encrypt(algorithm, text, secretKey, iv);
		}
	}
	
	public IvParameterSpec getIv() {
		return iv;
	}

	public void encrypt(String text, String secretKeyParam, String saltParam) {
		
		byte[] decodedKey = Base64.getDecoder().decode(secretKeyParam);
		byte[] biv = Base64.getDecoder().decode(saltParam);
		
		secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
		IvParameterSpec iv = new IvParameterSpec(biv);
		String algorithm = "AES/CBC/PKCS5Padding";
		
		if (secretKey != null) {
			encryptedText = encrypt(algorithm, text, secretKey, iv);
		}

	}
	
	public void clear() {
		secretKey = null;
		encryptedText = null;
	}
	
	public SecretKey getSecretKey() {
		return secretKey;
	}

	private String generateSKeyText() {
		return SecretKeyGenerator.generateKey(30);
	}
	
	private String generateSKeySalt() {
		return SecretKeyGenerator.generateKey(30);
	}
	
	private String encrypt(String algorithm, String input, SecretKey key,
		    			   IvParameterSpec iv) {
		    
	    Cipher cipher;
		try {
			cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte[] cipherText = cipher.doFinal(input.getBytes());
			return Base64.getEncoder().encodeToString(cipherText);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			
			e.printStackTrace();
		} catch (BadPaddingException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	private IvParameterSpec generateIv() {
	    byte[] iv = new byte[16];
	    new SecureRandom().nextBytes(iv);
	    return new IvParameterSpec(iv);
	}
	
	private SecretKey generateKey(String password, String salt) {
		
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
			    
			return secret;
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			
			e.printStackTrace();
		}
	  
		return null;
	}
	
	public String getEncodedKey(SecretKey s) {
		
		if (s == null) {
			return null;
		}
		
		return Base64.getEncoder().encodeToString(s.getEncoded());
	}

	public String getEncodedIV(IvParameterSpec i) {
		
		if (i == null) {
			return null;
		}
		
		return new String(Base64.getEncoder().encodeToString(i.getIV()));
	}
}
