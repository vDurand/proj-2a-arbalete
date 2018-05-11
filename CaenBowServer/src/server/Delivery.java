/**
 * @Author Valentin Durand - ENSICAEN
 * @Project ReseauJava.ServerDiscussion
 * @Package 
 * @Class Delivery
 * @ Dec 02, 2016 3:03:29 PM
 */
package server;

public class Delivery {
	private Server serv;
	
	/**
	 * 
	 */
	public Delivery(Server serv) {
		this.serv = serv;
	}
	
	public void ship(String message) {
		for (int i=0; i < serv.whosOnline().size(); i++){
			serv.whosOnline().get(i).send(message);
		}
	}
	
	public void ship(String message, Client c) {
		for (int i=0; i < serv.whosOnline().size(); i++){
			if(serv.whosOnline().get(i)!=c)
				serv.whosOnline().get(i).send(message);
		}
	}
}
