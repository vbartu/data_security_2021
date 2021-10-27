import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Client {

    public static void main(String [] args){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            IPrintServer printserver = (IPrintServer) registry.lookup("IPrintServer");
            String token = printserver.startSession("Bence", "password12");
			if (token == null) {
				System.out.println("Invalid user/password");
				return;
			}
            printserver.print("hello.txt", "printer1", token);
        } catch (Exception e) {
            System.err.println("Client exception: ");
            e.printStackTrace();
        }
    }

    public static void main1 (String [] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            IPrintServer printserver = (IPrintServer) registry.lookup("IPrintServer");
			run_client();
        } catch (Exception e) {
            System.err.println("Client exception: ");
            e.printStackTrace();
        }
    }



	public static void run_client() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome to print server!");
		System.out.print("User: ");
		String user = sc.nextLine();
		System.out.print("Password: ");
		String password = sc.nextLine();
		System.out.printf("%s, %s\n", user, password);
	}
}
