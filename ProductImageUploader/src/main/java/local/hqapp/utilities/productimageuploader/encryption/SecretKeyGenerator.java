package local.hqapp.utilities.productimageuploader.encryption;

import java.util.Random;

public class SecretKeyGenerator {

	public static String generateKey(int length) {
		
		int leftLimit = 48;
	    int rightLimit = 122;
	    Random random = new Random(); 

	    String key = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(length)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
	    
	    return key;
	}
}
