public class main {

	public static void main(String[] args) throws Exception
	{
		
		String msg = "Hello";
		System.out.println("Original Msg: "+msg);
		myCrypto cipher = new myCrypto();
		byte[] secret = cipher.encrypt(msg.getBytes());
		cipher.decrypt(secret);
		System.out.println("END!");

		
		
	}

}
