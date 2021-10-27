import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IPrintServer extends Remote {
    void print(String filename, String printer, String token) throws RemoteException;
    ArrayList<String> queue(String printer, String token) throws RemoteException;
    boolean topQueue(String printer, int job, String token) throws RemoteException;
    void start(String token) throws RemoteException;
    void stop(String token) throws RemoteException;
    void restart(String token) throws RemoteException;
    int status(String printer, String token) throws RemoteException;
    String readConfig(String parameter, String token) throws RemoteException;
    void setConfig(String parameter, String value, String token) throws RemoteException;
    String startSession(String username, String password) throws RemoteException;
}
