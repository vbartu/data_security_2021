import java.util.*;

interface ServerInterface {
	void print(String filename, String printer);
	ArrayList<String> queue(String printer);
	void topQueue(String printer, int job);
	void start();
	void stop();
	void restart();
	String status(String printer);
	String readConfig(String parameter);
	void setConfig(String parameter, String value);
}


public class BaseServer implements ServerInterface {

	private Map<String, ArrayList<String>> printers;
	private Map<String, String> parameters;

	void BaseServer(){
		this.printers = new HashMap<>();
		this.parameters = new HashMap<>();
			
	}


	public void print(String filename, String printer) {
		if (this.printers.get(printer) == null) {
			this.printers.put(printer, new ArrayList<String>());
		}
		this.printers.get(printer).add(filename);
	}

	public ArrayList<String> queue(String printer) {
		ArrayList<String> result = this.printers.get(printer);
		if (result == null) {
			return new ArrayList<String>();
		}
		return result;
	}

	public void topQueue(String printer, int job) {
		ArrayList<String> queue = this.printers.get(printer);
		if (queue == null || queue.size() < job) {
			return;
		}
		String filename = queue.get(job-1);
		queue.remove(job-1);
		queue.add(0, filename);
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

	public String status(String printer) {
		ArrayList<String> queue = this.printers.get(printer);
		if (queue == null || queue.isEmpty()) {
			return String.format("Printer %s has no jobs", printer);
		}
		return String.format("Printer %s has %d jobs", printer, queue.size());
	}

	public String readConfig(String parameter) {
		String value = this.parameters.get(parameter);
		if (value == null) {
			return "";
		}
		return value;
	}

	public void setConfig(String parameter, String value) {
		this.parameters.put(parameter, value);
	}
}
