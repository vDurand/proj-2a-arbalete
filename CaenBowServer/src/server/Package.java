/**
 * @Author Valentin Durand - ENSICAEN
 * @Project ReseauJava.ServerDiscussion
 * @Package 
 * @Class Package
 * @ Dec 18, 2016 3:03:20 PM
 */
package server;

public class Package {
	private Delivery del;
	
	/**
	 * 
	 */
	public Package(Delivery del) {
		this.del = del;
	}
	
	public synchronized void deliver(String message) {
		del.ship(message);
		notifyAll();
	}
	
	public synchronized void deliver(String message, Client c) {
		del.ship(message,c);
		notifyAll();
	}
}
