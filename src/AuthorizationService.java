import java.util.*;
import java.io.*;


public class AuthorizationService {
	static final String RIGHTS_FILE = "../policies/rights.csv";

	private HashMap<String,HashSet<Right>> userRights;

	public AuthorizationService() {
		this.userRights = new HashMap<String,HashSet<Right>>();
		this.loadPolicyFiles();
	}

	private void loadPolicyFiles() {
		try {
		    String line;

			BufferedReader br = new BufferedReader(new FileReader(RIGHTS_FILE));
		    while ((line = br.readLine()) != null) {
				if (line.equals("User,Permissions")) continue;
		        String[] values = line.split(",");
				HashSet<Right> rights = new HashSet<Right>();
				for (int i = 1; i < values.length; i++) {
					switch (values[i]) {
						case "print":
							rights.add(Right.PRINT);
							break;
						case "queue":
							rights.add(Right.QUEUE);
							break;
						case "topQueue":
							rights.add(Right.TOP_QUEUE);
							break;
						case "start":
							rights.add(Right.START);
							break;
						case "stop":
							rights.add(Right.STOP);
							break;
						case "restart":
							rights.add(Right.RESTART);
							break;
						case "status":
							rights.add(Right.STATUS);
							break;
						case "readConfig":
							rights.add(Right.READ_CONFIG);
							break;
						case "setConfig":
							rights.add(Right.SET_CONFIG);
							break;
					}
				}
				this.userRights.put(values[0], rights);
		    }
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkAccessRight(String user, Right right) {
		if (!this.userRights.containsKey(user)) {
			return false;
		}

		return this.userRights.get(user).contains(right);
	}
}
