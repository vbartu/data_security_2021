import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class PrintServer extends BaseServer implements IPrintServer {
	static final String LOG_FILE = "/tmp/print_server.log";

    private TokenChecker tokenChecker;
    private Authenticator authenticator;
	private FileWriter logfile;

    public PrintServer() {
        this.tokenChecker = new TokenChecker();
        this.authenticator = new Authenticator();
		try {
			this.logfile = new FileWriter(LOG_FILE, true);
		} catch (Exception e) {
			;
		}
    }

	private void log(String info) {
		try {
			this.logfile.append(info + "\n");
			this.logfile.flush();
		} catch (Exception e) {
			;
		}
	}

	public void close() {
		try {
			this.logfile.close();
		} catch (Exception e) {
			;
		}
	}

    @Override
    public void print(String filename, String printer, String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("%s: print operation", user));
            super.print(filename, printer);
        } else {
			this.log("INVALID USER: print operation");
        }
    }

    @Override
    public ArrayList<String> queue(String printer, String token) throws RemoteException, ServerNotStartedException {

		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("%s: queue operation", user));
            return super.queue(printer);
        } else {
			this.log("INVALID USER: queue operation");
		}
        return null;
    }

    @Override
    public boolean topQueue(String printer, int job, String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("%s: topQueue operation", user));
            return super.topQueue(printer, job);
        } else {
			this.log("INVALID USER: topQueue operation");
        }
		return false;
    }

    @Override
    public void start(String token) throws RemoteException, ServerAlreadyStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("%s: start operation", user));
            super.start();
        } else {
			this.log("INVALID USER: start operation");
        }
    }

    @Override
    public void stop(String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("%s: stop operation", user));
            super.stop();
        } else {
			this.log("INVALID USER: stop operation");
        }
    }

    @Override
    public void restart(String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("%s: restart operation", user));
            super.restart();
        } else {
			this.log("INVALID USER: restart operation");
        }
    }

    @Override
    public int status(String printer, String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("%s: status operation", user));
            return super.status(printer);
        } else {
			this.log("INVALID USER: status operation");
        }
        return 0;
    }

    @Override
    public String readConfig(String parameter, String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("%s: readConfig operation", user));
            return super.readConfig(parameter);
        } else {
			this.log("INVALID USER: readConfig operation");
        }
        return null;
    }

    @Override
    public void setConfig(String parameter, String value, String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("%s: setCofig operation", user));
            super.setConfig(parameter, value);
        } else {
			this.log("INVALID USER: setCofig operation");
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
        Registry registry = null;
		IPrintServer server = new PrintServer();
        try(Scanner sc = new Scanner(System.in)) {
            IPrintServer stub = (IPrintServer) UnicastRemoteObject.exportObject(server, 0);
            registry = LocateRegistry.getRegistry();
            registry.bind("IPrintServer", stub);
			System.out.println("1");
			while (true){
                Thread.sleep(1000);
                if(sc.nextLine().trim().equals("q")){
                    System.out.println("Exiting...");
                    registry.unbind("IPrintServer");
                    break;
                }
            }
        } catch (Exception e) {
			System.out.println("2");
            System.err.println("Print Server exception: ");
            e.printStackTrace();
			((PrintServer) server).close();
        }
    }
}
