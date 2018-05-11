/**
 * @Author Valentin Durand - ENSICAEN
 * @Project CaenBowClient
 * @Package client
 * @Class init_client
 * @ Jan 4, 2017 1:37:37 PM
 */
package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class init_client implements Runnable {

	private Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
	@SuppressWarnings("unused")
	private Scanner sc;
	@SuppressWarnings("unused")
	private Thread t3, t4;
	private Connexion c;
        private Emission em;
        private Reception re;
	
	public init_client(Socket s, Connexion c){
		socket = s;
		this.c = c;
	}
        
        public Emission getEmission(){
            return em;
        }
        
        public Reception getReception(){
            return re;
        }
	
	public void run() {
		try {
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			sc = new Scanner(System.in);
			
                        em = new Emission(out,c);
                        c.setEmission(em);
			//Thread t4 = new Thread(em);
			//t4.start();
                        re = new Reception(in,c);
                        c.setReception(re);
			Thread t3 = new Thread(re);
			t3.start();
		
		   
		    
		} catch (IOException e) {
			System.err.println("Le serveur distant s'est déconnecté !");
		}
	}

}
