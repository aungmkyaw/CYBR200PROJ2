import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import java.security.SecureRandom;
import java.util.*;

public class AES {
	
	public AES()
	{

	}
	
	public SecretKey generateSecretKey() throws Exception
	{
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(256);
		SecretKey key = generator.generateKey();
		return key;
	}
	
	public IvParameterSpec generateIV() throws Exception
	{
		SecureRandom rand = new SecureRandom();
		byte iv[] = new byte[32];
		rand.nextBytes(iv);
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		return ivspec;
	}

	public byte[] encrypt(byte[] msg, SecretKey spec, IvParameterSpec iv) throws Exception
	{
		byte[] encodedBytes = Base64.getEncoder().encode(msg);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, spec, iv);
		byte[] encrypted = cipher.doFinal(encodedBytes);
		return Base64.getEncoder().encode(encrypted);
	}
	
	public byte[] decrypt(byte[] encrypted, SecretKey spec, IvParameterSpec iv) throws Exception 
	{
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, spec, iv);
		byte[] decodedBytes = Base64.getDecoder().decode(encrypted);
		return cipher.doFinal(decodedBytes);
	}


}
