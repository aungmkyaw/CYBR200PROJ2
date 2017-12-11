import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class myCrypto {
	
	AES aes;
	RSA rsa;
	
	public myCrypto()
	{
		this.aes = new AES();
		this.rsa = new RSA();
	}
	
	public byte[] encrypt(byte[] msg) throws Exception
	{

		//generate a a symmetric AES key		
		SecretKey secret = aes.generateSecretKey();
		IvParameterSpec iv = aes.generateIV();
		//encrypt the data using the AES symmetric key
		byte[] cipherText = aes.encrypt(msg, secret, iv);
		String cipherTxt = new String(cipherText);
		System.out.println("(AES)Encrypted: " + cipherTxt);
		//RSA encrypt secret key
		rsa.generateKeyPair();
		byte[] encodedsecretkey = secret.getEncoded();
		byte[] ivarr = iv.getIV();
		
//		String see = new String(ivarr);
//		System.out.println("here1:  "+see);
		
		byte[] combinedsecret = new byte[ivarr.length+encodedsecretkey.length+cipherText.length];
		System.arraycopy(ivarr, 0, combinedsecret, 0, ivarr.length);
		System.arraycopy(encodedsecretkey, 0, combinedsecret, ivarr.length, encodedsecretkey.length);
		System.arraycopy(cipherText, 0, combinedsecret, ivarr.length+encodedsecretkey.length, cipherText.length);
		
//		byte[] ivs = Arrays.copyOfRange(combinedsecret, 0, 16);
//		String see1 = new String(ivs);
//		System.out.println("here1:  "+see1);
//		System.out.println("Compare: " + Arrays.equals(ivarr, ivs));

		byte[] cipher = rsa.encrypt(combinedsecret);
		String c = new String(cipher);
		System.out.println("(RSA)Encrypted: " + c);
		return cipher;
	}
	public byte[] decrypt(byte[] payload) throws Exception
	{
		
		byte[] cip = rsa.decrypt(payload);
		byte[] ivs = Arrays.copyOfRange(cip, 0, 16);
		IvParameterSpec iv = new IvParameterSpec(ivs);
		
		byte[] akeyencoded = Arrays.copyOfRange(cip, 16, 48);
		SecretKey originalkey = new SecretKeySpec(akeyencoded, 0, akeyencoded.length, "AES");
		
		byte[] ctext = Arrays.copyOfRange(cip, 48, cip.length);
		
		String aeskey = new String(akeyencoded);
		System.out.println("(RSA)Decrypted: " + aeskey);
		
		byte[] txt = aes.decrypt(ctext, originalkey, iv);
		String text = new String(txt);
		System.out.println("(AES)Decrypted: " + text);
		return txt;
	}
	
	public byte[] returnPubKey() throws Exception
	{
		return rsa.loadPublicKey().getEncoded();
	}
	
	public PublicKey convertToPubKey(byte[] arr) throws InvalidKeySpecException, NoSuchAlgorithmException
	{
		PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(arr));
		return publicKey;
	}

}
