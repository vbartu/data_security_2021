import java.util.*;

interface ServerInterface {
	void print(String filename, String printer);
	ArrayList<String> queue(String printer);
	boolean topQueue(String printer, int job);
	void start();
	void stop();
	void restart();
	int status(String printer);
	String readConfig(String parameter);
	void setConfig(String parameter, String value);
}


public class BaseServer implements ServerInterface {

	private Map<String, ArrayList<String>> printers;
	private Map<String, String> parameters;

	public BaseServer() {
		this.printers = new HashMap<>();
		this.parameters = new HashMap<>();
	}


	public void print(String filename, String printer) {
		if (this.printers.get(printer) == null) {
			this.printers.put(printer, new ArrayList<String>());
		}
		this.printers.get(printer).add(filename);
		System.out.printf("Print: file %s in printer %s\n\n", filename, printer);
	}

	public ArrayList<String> queue(String printer) {
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

	public boolean topQueue(String printer, int job) {
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

	public void start() {
		this.printers = new Hashtable<String, ArrayList<String>>();
		this.parameters = new Hashtable<String, String>();
	}

	public void stop() {
		this.printers = null;
		this.parameters = null;
	}

	public void restart() {
		this.stop();
		this.start();
	}

	public int status(String printer) {
		int jobs = 0;
		ArrayList<String> queue = this.printers.get(printer);
		if (queue != null) {
			jobs = queue.size();
		}
		System.out.printf("Status: printer %s has %d jobs\n\n", printer, jobs);
		return jobs;
	}

	public String readConfig(String parameter) {
		String value = this.parameters.get(parameter);
		if (value == null) {
			value = "";
		}
		System.out.printf("ReadConfig: %s -> \"%s\"\n\n", parameter, value);
		return value;
	}

	public void setConfig(String parameter, String value) {
		System.out.printf("SetConfig: %s -> \"%s\"\n\n", parameter, value);
		this.parameters.put(parameter, value);
	}
}
