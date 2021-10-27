import java.util.*;
import java.security.MessageDigest;
import java.security.SecureRandom;


public class Authenticator {

	static final int HASH_LENGTH = 64; // 512 bits
	static final int SALT_LENGTH = 8;  // 64 bits

	private HashMap<String, byte[]> userPasswords;
	private MessageDigest md;
	private SecureRandom rd;

	public Authenticator() {
		try {
			this.md = MessageDigest.getInstance("SHA-512");
		} catch (Exception e) {
			;
		}
		this.rd = new SecureRandom();
		this.userPasswords = new HashMap<String, byte[]>();

		this.addUser("Bence", "password12");
		this.addUser("Nicolo", "strong_password");
		this.addUser("Vicente", "123");
	}

	private byte[] hashPassword(String password, byte[] salt) {
		byte[] bytePwd = password.getBytes();
		byte[] saltedPwd = new byte[bytePwd.length + salt.length];
		System.arraycopy(bytePwd, 0, saltedPwd, 0, bytePwd.length);
		System.arraycopy(salt, 0, saltedPwd, bytePwd.length, salt.length);

		byte[] pwdHash = this.md.digest(saltedPwd);
		
		byte[] pwdHashSalt = new byte[pwdHash.length + salt.length];
		System.arraycopy(pwdHash, 0, pwdHashSalt, 0, pwdHash.length);
		System.arraycopy(salt, 0, pwdHashSalt, pwdHash.length, salt.length);

		return pwdHashSalt;
	}

	private void addUser(String user, String password) {
		byte[] salt = new byte[SALT_LENGTH];
		this.rd.nextBytes(salt);

		byte[] hashedPassword = this.hashPassword(password, salt);
		this.userPasswords.put(user, hashedPassword);
	}

	private boolean checkPassword(String user, String password) {
		byte[] hash = this.userPasswords.get(user);
		if (hash == null) {
			return false;
		}

		//byte[] pwdHash = Arrays.copyOfRange(hash, 0, HASH_LENGTH);
		byte[] salt = Arrays.copyOfRange(hash, HASH_LENGTH, HASH_LENGTH + SALT_LENGTH);
		byte[] checkHash = this.hashPassword(password, salt);

		return this.md.isEqual(hash, checkHash);
	}

	public boolean authenticate(String username, String password) {
		return checkPassword(username, password);
	}
}
