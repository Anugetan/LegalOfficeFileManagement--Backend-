package local.hqapp.utilities.productimageuploader.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

public class Decryptor {

public String decrypt(String encryptedText, String secretKeyParam, String saltParam) {
		
		byte[] decodedKey = Base64.getDecoder().decode(secretKeyParam);
		byte[] biv = Base64.getDecoder().decode(saltParam);
		
		SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
		IvParameterSpec iv = new IvParameterSpec(biv);
		String algorithm = "AES/CBC/PKCS5Padding";
		
		String plainText = null;
		
		if (secretKey != null) {
			plainText = decrypt(algorithm, encryptedText, secretKey, iv);
		}

		return plainText;
	}

	private String decrypt(String algorithm, String encryptedText, SecretKey secretKey, IvParameterSpec iv) {
		
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		    byte[] plainText = cipher.doFinal(Base64.getDecoder()
		        .decode(encryptedText));
		    return new String(plainText);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
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
}
