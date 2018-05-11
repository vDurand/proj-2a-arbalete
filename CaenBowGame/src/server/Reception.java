/**
 * @Author Valentin Durand - ENSICAEN
 * @Project CaenBowClient
 * @Package client
 * @Class Reception
 * @ Jan 4, 2017 1:37:45 PM
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import player.Joueur;


public class Reception implements Runnable {

	private BufferedReader in;
	private Connexion c;
	
	public Reception(BufferedReader in, Connexion c){
		this.c = c;
		this.in = in;
	}
	
	public void run() {
		
		while(!c.getGameOver()){
	        try {
	        	JSONObject obj = new JSONObject(in.readLine());
	        	if(obj.has("pseudo") && obj.getJSONObject("arrow").has("x")){
	        		System.out.println("New shot by "+obj.getString("pseudo")
	        				+" : "+obj.getJSONObject("arrow").getDouble("x")
	        				+";"+obj.getJSONObject("arrow").getDouble("y")
	        				+" (score : "+obj.getInt("score")+")");
                            c.arrowAdversary(obj.getString("pseudo"),
                                    obj.getJSONObject("arrow").getDouble("x"),
                                    obj.getJSONObject("arrow").getDouble("y"),
                                    obj.getInt("score"));
	        	}
	        	else if(obj.has("pseudo")){
	        		System.out.println("New user : "+obj.getString("pseudo"));
                                c.newAdversary(obj.getString("pseudo"));
	        	}
	        	if(obj.has("wind")){
	        		System.out.println("Next round with wind ("+obj.getJSONObject("wind").getInt("x")
	        				+","+obj.getJSONObject("wind").getInt("y")+" and distance "
	        				+obj.getInt("distance")+")");
	        		c.setCurrentRoundTargetDistance(obj.getInt("distance"));
	        		c.setCurrentRoundWindX(obj.getJSONObject("wind").getInt("x"));
	        		c.setCurrentRoundWindY(obj.getJSONObject("wind").getInt("y"));
                                c.newRound((double)obj.getInt("distance"),
                                        obj.getJSONObject("wind").getDouble("x"),
                                        obj.getJSONObject("wind").getDouble("y"));
	        	}
	        	if(obj.has("result")){
	        		System.out.println("Game finished");
	        		JSONArray players = obj.getJSONArray("result");
                                ArrayList<Joueur> classement = new ArrayList<Joueur>();
	        		for(int i = 0; i < players.length(); i++){
                                    Joueur j = new Joueur(players.getJSONObject(i).getString("pseudo"));
                                    j.setScore(players.getJSONObject(i).getInt("score"));
                                    classement.add(j);
	        			System.out.println(i+1+" : "+players.getJSONObject(i).getString("pseudo")
	        					+" with score "+players.getJSONObject(i).getInt("score"));
	        		}
                                c.setClassement(classement);
	        		c.setGameOver(true);
	        	}
	        	if(obj.has("disconnected")){
	        		System.out.println(obj.getString("disconnected")+" left");
                                c.adversaryLeft(obj.getString("disconnected"));
	        	}
		    } catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}

}
