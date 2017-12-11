import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


public class RSA {
	
	private byte[] rsapubkey;
	private byte[] rsaprikey;
	
	public RSA()
	{
		this.rsaprikey = new byte[32];
		this.rsapubkey = new byte[32];
	}
	
	public void generateKeyPair() throws Exception 
	{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom rand = new SecureRandom();
		keyGen.initialize(1024, rand);
		KeyPair generatedKeyPair = keyGen.genKeyPair();
		savePublicKey(generatedKeyPair.getPublic());
		savePrivateKey(generatedKeyPair.getPrivate());
	}
	
	private PublicKey loadPublicKey() throws Exception 
	{
		byte[] encodedPublicKey = rsapubkey;
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
		encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		return publicKey;
	}
	
	private PrivateKey loadPrivateKey() throws Exception 
	{
		byte[] encodedPrivateKey = rsaprikey;
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
		encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
		return privateKey;
	}
	
	public void savePublicKey(PublicKey key) throws Exception 
	{
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
		key.getEncoded());
		rsapubkey = x509EncodedKeySpec.getEncoded();
	}
	
	private void savePrivateKey(PrivateKey key) throws Exception 
	{
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
		key.getEncoded());
		rsaprikey = pkcs8EncodedKeySpec.getEncoded();
	}
	
	public byte[] encrypt(byte[] msg) throws Exception
	{
		Cipher cipher;
		byte[] cipherData = null;
		cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey());
		cipherData = cipher.doFinal(msg);
		return cipherData;
	}
	
	public byte[] decrypt(byte[] encrypted) throws Exception
	{
		Cipher cipher;
		byte[] decryptedData = null;
		cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, loadPrivateKey());
		decryptedData = cipher.doFinal(encrypted);
		return decryptedData;
	}

}
