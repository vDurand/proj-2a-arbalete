/**
 * @Author Valentin Durand - ENSICAEN
 * @Project ReseauJava.ServerDiscussion
 * @Package 
 * @Class Client
 * @ Dec 02, 2016 2:18:14 PM
 */
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import java.util.ArrayList;

import org.json.*;



public class Client implements Runnable{
	private BufferedWriter fluxOut;
	private BufferedReader fluxIn;
	private String id;
	private int score;
	private int scoreTotal;
	private int nbShot;
	private int nbRound;
	private int maxShot = 10;
	private Arrow arrow;
	private ArrayList<Arrow> arrows;
	private Server serv;
	private Socket sock;
	private boolean OK;
	private boolean alive;
	
	/**
	 * Init a player with server socket
	 */
	public Client(Server serv, Socket sock, boolean OK) {
		this.score = 0;
		this.scoreTotal = 0;
		this.nbShot = 0;
		this.nbRound = 1;
		arrows = new ArrayList<Arrow>();
		this.serv = serv;
		this.sock = sock;
		this.OK = OK;
		this.alive = true;
		try {
			fluxOut = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			fluxIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (IOException e) {
			System.out.println("Buffer error");
			e.printStackTrace();
		}
	}
	
	/**
	 * Init a player without socket for testing purposes
	 */
	public Client(String id) {
		score = 0;
		this.id = id;
		this.arrow = new Arrow(213,234);
	}
	
	@Override
	public void run() {
		if(OK){
			send("{\"ok\":1}\n");
			try {
				JSONObject obj = new JSONObject(fluxIn.readLine());
				if(obj.has("pseudo")){
					while(!serv.checkIdAvailability(obj.getString("pseudo"))){
						send("{\"available\":0}\n");
						obj = new JSONObject(fluxIn.readLine());
					}
					setId(obj.getString("pseudo"));
						
					System.out.print(id+"\n");
				}
				if(obj.has("exit")){
					serv.rmClient(this);
					alive = false;
					System.out.println("- new deconnection : "+id);
					serv.getPackage().deliver("{\"disconnected\":\""+id+"\"}\n");
					sock.close();
					if(serv.empty()){
						serv.kill();
						//Server.main(null);
					}
				}
				if(alive){
					if(obj.has("maxround")){
						serv.setMaxRound(obj.getInt("maxround"));
						if(obj.has("windlevel")){
							serv.setWindForce(obj.getInt("maxround"),obj.getInt("windlevel"));
						}
						if(obj.has("distancelevel")){
							serv.setTargetDistance(obj.getInt("maxround"),obj.getInt("distancelevel"));
						}
						if(obj.has("gamename")){
							serv.setGameName(obj.getString("gamename"));
						}
					}
					else{
						send(serv.getMaxRound()+"\n");
						send(serv.clientToString(this)+"\n");
					}
					serv.getPackage().deliver(this.toString() + "\n", this);
					send("{\"wind\":{\"x\":"+serv.getWindForceXRound(nbRound-1)
							+",\"y\":"+serv.getWindForceYRound(nbRound-1)+"}, \"distance\":"
							+serv.getDistanceRound(nbRound-1)+"}\n");
				}
			} catch (IOException e1) {
				System.out.println("Username error");
				e1.printStackTrace();
			}
			while(alive){
				try {
					JSONObject obj = new JSONObject(fluxIn.readLine());
					if(obj.has("exit")){
						serv.rmClient(this);
						alive = false;
						serv.getPackage().deliver("{\"disconnected\":\""+id+"\"}\n");
						sock.close();
						System.out.println("- new deconnection : "+id);
						if(serv.empty()){
							serv.kill();
							//Server.main(null);
						}
						break;
					}
					else{
						score = obj.getInt("score");
						scoreTotal += score;
						
						nbShot++;
						if(obj.has("arrow")){
							/*for(int i = 0; i < obj.getJSONArray("arrows").length(); i++){
								arrows.add(
										new Arrow(
												obj.getJSONArray("arrows").getJSONObject(i).getInt("x"),
												obj.getJSONArray("arrows").getJSONObject(i).getInt("y")
												)
										);
							}*/
							arrow = new Arrow(obj.getJSONObject("arrow").getDouble("x"),
									obj.getJSONObject("arrow").getDouble("y"));
							arrow.setPoint(score);
							arrows.add(arrow);
						}
						serv.getPackage().deliver(this.toString()+"\n",this);
						if(nbShot == maxShot){
							if(alive){
								nbShot = 0;
								nbRound++;
								if(nbRound <= serv.getMaxRoundInt()){
									System.out.println("nextround");
									send("{\"wind\":{\"x\":"+serv.getWindForceXRound(nbRound-1)
											+",\"y\":"+serv.getWindForceYRound(nbRound-1)+"}, \"distance\":"
											+serv.getDistanceRound(nbRound-1)+"}\n");
								}
								else{
									System.out.println("endround");
									serv.updateRound();
									break;
								}
							}
						}
					}
				} catch (IOException e) {
					System.out.println("package not delivered");
					e.printStackTrace();
				}
			}
		}
		else{
			send("{\"ok\":0}\n");
			try {
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public int getScore(){
		return scoreTotal;
	}
	
	/**
	 * @param alive the alive to set
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	/**
	 * @return the sock
	 */
	public Socket getSock() {
		return sock;
	}
	
	/**
	 * @return the nbRound
	 */
	public int getNbRound() {
		return nbRound;
	}
	
	@Override
	public String toString() {
		String res = "{\"pseudo\":\"" + id + "\",\"score\":"+String.valueOf(score)+",\"arrow\":";
		/*for(int i = 0; i < arrows.size(); i++){
			res += "{\"x\":" + arrows.get(i).getX() + ",\"y\":" + arrows.get(i).getY() + "}";
			if(i != arrows.size() - 1){
				res += ",";
			}
		}*/
		if(arrow != null)
			res += "{\"x\":" + arrow.getX() + ",\"y\":" + arrow.getY() + "}";
		else
			res += "{}";
		res += "}";
		return res;
	}
	
	public String toStringInit() {
		String res = "{\"pseudo\":\"" + id + "\",\"score\":"+String.valueOf(score)+",\"arrows\":[";
		for(int i = 0; i < arrows.size(); i++){
			res += "{\"x\":" + arrows.get(i).getX() 
					+ ",\"y\":" + arrows.get(i).getY() 
					+",\"points\":" + arrows.get(i).getPoint() + "}";
			if(i != arrows.size() - 1){
				res += ",";
			}
		}
		res += "]";
		res += "}";
		return res;
	}
}
