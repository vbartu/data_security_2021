import java.util.*;
import java.io.*;


public class AuthorizationService {
	static final String USER_FILE = "../policies/user-roles.csv";
	static final String ROLES_FILE = "../policies/role-pem.csv";

	private HashMap<String,List<String>> userRoles;
	private HashMap<String,Set<Right>> roleRights;

	public AuthorizationService() {
		this.userRoles = new HashMap<String,List<String>>();
		this.roleRights = new HashMap<String,Set<Right>>();
		this.loadPolicyFiles();
	}

	private void loadPolicyFiles() {
		try {
		    String line;

			BufferedReader br = new BufferedReader(new FileReader(USER_FILE));
		    while ((line = br.readLine()) != null) {
				if (line.equals("User,Role")) continue;
		        String[] values = line.split(",");

				ArrayList<String> roles = new ArrayList<String>();
				for (int i = 1; i < values.length; i++) {
					roles.add(values[i]);
				}
				this.userRoles.put(values[0], roles);
		    }

			br.close();
			br = new BufferedReader(new FileReader(ROLES_FILE));
		    while ((line = br.readLine()) != null) {
				if (line.equals("Role,Permissions")) continue;
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
				this.roleRights.put(values[0], rights);
		    }
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkAccessRight(String user, Right right) {
		List<String> roles = this.userRoles.get(user);
		if (roles == null) {
			return false;
		}

		for (String role : roles) {
			if (!this.roleRights.containsKey(role)) {
				continue;
			}

			if (this.roleRights.get(role).contains(right)) {
				return true;
			}
		}
		return false;
	}
}
