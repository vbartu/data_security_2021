import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PrintServer extends BaseServer implements IPrintServer {
    private TokenChecker tokenChecker;
    private Authenticator authenticator;

    public PrintServer(){
        this.tokenChecker = new TokenChecker();
        this.authenticator = new Authenticator();
    }

    @Override
    public void print(String filename, String printer, String token) throws RemoteException, ServerNotStartedException {
        if(this.tokenChecker.checkToken(token)){
            super.print(filename, printer);
        }
    }

    @Override
    public ArrayList<String> queue(String printer, String token) throws RemoteException, ServerNotStartedException {
        if(this.tokenChecker.checkToken(token)) {
            return super.queue(printer);
        }
        return null;
    }

    @Override
    public boolean topQueue(String printer, int job, String token) throws RemoteException, ServerNotStartedException {
        if(this.tokenChecker.checkToken(token)){
            return super.topQueue(printer, job);
        }
		return false;
    }

    @Override
    public void start(String token) throws RemoteException, ServerAlreadyStartedException {
        if(this.tokenChecker.checkToken(token)){
            super.start();
        }
    }

    @Override
    public void stop(String token) throws RemoteException, ServerNotStartedException {
        if(this.tokenChecker.checkToken(token)){
            super.stop();
        }
    }

    @Override
    public void restart(String token) throws RemoteException, ServerNotStartedException {
        if(this.tokenChecker.checkToken(token)){
            super.restart();
        }
    }

    @Override
    public int status(String printer, String token) throws RemoteException, ServerNotStartedException {
        if(this.tokenChecker.checkToken(token)){
            return super.status(printer);
        }
        return 0;
    }

    @Override
    public String readConfig(String parameter, String token) throws RemoteException, ServerNotStartedException {
        if(this.tokenChecker.checkToken(token)){
            return super.readConfig(parameter);
        }
        return null;
    }

    @Override
    public void setConfig(String parameter, String value, String token) throws RemoteException, ServerNotStartedException {
        if(this.tokenChecker.checkToken(token)){
            super.setConfig(parameter, value);
        }
    }

    @Override
    public String startSession(String username, String password) throws RemoteException { // Returns session token
        if (this.authenticator.authenticate(username, password)) {
			return this.tokenChecker.newSession(username);
		}
		return null;
    }

    public static void main(String[] args){
        /*System.setProperty("java.security.policy", "server.policy");

        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }*/
        Registry registry = null;
        try {
            IPrintServer server = new PrintServer();
            IPrintServer stub = (IPrintServer) UnicastRemoteObject.exportObject(server, 0);
            registry = LocateRegistry.getRegistry();
            registry.bind("IPrintServer", stub);
			System.out.println("1");
			while (true);
        } catch (Exception e) {
			System.out.println("2");
            System.err.println("Print Server exception: ");
            e.printStackTrace();
        } finally {
			System.out.println("3");
			System.out.println("Exiting...");
			try {
				if (registry != null) {
					registry.unbind("IPrintServer");
				}
			} catch (Exception e) {}
		}
    }
}
