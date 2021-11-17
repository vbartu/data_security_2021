import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class PrintServer extends BaseServer implements IPrintServer {
	static final String LOG_FILE = "/tmp/print_server.log";
	static final String unauthUser = "UNAUTHENTICATED USER";

    private TokenChecker tokenChecker;
    private Authenticator authenticator;
	private AuthorizationService authorization;
	private FileWriter logfile;

    public PrintServer() {
        this.tokenChecker = new TokenChecker();
        this.authenticator = new Authenticator();
		this.authorization = new AuthorizationService();
		try {
			this.logfile = new FileWriter(LOG_FILE, true);
		} catch (Exception e) {
			;
		}
    }

	private void log(String info) {
		System.out.println(info);
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
			this.log(String.format("(%s) Print: file %s in printer %s",
						user, filename, printer, user));
			if (this.authorization.checkAccessRight(user, Right.PRINT)) {
				this.log(String.format("%s is authorized to Print", user));
				super.print(filename, printer);
			} else {
				this.log(String.format("%s is not authorized to Print", user));
			}
        } else {
			this.log(String.format("(%s) Print: file %s in printer %s",
						unauthUser, filename, printer, user));
        }
    }

    @Override
    public ArrayList<String> queue(String printer, String token) throws RemoteException, ServerNotStartedException {

		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("(%s) Queue: printer %s", user, printer));
			if (this.authorization.checkAccessRight(user, Right.QUEUE)) {
				this.log(String.format("%s is authorized to Queue", user));
				return super.queue(printer);
			} else {
				this.log(String.format("%s is not authorized to Queue", user));
			}
        } else {
			this.log(String.format("(%s) Queue: printer %s", unauthUser, printer));
		}
        return null;
    }

    @Override
    public boolean topQueue(String printer, int job, String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("(%s) TopQueue: job %d in printer %s", 
						user, job, printer));
			if (this.authorization.checkAccessRight(user, Right.TOP_QUEUE)) {
				this.log(String.format("%s is authorized to TopQueue", user));
				return super.topQueue(printer, job);
			} else {
				this.log(String.format("%s is not authorized to TopQueue", user));
			}
        } else {
			this.log(String.format("(%s) TopQueue: job %d in printer %s", 
						unauthUser, job, printer));
        }
		return false;
    }

    @Override
    public void start(String token) throws RemoteException, ServerAlreadyStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("(%s) Start", user));
			if (this.authorization.checkAccessRight(user, Right.START)) {
				this.log(String.format("%s is authorized to Start", user));
				super.start();
			} else {
				this.log(String.format("%s is not authorized to Stop", user));
			}
        } else {
			this.log(String.format("(%s) Start", unauthUser));
        }
    }

    @Override
    public void stop(String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("(%s) Stop", user));
			if (this.authorization.checkAccessRight(user, Right.STOP)) {
				this.log(String.format("%s is authorized to Stop", user));
				super.stop();
			} else {
				this.log(String.format("%s is not authorized to Stop", user));
			}
        } else {
			this.log(String.format("(%s) Stop", unauthUser));
        }
    }

    @Override
    public void restart(String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("(%s) Restart", user));
			if (this.authorization.checkAccessRight(user, Right.RESTART)) {
				this.log(String.format("%s is authorized to Restart", user));
				super.restart();
			} else {
				this.log(String.format("%s is not authorized to Restart", user));
			}
        } else {
			this.log(String.format("(%s) Restart", unauthUser));
        }
    }

    @Override
    public int status(String printer, String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("(%s) Status: printer %s",
						user, printer));
			if (this.authorization.checkAccessRight(user, Right.STATUS)) {
				this.log(String.format("%s is authorized to Status", user));
				return super.status(printer);
			} else {
				this.log(String.format("%s is not authorized to Status", user));
			}
        } else {
			this.log(String.format("(%s) Status: printer %s",
						unauthUser, printer));
        }
        return 0;
    }

    @Override
    public String readConfig(String parameter, String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("(%s) ReadConfig: %s", user, parameter));
			if (this.authorization.checkAccessRight(user, Right.READ_CONFIG)) {
				this.log(String.format("%s is authorized to ReadConfig", user));
				return super.readConfig(parameter);
			} else {
				this.log(String.format("%s is not authorized to ReadConfig", user));
			}
        } else {
			this.log(String.format("(%s) ReadConfig: %s", unauthUser, parameter));
        }
        return null;
    }

    @Override
    public void setConfig(String parameter, String value, String token) throws RemoteException, ServerNotStartedException {
		String user = this.tokenChecker.checkToken(token);
        if (user != null) {
			this.log(String.format("(%s) SetCofig: %s -> %s",
						user, parameter, value));
			if (this.authorization.checkAccessRight(user, Right.SET_CONFIG)) {
				this.log(String.format("%s is authorized to SetConfig", user));
				super.setConfig(parameter, value);
			} else {
				this.log(String.format("%s is not authorized to SetConfig", user));
			}
        } else {
			this.log(String.format("(%s) SetCofig: %s -> %s",
						unauthUser, parameter, value));
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
			System.out.println("Print Sever started!");
			while (true){
                Thread.sleep(1000);
                if(sc.nextLine().trim().equals("q")){
                    System.out.println("Exiting...");
                    registry.unbind("IPrintServer");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Print Server exception: ");
            e.printStackTrace();
        } finally {
			((PrintServer) server).close();
		}
    }
}
