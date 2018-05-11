/**
 * @Author Valentin Durand - ENSICAEN
 * @Project CaenBowClient
 * @Package client
 * @Class Connexion
 * @ Jan 4, 2017 1:37:14 PM
 */
package server;

import vue.debutPartie.MultiJoinViewController;
import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import outils.Fleche;
import player.Joueur;
import modele.PartieMulti;

public class Connexion implements Runnable {

    private Socket socket = null;
    public static Thread t2;
    private String login = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Scanner sc = null;
    private double currentRoundWindX, currentRoundWindY, currentRoundTargetDistance;
    private int maxRound, windLevel, targetDistanceLevel;
    private boolean gameOver;
    private String choice;
    private boolean connected;
    private Emission emission;
    private Reception reception;
    private PartieMulti partie;

    public Connexion(Socket s, String choice, Scanner sc){
        gameOver = false;
        socket = s;
        this.choice = choice;
        this.sc = sc;
    }
    
    public void sendShot(int score, double x, double y){
        emission.sendArrow(score, x, y);
    }
    
    public void setPartie(PartieMulti p)
    {
        partie = p;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public void createPartie(String pseudo, String gameName, int nbRound, int windLevel, int targetDistance) {
        try {
            JSONObject obj;
            if(connected){
                if(choice.equals("1")){
                    login = pseudo;
                    System.out.println(">----{\"pseudo\":\""+login+"\",\"gamename\":\""+gameName+"\",\"maxround\":"
                                    +nbRound+",\"windlevel\":"+windLevel+",\"distancelevel\":"
                                    +targetDistance+"}");
                    out.flush();
                    out.println("{\"pseudo\":\""+login+"\",\"gamename\":\""+gameName+"\",\"maxround\":"
                                    +nbRound+",\"windlevel\":"+windLevel+",\"distancelevel\":"
                                    +targetDistance+"}");
                    out.flush();

                    obj = new JSONObject(in.readLine());
                    System.out.println("---->"+obj);
                    if(obj.has("wind") && obj.has("distance")){
                        currentRoundWindX = obj.getJSONObject("wind").getInt("x");
                        currentRoundWindY = obj.getJSONObject("wind").getInt("y");
                        currentRoundTargetDistance = obj.getInt("distance");
                        System.out.println("Wind : "+currentRoundWindX+";"+currentRoundWindY);
                        System.out.println("Distance : "+currentRoundTargetDistance);
                    }
                    init_client startEmRe = new init_client(socket,this);
                    t2 = new Thread(startEmRe);
                    System.out.println("Game begins");
                    t2.start();
                    //emission = startEmRe.getEmission();
                    //reception = startEmRe.getReception();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean checkAvailability(MultiJoinViewController controller){
        JSONObject obj;
        boolean available = false;
        if(connected){
            if(choice.equals("2")){
                out.flush();
                out.println("{\"pseudo\":\""+login+"\"}");
                out.flush();
                
                try {

                    obj = new JSONObject(in.readLine());
                    if(obj.has("available")){
                        controller.notifyPseudoTaken();
                    }
                    if(obj.has("maxround")){
                        maxRound = obj.getInt("maxround");
                        available = true;
                    }
                    
                } catch (IOException e) {

                    System.err.println("Le serveur ne répond plus ");
                }
            }
        }
        else{
            System.out.println("Failed connection");
        }
        return available;
    }
    
    public void joinGame(){
        JSONObject obj;
        try {
            obj = new JSONObject(in.readLine());
            System.out.println("---->"+obj);
            if(obj.has("players")){
                System.out.println("Current players:");
                JSONArray players = obj.getJSONArray("players");
                for(int i = 0; i < players.length(); i++){
                    Joueur player = new Joueur(players.getJSONObject(i).getString("pseudo"));
                    player.setScore(players.getJSONObject(i).getInt("score"));
                    System.out.println(players.getJSONObject(i).getString("pseudo")
                                    +" with score "+players.getJSONObject(i).getInt("score"));
                    if(players.getJSONObject(i).has("arrows")){
                        JSONArray arrowsOfPlayer = players.getJSONObject(i).getJSONArray("arrows");
                        for(int j = 0; j < arrowsOfPlayer.length(); j++){
                            Fleche a = new Fleche(arrowsOfPlayer.getJSONObject(j).getInt("x"), 
                                    arrowsOfPlayer.getJSONObject(j).getInt("y"), 
                                    arrowsOfPlayer.getJSONObject(j).getInt("points"));
                            player.addFleche(a);
                            System.out.println("with arrows: "+
                            arrowsOfPlayer.getJSONObject(j).getInt("x")+";"+
                            arrowsOfPlayer.getJSONObject(j).getInt("y")+" (points: "+
                            arrowsOfPlayer.getJSONObject(j).getInt("points")+")"
                            );
                        }
                    }
                    partie.addAdversary(player);
                }
            }

            obj = new JSONObject(in.readLine());
            System.out.println("---->"+obj);
            if(obj.has("wind") && obj.has("distance")){
                currentRoundWindX = obj.getJSONObject("wind").getDouble("x");
                currentRoundWindY = obj.getJSONObject("wind").getDouble("y");
                currentRoundTargetDistance = (double)obj.getInt("distance");
            }
            System.out.println("Wind : "+currentRoundWindX+";"+currentRoundWindY);
            System.out.println("Distance : "+currentRoundTargetDistance);

            init_client startEmRe = new init_client(socket,this);
            t2 = new Thread(startEmRe);
            System.out.println("Game begins");
            t2.start();
            //emission = startEmRe.getEmission();
            //reception = startEmRe.getReception();
    } catch (IOException e) {

        System.err.println("Le serveur ne répond plus ");
    }
    }
    

    public void run() {

        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));	

            connected = false;

            JSONObject obj = new JSONObject(in.readLine());
            if(obj.has("ok")){
                if(obj.getInt("ok") == 1)
                    connected = true;
            }
            
        } catch (IOException e) {

            System.err.println("Le serveur ne répond plus ");
        }
    }

    /**
     * @param currentRoundTargetDistance the currentRoundTargetDistance to set
     */
    public void setCurrentRoundTargetDistance(int currentRoundTargetDistance) {
        this.currentRoundTargetDistance = currentRoundTargetDistance;
    }

    /**
     * @param currentRoundWindX the currentRoundWindX to set
     */
    public void setCurrentRoundWindX(int currentRoundWindX) {
        this.currentRoundWindX = currentRoundWindX;
    }

    /**
     * @param gameOver the gameOver to set
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * @param currentRoundWindY the currentRoundWindY to set
     */
    public void setCurrentRoundWindY(int currentRoundWindY) {
        this.currentRoundWindY = currentRoundWindY;
    }

    /**
     * 
     */
    public boolean getGameOver() {
        return gameOver;
    }

    public double getCurrentRoundTargetDistance() {
        return currentRoundTargetDistance;
    }

    public double getCurrentRoundWindX() {
        return currentRoundWindX;
    }

    public double getCurrentRoundWindY() {
        return currentRoundWindY;
    }
    
    public void arrowAdversary(String pseudo,double x,double y,int score){
        partie.addArrowToAdversary(pseudo,x,y,score);
    }
    
    public void newAdversary(String pseudo){
        partie.addNewAdversary(pseudo);
    }
    
    public void newRound(double distance,double x,double y){
        System.out.println("DISTANCE="+distance);
        partie.updateRound(distance,x,y);
    }
    
    public void adversaryLeft(String pseudo){
        partie.removeAdversary(pseudo);
    }

    public int getMaxRound() {
        return maxRound;
    }

    public void setEmission(Emission emission) {
        this.emission = emission;
    }

    public void setReception(Reception reception) {
        this.reception = reception;
    }
    
    public void setClassement(ArrayList<Joueur> classement){
        partie.setClassement(classement);
    }
}
