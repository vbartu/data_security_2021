import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String [] args){
        /*System.setProperty("java.security.policy", "server.policy");
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }*/
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            IPrintServer printserver = (IPrintServer) registry.lookup("IPrintServer");
            String token = printserver.startSession("user1", "user1");
            printserver.print("hello.txt", "printer1", token);
        }catch (Exception e){
            System.err.println("Client exception: ");
            e.printStackTrace();
        }
    }
}
