import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PrintServer extends BaseServer implements IPrintServer {

    @Override
    public void print(String filename, String printer, String token) throws RemoteException {

    }

    @Override
    public ArrayList<String> queue(String printer, String token) throws RemoteException {
        return null;
    }

    @Override
    public void topQueue(String printer, int job, String token) throws RemoteException {

    }

    @Override
    public void start(String token) throws RemoteException {

    }

    @Override
    public void stop(String token) throws RemoteException {

    }

    @Override
    public void restart(String token) throws RemoteException {

    }

    @Override
    public String status(String printer, String token) throws RemoteException {
        return null;
    }

    @Override
    public String readConfig(String parameter, String token) throws RemoteException {
        return null;
    }

    @Override
    public void setConfig(String parameter, String value, String token) throws RemoteException {

    }

    @Override
    public void authenticate(String username, String password) throws RemoteException {

    }
    public static void main(String[] args){
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }
        try {
            IPrintServer server = new PrintServer();
            IPrintServer stub = (IPrintServer) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("printserver", stub);
        }catch (Exception e){
            System.err.println("Print Server exception: ");
            e.printStackTrace();
        }
    }
}
