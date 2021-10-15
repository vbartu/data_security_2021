import java.security.SecureRandom;
import java.util.*;


class TokenInfo {
	public String user;
	public Date validity;

	public TokenInfo(String user) {
		this.user = user;
		this.validity = new Date(System.currentTimeMillis());
	}
}


public class TokenChecker {

	static final int TOKEN_LENGTH = 16; // 128 bits

	private HashMap<byte[], TokenInfo> sessions;
	private SecureRandom rd;

	public TokenChecker() {
		this.sessions = new HashMap<byte[], TokenInfo>();
		this.rd = new SecureRandom();
	}


	public String newSession(String user) {
		byte[] token = new byte[TOKEN_LENGTH];
		this.rd.nextBytes(token);
		this.sessions.put(token, new TokenInfo(user));

		return new String(token);
	}

	public boolean checkToken(String token) {
		byte[] tokenByte = token.getBytes();
		TokenInfo tokenInfo = this.sessions.get(token);
		if (tokenInfo == null) {
			return false;
		}

		Date now = new Date(System.currentTimeMillis());
		if (now.compareTo(tokenInfo.validity) > 0) {
			return false;
		}

		return true;
	}
}
