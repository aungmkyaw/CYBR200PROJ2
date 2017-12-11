import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class main {

	public static void main(String[] args) throws Exception
	{
		
		String msg = "Hello";
		AES aes = new AES();
		SecretKey secret = aes.generateSecretKey();
		IvParameterSpec iv = aes.generateIV();
		byte[] cipherText = aes.encrypt(msg.getBytes(), secret, iv);
		byte[] txt = aes.decrypt(cipherText, secret, iv);
		System.out.println("Original Msg: "+msg);
		String cipherTxt = new String(cipherText);
		System.out.println("(AES)Encrypted: " + cipherTxt);
		String text = new String(txt);
		System.out.println("(AES)Decrypted: " + text);
		
		RSA rsa = new RSA();
		rsa.generateKeyPair();
		byte[] ciphertxt = rsa.encrypt(msg.getBytes());
		byte[] plain = rsa.decrypt(ciphertxt);
		String ctext = new String(ciphertxt);
		System.out.println("(RSA)Encrypted: " + ctext);
		String plaint = new String(plain);
		System.out.println("(RSA)Decrypted: " + plaint);
		
	}

}
