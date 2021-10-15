import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
}
