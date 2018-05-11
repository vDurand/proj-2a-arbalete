/**
 * @Author Valentin Durand - ENSICAEN
 * @Project CaenBowClient
 * @Package client
 * @Class Emission
 * @ Jan 4, 2017 1:37:23 PM
 */
package server;

import java.io.PrintWriter;
import java.util.Scanner;


public class Emission {

	private PrintWriter out;
	private Connexion c;
	
	public Emission(PrintWriter out, Connexion c) {
		this.out = out;
		this.c = c;
	}

        public void sendArrow(int score, double x, double y){
            out.flush();
            out.println("{\"score\":"+score+",\"arrow\":{\"x\":"+x+",\"y\":"+y+"}}");
            out.flush();
        }
        
        public void sendExit(){
            out.flush();
            out.println("{\"exit\":1}");
            out.flush();
            c.setGameOver(true);
        }
	
}
