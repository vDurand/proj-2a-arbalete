/**
 * @Author Valentin Durand - ENSICAEN
 * @Project CaenBowServer
 * @Package server
 * @Class Connexion
 * @ Jan 5, 2017 12:43:55 PM
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Connexion implements Runnable {
	private ServerSocket sockServ;
	private ArrayList<Server> serverList;
	private int availablePort;
	
	/**
	 * 
	 */
	public Connexion() {
		serverList = new ArrayList<Server>();
		availablePort = 2015;
	}
	
	public static void main(String[] args) {
		System.out.println("starting...");
		Thread threadServ = new Thread(new Connexion());
		threadServ.start();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			sockServ = new ServerSocket(2014);
			System.out.println("init server started");
			while(true){
				Socket sockCli = sockServ.accept();
				ConnexionClient cli = new ConnexionClient(this, sockCli);
				Thread threadCli = new Thread(cli);
				threadCli.start();
			}
		
		} catch (IOException e) {
				System.out.println("server closed");
		}
	}
	
	public int getNewId() {
		return serverList.size();
	}
	
	public void addServer(Server s) {
		serverList.add(s);
		availablePort++;
		if(availablePort == 3014)
			availablePort = 2015;
	}
	
	public void removeServer(Server s) {
		serverList.remove(s);
	}
	
	public int getServerPort(int id) {
		for(int i = 0; i < serverList.size(); i++){
			if(serverList.get(i).getGameId() == id){
				return serverList.get(i).getPort();
			}
		}
		return 0;
	}
	
	/**
	 * @return the availablePort
	 */
	public int getAvailablePort() {
		return availablePort;
	}
	
	public String getServerList(){
		String a = "{";
		if(serverList.size() == 0){
			a += "\"empty\":1";
		}
		else{
			a += "\"games\":[";
			for(int i = 0; i < serverList.size(); i++){
				a += "{";
				a += "\"id\":"+serverList.get(i).getGameId();
				a += ",\"name\":\""+serverList.get(i).getGameName();
				a += "\",\"nbplayer\":"+(serverList.get(i).getNbPlayer()-1);
				a += ",\"port\":"+serverList.get(i).getPort();
				a += "}";
				if(i != serverList.size()-1){
					a += ",";
				}
			}
			a += "]";
		}
		a+="}\n";
		return a;
	}
}