/**
 * @Author Valentin Durand - ENSICAEN
 * @Project CaenBowClient
 * @Package client
 * @Class Client
 * @ Jan 4, 2017 1:37:04 PM
 */
package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import modele.Partie;

public class Client {

    public static Socket socket = null;
    public static Socket newSocket = null;
    public static Thread t1;
    private ArrayList<PartieDisponible> parties;
    private String ip;
    private Scanner sc;
    private Connexion c;
    private boolean noPartie;

    public Connexion getConnexion() {
        return c;
    }
        
        

    public ArrayList<PartieDisponible> getAvailableGame() {
        return parties;
    }

    public void joinPartie(int port){
        try {
            newSocket = new Socket(ip,port);
            c = new Connexion(newSocket,"2",sc);
            t1 = new Thread(c);
            t1.start();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean hasPartie() {
        return !noPartie;
    }

    public Client(boolean create) {
        noPartie = false;
        ip = "caenbow.youspin.it";//"127.0.0.1";//
        int port = 2014;
        int newPort;
        try {
            System.out.println("Demande de connexion");
            socket = new Socket(ip,port);

            System.out.println("Connected to server");

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sc = new Scanner(System.in);

            JSONObject obj = new JSONObject(in.readLine());
            System.out.println("---->"+obj);
            if(obj.has("ok")){
                if(obj.getInt("ok") == 1){
                    //System.out.println("Create : 1 | Join : 2");
                    //String choice = sc.nextLine();
                    if(create){
                        out.flush();
                        out.println("{\"create\":1}");
                        out.flush();
                        obj = new JSONObject(in.readLine());
                        System.out.println("---->"+obj);
                        if(obj.has("port")){
                            newPort = obj.getInt("port");
                            newSocket = new Socket(ip,newPort);
                            c = new Connexion(newSocket,"1",sc);
                            t1 = new Thread(c);
                            t1.start();
                        }
                        else{
                            System.out.println("Error no port returned");
                        }
                }
                else{
                        /*if(choice.equals("exit")){
                            out.flush();
                            out.println("{\"exit\":1}");
                            out.flush();
                        }
                        else{*/
                            out.flush();
                            out.println("{\"join\":1}");
                            out.flush();
                            obj = new JSONObject(in.readLine());
                            System.out.println("---->"+obj);
                            if(obj.has("games")){
                                parties = new ArrayList<PartieDisponible>();
                                JSONArray gameArray = obj.getJSONArray("games");
                                for(int i = 0; i < gameArray.length(); i++){
                                    parties.add(new PartieDisponible(
                                            gameArray.getJSONObject(i).getInt("port"), 
                                            gameArray.getJSONObject(i).getString("name"), 
                                            gameArray.getJSONObject(i).getInt("nbplayer")));
                                        /*System.out.println((i+1)+" : "
                                                        +gameArray.getJSONObject(i).getString("name")
                                                        +" ("+gameArray.getJSONObject(i).getInt("nbplayer")+"/4)");*/
                                }
                                //System.out.println("Which game?");
                                //int gameId = Integer.parseInt(sc.nextLine());

                            }
                            else{
                                noPartie = true;
                                //System.out.println("Error no port returned");
                            }
                        //}
                    }
                }
                else{
                        System.out.println("Error ok = 0");
                }
        }
        else{
                System.out.println("Error in connection");
            }
        } catch (UnknownHostException e) {
          System.err.println("Failed connecting to host");
        } catch (IOException e) {
          System.err.println("Failed connecting to server");
        }
    }

}
