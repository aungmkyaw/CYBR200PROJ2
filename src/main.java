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
		System.out.println("Encrypted: " + cipherText);
		System.out.println("Decrypted: " + txt);
		
		RSA rsa = new RSA();
		rsa.generateKeyPair();
		byte[] ciphertxt = rsa.encrypt(msg.getBytes());
		byte[] plain = rsa.decrypt(ciphertxt);
		System.out.println("Encrypted: " + ciphertxt);
		System.out.println("Decrypted: " + plain);
		
	}

}
