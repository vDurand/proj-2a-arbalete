/**
 * @Author Valentin Durand - ENSICAEN
 * @Project ReseauJava.ServerDiscussion
 * @Package 
 * @Class Server
 * @ Dec 02, 2016 1:54:44 PM
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;


public class Server implements Runnable {
	private ServerSocket sockServ;
	private ArrayList<Client> listCli;
	private int maxClient = 4;
	private int nbClient;
	private int maxRound;
	private ArrayList<Integer> windforceX;
	private ArrayList<Integer> windforceY;
	private ArrayList<Integer> targetDistance;
	private boolean over;
	private Package pack;
	private boolean online;
	private int minWind;
	private int maxWind;
	private String gameName;
	private int gameId;
	private int port;
	private Connexion originServer;
	
	/**
	 * 
	 */
	public Server(int port, int gameId, Connexion originServer) {
		listCli = new ArrayList<Client>();
		windforceX = new ArrayList<Integer>();
		windforceY = new ArrayList<Integer>();
		targetDistance = new ArrayList<Integer>();
		pack = new Package(new Delivery(this));
		this.port = port;
		this.gameId = gameId;
		this.originServer = originServer;
	}
	
	@Override
	public void run() {
		online = true;
		nbClient = 1;
		over = false;
		try {
			sockServ = new ServerSocket(port);
			System.out.println("server started");
			while(online){
				if(nbClient <= maxClient){
					Socket sockCli = sockServ.accept();
					Client cli = new Client(this, sockCli, true);
					listCli.add(cli);
					nbClient++;
					System.out.print("- new connection : ");
					Thread threadCli = new Thread(cli);
					threadCli.start();
				}
				else{
					Socket sockCli = sockServ.accept();
					Client cli = new Client(this, sockCli, false);
					Thread threadCli = new Thread(cli);
					threadCli.start();
				}
			}
		} catch (IOException e) {
			if(online){
				System.out.println("Port busy : 2014");
				e.printStackTrace();
			}
			else{
				System.out.println("server closed");
			}
		}
		
	}
	
	public ArrayList<Client> whosOnline(){
		return listCli;
	}
	
	public Package getPackage(){
		return pack;
	}
	public void rmClient(Client id){
		listCli.remove(listCli.indexOf(id));
		nbClient--;
	}
	public boolean empty(){
		return listCli.isEmpty();
	}
	public void kill(){
		System.out.println("closing...");
		online = false;
		try {
			sockServ.close();
		} catch (IOException e) {
			System.out.println("closing error");
			e.printStackTrace();
		}
		originServer.removeServer(this);
	}
	
	public String clientToString(){
		String a = "";
		if(listCli.size()<2){
			a = "[]";
		}
		else{
			a = "{\"players\":[";
			for (int i=0; i < listCli.size()-1; i++){
				//a += "{pseudo:\"" + listCli.get(i).getId() + "\",score:"+listCli.get(i).getScore()+",arrows:[]}";
				a += listCli.get(i).toStringInit();
				if(i != listCli.size()-2){
					a += ",";
				}
			}
			a += "]}";
		}
		return a;
	}
	public String clientToString(Client c){
		String a = "";
		if(listCli.size()<2){
			a = "[]";
		}
		else{
			a = "{\"players\":[";
			for (int i=0; i < listCli.size()-1; i++){
				//a += "{pseudo:\"" + listCli.get(i).getId() + "\",score:"+listCli.get(i).getScore()+",arrows:[]}";
				if(listCli.get(i) != c){
					a += listCli.get(i).toStringInit();
					if(i != listCli.size()-2){
						a += ",";
					}
				}
			}
			a += "]}";
		}
		return a;
	}
	
	/**
	 * @param maxRound the maxRound to set
	 */
	public void setMaxRound(int maxRound) {
		this.maxRound = maxRound;
	}
	
	/**
	 * @return the maxRound
	 */
	public String getMaxRound() {
		return "{\"maxround\":"+maxRound+"}";
	}
	
	public int getMaxRoundInt() {
		return maxRound;
	}
	
	public void updateRound() {
		over = true;
		for (int i=0; i < listCli.size(); i++){
			if(listCli.get(i).getNbRound() < maxRound+1){
				over = false;
				return;
			}
		}
		if(over){
			pack.deliver(this.getResults()+"\n");
			pack.deliver("{\"over\":1}\n");
			while(listCli.size() > 0){
				try {
					System.out.println("ending");
					listCli.get(0).setAlive(false);
					listCli.get(0).getSock().close();
					rmClient(listCli.get(0));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			kill();
			//main(null);
		}
	}
	
	public boolean isOver() {
		return over;
	}
	
	public String getResults() {
		Collections.sort(listCli, new Comparator<Client>() {
	        @Override public int compare(Client p1, Client p2) {
	            return p1.getScore() - p2.getScore(); // Ascending
	        }

	    });
		String a = "{\"result\":[";
		int position = 1;
		for (int i=listCli.size()-1; i > -1; i--){
			a += "{\"position\":"+position
					+",\"pseudo\":\""+listCli.get(i).getId()
					+"\",\"score\":"+listCli.get(i).getScore()
					+"}";
			if(i != 0){
				a += ",";
			}
			position++;
		}
		a += "]}";
		return a;
	}
	
	public void setWindForce(int nbRound, int windLevel) {
		int randomNum;
		
		if(windLevel == 0){
			for(int i = 0; i< nbRound; i++){
				windforceX.add(0);
				windforceY.add(0);
			}
		}
		else if(windLevel == 1){
			minWind = 1;
			maxWind = 15;
		}
		else if(windLevel == 2){
			minWind = 16;
			maxWind = 45;
		}
		else if(windLevel == 3){
			minWind = 46;
			maxWind = 70;
		}
		
		for(int i = 0; i< nbRound; i++){
			randomNum = ThreadLocalRandom.current().nextInt(minWind, maxWind + 1);
			windforceX.add(randomNum);
			randomNum = ThreadLocalRandom.current().nextInt(minWind, maxWind + 1);
			windforceY.add(randomNum);
		}
	}
	
	public void setTargetDistance(int nbRound, int distanceLevel) {
		int randomNum;
		int minDist = 0, maxDist = 0;
		
		if(distanceLevel == 0){
			minDist = 15;
			maxDist = 50;
		}
		else if(distanceLevel == 1){
			minDist = 51;
			maxDist = 125;
		}
		else if(distanceLevel == 2){
			minDist = 126;
			maxDist = 250;
		}
		
		for(int i = 0; i< nbRound; i++){
			randomNum = ThreadLocalRandom.current().nextInt(minDist, maxDist + 1);
			targetDistance.add(randomNum);
		}
	}
	
	public int getDistanceRound(int round) {
		return targetDistance.get(round);
	}
	
	/**
	 * @param minWind the minWind to set
	 */
	public void setMinWind(int minWind) {
		this.minWind = minWind;
	}
	
	/**
	 * @param maxWind the maxWind to set
	 */
	public void setMaxWind(int maxWind) {
		this.maxWind = maxWind;
	}
	
	public int getWindForceXRound(int round) {
		return windforceX.get(round);
	}
	public int getWindForceYRound(int round) {
		return windforceY.get(round);
	}
	
	public boolean checkIdAvailability(String id) {
		for (int i=0; i < listCli.size()-1; i++){
			if(listCli.get(i).getId().equals(id)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param gameName the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}
	
	/**
	 * @return the gameId
	 */
	public int getGameId() {
		return gameId;
	}
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	public int getNbPlayer() {
		return nbClient;
	}
}