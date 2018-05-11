/**
 * @Author Valentin Durand - ENSICAEN
 * @Project CaenBowServer
 * @Package server
 * @Class AddClientException
 * @ Dec 8, 2016 11:21:56 AM
 */
package server;

public class AddClientException extends Exception { 
	/**
	 * 
	 */
	private static final long serialVersionUID = -270649175035431281L;

	public AddClientException(String message){
		super(message);
	}
}