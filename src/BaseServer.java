import java.util.*;


interface ServerInterface {
	void print(String filename, String printer) throws ServerNotStartedException;
	ArrayList<String> queue(String printer) throws ServerNotStartedException;
	boolean topQueue(String printer, int job) throws ServerNotStartedException;
	void start() throws ServerAlreadyStartedException;
	void stop() throws ServerNotStartedException;
	void restart() throws ServerNotStartedException;
	int status(String printer) throws ServerNotStartedException;
	String readConfig(String parameter) throws ServerNotStartedException;
	void setConfig(String parameter, String value) throws ServerNotStartedException;
}


public class BaseServer implements ServerInterface {

	private Map<String, ArrayList<String>> printers;
	private Map<String, String> parameters;
	private boolean started;

	public BaseServer() {
		this.printers = new HashMap<>();
		this.parameters = new HashMap<>();
		this.started = false;
	}


	public void print(String filename, String printer) throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		if (this.printers.get(printer) == null) {
			this.printers.put(printer, new ArrayList<String>());
		}
		this.printers.get(printer).add(filename);
		System.out.printf("Print: file %s in printer %s\n\n", filename, printer);
	}

	public ArrayList<String> queue(String printer) throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		ArrayList<String> result = this.printers.get(printer);
		if (result == null) {
			result = new ArrayList<String>();
		}
		System.out.printf("Queue: printer %s\n", printer);
		for (int i = 0; i < result.size(); i++) {
			System.out.printf("%d %s\n", i+1, result.get(i));
		}
		System.out.println();
		return result;
	}

	public boolean topQueue(String printer, int job) throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		ArrayList<String> queue = this.printers.get(printer);
		if (queue == null || queue.size() < job) {
			System.out.printf("TopQueue: job %d doesn't exist in printer "
					+ "%s\n\n", job, printer);
			return false;
		}
		String filename = queue.get(job-1);
		queue.remove(job-1);
		queue.add(0, filename);
		System.out.printf("TopQueue: job %d (%s) in printer %s moved to the "
				+ "top\n\n", job, filename, printer);
		return true;
	}

	public void start() throws ServerAlreadyStartedException {
		if (this.started) {
			throw new ServerAlreadyStartedException();
		}
		System.out.println("Start\n");
		this.started = true;
	}

	public void stop() throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		System.out.println("Stop\n");
		this.started = false;
		this.printers = new Hashtable<String, ArrayList<String>>();
		this.parameters = new Hashtable<String, String>();
	}

	public void restart() throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		System.out.println("Restart\n");
		this.printers = new Hashtable<String, ArrayList<String>>();
		this.parameters = new Hashtable<String, String>();
		this.started = true;
	}

	public int status(String printer) throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		int jobs = 0;
		ArrayList<String> queue = this.printers.get(printer);
		if (queue != null) {
			jobs = queue.size();
		}
		System.out.printf("Status: printer %s has %d jobs\n\n", printer, jobs);
		return jobs;
	}

	public String readConfig(String parameter) throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		String value = this.parameters.get(parameter);
		if (value == null) {
			value = "";
		}
		System.out.printf("ReadConfig: %s -> \"%s\"\n\n", parameter, value);
		return value;
	}

	public void setConfig(String parameter, String value) throws ServerNotStartedException {
		if (!this.started) {
			throw new ServerNotStartedException();
		}
		System.out.printf("SetConfig: %s -> \"%s\"\n\n", parameter, value);
		this.parameters.put(parameter, value);
	}
}
