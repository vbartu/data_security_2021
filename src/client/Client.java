package client;

import server.IPrintServer;
import server.PrintServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String [] args){
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Registry registry = LocateRegistry.getRegistry(args[0]);
            IPrintServer printserver = (PrintServer) registry.lookup("printserver");
            String token = printserver.startSession("user1", "user1");
            printserver.print("hello.txt", "printer1", token);
        }catch (Exception e){
            System.err.println("Client exception: ");
            e.printStackTrace();
        }
    }
}
