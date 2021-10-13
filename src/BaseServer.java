import java.util.*;

interface ServerInterface {
	public void print(String filename, String printer);
	public ArrayList<String> queue(String printer);
	public void topQueue(String printer, int job);
	public void start();
	public void stop();
	public void restart();
	public String status(String printer);
	public String readConfig(String parameter);
	public void setConfig(String parameter, String value);
}


public class BaseServer implements ServerInterface {


	private Dictionary<String, ArrayList<String>> printers;
	private Dictionary<String, String> parameters;


	public void print(String filename, String printer) {
		if (this.printers.get(printer) == null) {
			this.printers.put(printer, ArrayList<String>());
		}
		this.printer.get(printer).add(filename);
	}

	public ArrayList<String> queue(String printer) {
		ArrayList<String> result = this.printers.get(printer);
		if (result == null) {
			return ArrayList<String>();
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
		this.printers = Hashtable<String, ArrayList<String>();
		this.parameters = Hashtable<String, String>();
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
		if (queue == null || queue.size() < job) {
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
