/**
 * @Author Valentin Durand - ENSICAEN
 * @Project CaenBowServer
 * @Package server
 * @Class ConnexionClient
 * @ Jan 7, 2017 2:52:28 PM
 */
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.JSONObject;

public class ConnexionClient implements Runnable {
	private BufferedWriter fluxOut;
	private BufferedReader fluxIn;
	private Connexion serv;
	private Socket sock;
	
	/**
	 * 
	 */
	public ConnexionClient(Connexion serv, Socket sock) {
		this.serv = serv;
		this.sock = sock;
		try {
			fluxOut = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			fluxIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (IOException e) {
			System.out.println("Buffer error");
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		send("{\"ok\":1}\n");
		int newPort;
		int newId;
		Server newServ;
		Thread threadServ;
		try {
			JSONObject obj = new JSONObject(fluxIn.readLine());
			if(obj.has("create")){
				newPort = serv.getAvailablePort();
				newId = serv.getNewId();
				newServ = new Server(newPort, newId, serv);
				threadServ = new Thread(newServ);
				threadServ.start();
				serv.addServer(newServ);
				send("{\"port\":"+newPort+"}");
				sock.close();
			}
			if(obj.has("join")){
				send(serv.getServerList());
				sock.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void send(String message) {
		try {
			fluxOut.write(message);
			fluxOut.flush();
		} catch (IOException e) {
			System.out.println("Send error");
			e.printStackTrace();
		}
	}

}
